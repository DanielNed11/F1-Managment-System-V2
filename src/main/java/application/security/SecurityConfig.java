package application.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Order(1)
    SecurityFilterChain apiSecurityFilterChain(HttpSecurity security) throws Exception {
        return security.securityMatcher("/api/**")
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.GET, "/api/drivers/search").permitAll()
                        // permitAll is used so the separate Client project can test adding teams without logging in.
                        .requestMatchers(HttpMethod.POST, "/api/teams").permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(
                        handling -> handling
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .csrf(csrf -> csrf.ignoringRequestMatchers(
                        // CSRF is disabled only for the separate Client project's add-team endpoint.
                        request ->
                                "POST".equals(request.getMethod())
                                && "/api/teams".equals(request.getServletPath())
                ))
                .build();
    }


    @Bean
    @Order(2)
    SecurityFilterChain securityFilterChain(final HttpSecurity security) throws Exception {

        return security
                .authorizeHttpRequests(req -> req
                        .requestMatchers(HttpMethod.GET, "/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/drivers").permitAll()
                        .requestMatchers("/js/**", "/css/**", "/img/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll())
                .exceptionHandling(handling ->
                        handling.authenticationEntryPoint((
                                (request,
                                 response,
                                 authException) ->
                                        response.sendRedirect("/login")
                        )))
                .build();
    }

    @Bean
    WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:9000")
                        .allowedMethods("GET", "POST");
            }
        };
    }
}
