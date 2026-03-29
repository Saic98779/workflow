package com.metaverse.workflow.security;

import com.metaverse.workflow.security.config.CustomAccessDeniedHandler;
import com.metaverse.workflow.security.config.CustomAuthenticationEntryPoint;
import com.metaverse.workflow.security.config.ExceptionHandlerFilter;
import com.metaverse.workflow.security.filter.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    // --- Permit-all URL patterns ---
    private static final String[] SWAGGER_PATTERNS = {
            "/swagger-ui/**", "/swagger-ui.html",
            "/v3/api-docs/**",
            "/api/swagger-ui/**", "/api/swagger-ui.html",
            "/api/v3/api-docs/**"
    };

    private static final String[] PUBLIC_PATTERNS = {
            "/auth/**",
            "/workflow/auth/**",
            "/visitor-count/**",
            "/ramp/registrations/**",
            "/ramp/enrollments/**"
    };

    // --- Session config ---
    /** Max concurrent sessions per user. Set to -1 to allow unlimited. */
    private static final int MAX_SESSIONS_PER_USER = 1;

    /**
     * When true  → blocks a second login while the first is active.
     * When false → invalidates the first session, allowing the new login.
     */
    private static final boolean BLOCK_SECOND_LOGIN = false;

    // --- Dependencies ---
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final ExceptionHandlerFilter exceptionHandlerFilter;

    @Value("${cors.allowed-origins:http://localhost:*}")
    private List<String> allowedOrigins;

    @Value("${swagger.enabled:false}")
    private boolean swaggerEnabled;

    public WebSecurityConfig(
            JwtAuthenticationFilter jwtAuthFilter,
            @Qualifier("customUserDetailsService") UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            CustomAuthenticationEntryPoint authenticationEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler,
            ExceptionHandlerFilter exceptionHandlerFilter
    ) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.accessDeniedHandler = accessDeniedHandler;
        this.exceptionHandlerFilter = exceptionHandlerFilter;
    }

    // --- CORS ---

    /**
     * Configures CORS to allow credentialed requests from permitted origins.
     * Origins are externalized via {@code cors.allowed-origins} in application properties.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);

        // Specific configuration for images
        CorsConfiguration imageConfig = new CorsConfiguration();
        imageConfig.addAllowedOrigin("http://localhost:4200");
        imageConfig.addAllowedMethod("GET");
        imageConfig.addAllowedHeader("*");
        imageConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/images/**", imageConfig);
        return source;
    }

    // --- Security filter chain ---

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();

                    // Swagger is only accessible in non-production environments.
                    // Controlled via swagger.enabled in profile-specific properties.
                    if (swaggerEnabled) {
                        auth.requestMatchers(SWAGGER_PATTERNS).permitAll();
                    }

                    auth.requestMatchers(PUBLIC_PATTERNS).permitAll()
                            .anyRequest().authenticated();
                })
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(MAX_SESSIONS_PER_USER)
                        .maxSessionsPreventsLogin(BLOCK_SECOND_LOGIN)
                        .expiredSessionStrategy(sessionExpiredStrategy())
                        .sessionRegistry(sessionRegistry())
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(exceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- Session management ---

    /**
     * Returns a JSON 403 response when a session is invalidated due to a newer login.
     * Triggered only when {@link #BLOCK_SECOND_LOGIN} is {@code false}.
     */
    private SessionInformationExpiredStrategy sessionExpiredStrategy() {
        return event -> {
            HttpServletResponse response = event.getResponse();
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("""
                    {"code":"SESSION_EXPIRED","message":"Your session was invalidated because you signed in from another location.","success":false}
                    """);
        };
    }

    /**
     * Tracks active sessions. Required for concurrent session control.
     * Works in tandem with {@link #httpSessionEventPublisher()}.
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * Publishes session lifecycle events (created/destroyed) to the Spring context
     * so the {@link SessionRegistry} stays in sync.
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    // --- Authentication ---

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}