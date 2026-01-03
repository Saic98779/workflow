package com.metaverse.workflow;

import com.metaverse.workflow.common.fileservice.StorageProperties;
import com.metaverse.workflow.common.fileservice.StorageService;
import com.metaverse.workflow.common.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableScheduling
@EnableAsync
@EnableCaching
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
@SpringBootApplication(scanBasePackages = "com.metaverse")
public class WorkflowApplication extends SpringBootServletInitializer {

    // Must set Log4j2 plugin packages before any LogManager.getLogger() call.
    // This ensures Log4j2 discovers custom appenders (like SplunkSdkAppender) when
    // running inside servlet containers where plugin scanning can be classloader-sensitive.
    static {
        // You can also set this as a JVM arg: -Dlog4j2.plugin.packages=com.metaverse.workflow.logging
        System.setProperty("log4j2.plugin.packages", "com.metaverse.workflow.logging");
    }

	private static final Logger LOGGER =
			LogManager.getLogger(WorkflowApplication.class);

	public static void main(String[] args) {
		LOGGER.error("SPLUNK-FINAL-TEST-FROM-JAVA");
		SpringApplication.run(WorkflowApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(WorkflowApplication.class);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	/**
	 * Application initialization.
	 * Splunk logging validation is done ONLY via Log4j2 SplunkHttp appender.
	 */
	@Bean
	CommandLineRunner init(StorageService storageService,
						   CommonUtil commonUtil) {

		return args -> {
			storageService.init();
			commonUtil.init();

			// ✅ Single startup log — this is enough to validate Splunk
			LOGGER.info("WorkflowApplication started successfully – Splunk logging active");
		};
	}

	@Bean
	CommandLineRunner splunkTestLogger() {
		return args -> {
			Logger log = LogManager.getLogger("SPLUNK_TEST");
			log.error("SPLUNK-JAVA-AFTER-STARTUP");
		};
	}

}
