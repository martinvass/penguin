package com.github.martinyes.penguinapp.auth.security.config;

import com.github.martinyes.penguinapp.auth.user.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuration class for Spring Security.
 */
@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class WebSecurityConfig {

    private final AppUserService appUserService;
    private final Argon2PasswordEncoder argon2PasswordEncoder;

    private final String[] ENDPOINTS_WHITELIST = {
            "/",
            "/images/**",
            "/css/**",
            "/js/**",
            "/auth/login",
            "/auth/registration",
            "/features"
    };

    private final String LOGIN_URL = "/auth/login";
    private final String LOGIN_FAIL_URL = LOGIN_URL + "?error=true";
    private final String DEFAULT_SUCCESS_URL = "/dashboard";

    /**
     * Configures Spring Security's filter chain.
     *
     * @param http HttpSecurity instance to configure.
     * @return Configured SecurityFilterChain instance.
     * @throws Exception If an error occurs while configuring.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_WHITELIST).permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage(LOGIN_URL)
                        .loginProcessingUrl(LOGIN_URL)
                        .defaultSuccessUrl(DEFAULT_SUCCESS_URL, true)
                        .failureUrl(LOGIN_FAIL_URL)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl(LOGIN_URL + "?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    /**
     * Creates and configures an AuthenticationManager.
     *
     * @param http HttpSecurity instance used to access the AuthenticationManagerBuilder.
     * @return AuthenticationManager instance.
     * @throws Exception If an error occurs while configuring.
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());

        return authenticationManagerBuilder.build();
    }

    /**
     * Creates and configures a DaoAuthenticationProvider.
     *
     * @return Configured DaoAuthenticationProvider instance.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider();
        provider.setPasswordEncoder(argon2PasswordEncoder);
        provider.setUserDetailsService(appUserService);

        return provider;
    }
}