package com.example.crudusuario.security;

import java.io.IOException;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilita CSRF para pruebas (activarlo en producción)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/registro", "/css/**", "/js/**", "/images/**", "/webjars/**").permitAll() // Recursos públicos
                .requestMatchers("/admin/**").hasAuthority("ADMIN") // Acceso solo para ADMIN
                .requestMatchers("/user/**").hasAnyAuthority("USER", "ADMIN") // Acceso para USER y ADMIN
                .requestMatchers("/proyectos/**", "/tareas/**").authenticated() // Acceso autenticado
                .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
            )
            .formLogin(form -> form
                .loginPage("/login") // Página de login personalizada
                .successHandler(customAuthenticationSuccessHandler()) // Manejador de redirección
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new AuthenticationSuccessHandler() {
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                                org.springframework.security.core.Authentication authentication) throws IOException, ServletException {
                Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

                if (roles.contains("ADMIN")) {
                    response.sendRedirect("/admin/dashboard"); // Redirige al dashboard si es ADMIN
                } else {
                    response.sendRedirect("/usuario/home"); // Redirige al home si es USER
                }
            }
        };
    }
}
