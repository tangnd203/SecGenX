package com.ibatulanand.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.config.web.server.ServerHttpSecurity.HeaderSpec.FrameOptionsSpec; // Import cần thiết cho một số phiên bản

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                // MÃ FIX LỖI CWE-352: Tắt CSRF nhưng khẳng định Session là STATELESS.
                // Việc thiết lập STATELESS Session Creation Policy xác nhận rằng 
                // ứng dụng không sử dụng Session Cookies, từ đó làm giảm rủi ro CSRF.
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                
                // Thêm cấu hình Session Management
                .sessionManagement(spec -> spec
                        .sessionCreationPolicy(org.springframework.security.web.server.session.ServerSessionCreationPolicy.STATELESS)) 
                
                // MÃ FIX LỖI CWE-668: Yêu cầu xác thực cho /eureka/**
                .authorizeExchange(exchange ->
                        exchange
                                .pathMatchers("/eureka/**")
                                .authenticated()
                                .anyExchange()
                                .authenticated())
                                
                .oauth2ResourceServer(oAuth2ResourceServerSpec -> 
                        oAuth2ResourceServerSpec.jwt(Customizer.withDefaults()));
                        
        return serverHttpSecurity.build();
    }
}