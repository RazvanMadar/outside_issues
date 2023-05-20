package com.license.outside_issues.config;

import com.license.outside_issues.repository.CitizenRepository;
import com.license.outside_issues.service.authentication.jwt.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@EnableWebSecurity()
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CitizenRepository citizenRepository;

    private final JwtFilter jwtFilter;

    public SecurityConfig(CitizenRepository citizenRepository, JwtFilter jwtFilter) {
        this.citizenRepository = citizenRepository;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(username -> citizenRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found.")));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/api/email", "/api/issues/filtered/**", "/api/issues", "/api/images/*", "/api/citizens/auth/**", "/send/**", "/sendMessage/**", "/send-message/**", "/send-update/**", "/topic/**", "/app/**", "/ws-message/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/citizen-reactions", "/api/issues/basic-statistics", "/api/issues/year-statistics", "/api/issues/type-statistics", "/api/images/*/first").permitAll()
                .antMatchers(HttpMethod.POST, "/api/citizen/images/*").permitAll()
                .antMatchers(HttpMethod.GET, "/api/citizen/images/*").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.DELETE, "/api/citizen/images/*").hasAnyAuthority("ROLE_USER")
                .antMatchers("/api/blacklists/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.POST, "/api/citizen-reactions").hasAnyAuthority("ROLE_USER")
                .antMatchers(HttpMethod.GET, "/api/citizens").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers("/api/citizens/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.GET, "/api/images/**").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/issues/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/issues/*").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/issues/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers("/api/messages/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .antMatchers(HttpMethod.POST, "/api/rejected").hasAnyAuthority("ROLE_ADMIN")
                .antMatchers(HttpMethod.GET, "/api/rejected/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .anyRequest().authenticated();

        http.exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> {
                            response.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    ex.getMessage()
                            );
                        }
                );

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}