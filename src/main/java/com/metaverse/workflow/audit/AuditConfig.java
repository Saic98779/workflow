package com.metaverse.workflow.audit;

import com.metaverse.workflow.audit.filter.RequestResponseWrappingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AuditConfig {

    @Bean
    public RequestResponseWrappingFilter requestResponseWrappingFilter() {
        return new RequestResponseWrappingFilter();
    }
}

