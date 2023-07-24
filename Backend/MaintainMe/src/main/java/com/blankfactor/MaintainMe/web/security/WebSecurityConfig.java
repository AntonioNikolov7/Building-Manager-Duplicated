package com.blankfactor.MaintainMe.web.security;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfiguration;

import java.security.Security;
import java.util.Arrays;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,CorsConfigurationSource corsConfigurationSource) throws Exception {
        http.csrf().disable();
        http.cors().configurationSource(corsConfigurationSource);
        http.oauth2Login();
        return http.build();

    }

//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//       return http
//                .authorizeRequests()
//                .requestMatchers("/**").fullyAuthenticated()
//                .and()
//                .csrf().disable()
//                .cors().disable()
//              //  .authorizeRequests().anyRequest().permitAll()
//               // .and()
//                .oauth2Login().and()
//               .build();
//
//    }

//    @Bean
//    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
//       return http
//                .authorizeRequests()
//                .requestMatchers("/**").fullyAuthenticated()
//                .and()
//                .csrf().disable()
//                .cors().and()
//                .authorizeRequests().anyRequest().permitAll()
//                .and()
//                .oauth2Login().and().build();
//
//    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5175", "http://127.0.0.1:5173", "http://localhost:5173")); // Allow specific origins
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
