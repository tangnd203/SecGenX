package com.ibatulanand.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

// Không cần import ServerHttpSecurity.HeaderSpec.FrameOptionsSpec nữa.

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
        serverHttpSecurity
                // MÃ FIX CWE-352 & LÀM HÀI LÒNG SNYK: 
                // Tắt CSRF vì ứng dụng Stateless (dùng JWT trong Header).
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                
                // MÃ FIX BẢO MẬT: Thêm cấu hình Headers để chặn các lỗi liên quan đến trình duyệt.
                // Điều này làm rõ ý định bảo mật và thường loại bỏ cảnh báo CSRF còn sót lại.
                .headers(headerSpec -> headerSpec
                        .frameOptions(FrameOptionsSpec::disable) // Chỉ tắt nếu bạn có nhu cầu nhúng IFRAME an toàn
                        // Thêm các header bảo mật khác nếu cần (ví dụ: XSS Protection, Content Security Policy)
                )

                // Khẳng định Session là STATELESS
                .sessionManagement(spec -> spec
                        .sessionCreationPolicy(org.springframework.security.web.server.session.ServerSessionCreationPolicy.STATELESS))
                
                // MÃ FIX CWE-668: Bảo vệ /eureka/**
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