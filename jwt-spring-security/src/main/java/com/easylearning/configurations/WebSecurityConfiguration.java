package com.easylearning.configurations;

import com.easylearning.filters.JwtRequestFilter;
//import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {

    @Autowired
    private JwtRequestFilter requestFilter;


    @Bean //Indicates that a method produces a bean to be managed by the Spring container.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //BCryptPasswordEncoder function provided by the spring security
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.csrf().disable()
                .authorizeHttpRequests()
                // Configure access for "/register", "/authentication", and "/api/welcome" endpoints for all users
                .requestMatchers("/register", "/authentication").permitAll()
                // Configure access for "/api/hello" endpoint for authenticated users
                .requestMatchers("/api/v1/user/**").hasAuthority("USER")
                .requestMatchers( "/api/v1/manager/**").hasAuthority("MANAGER")
                // Any other request requires authentication
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(requestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}

/*
   .addFilterBefore():
      This is a method provided by Spring Security's HttpSecurity configuration.
      It allows you to add a custom filter to the filter chain before a specified filter.

     requestFilter:
     This is the custom filter that you want to add to the filter chain.
     This filter typically performs some custom logic on incoming requests, such as validating tokens or headers.

     UsernamePasswordAuthenticationFilter.class:
     This is the class of the filter that you want your custom filter to be placed before.
     In this case, it's UsernamePasswordAuthenticationFilter, which is a standard Spring Security filter responsible for handling username and password authentication.

*/

/*

    when using roles we can use below methods :

                 .authorizeHttpRequests()
                     // Allow all other requests that are not yet authenticated
                    .anyRequest().authenticated()
                .authorizeHttpRequests()
                         // Configure access for "/register" and "/authentication" endpoints for all users
                    .antMatchers("/register", "/authentication").permitAll()
                         // Configure access for "/api/hello" endpoint for users with role "ROLE_USER"
                    .antMatchers("/api/hello").hasRole("USER")
                         // Configure access for other APIs with different roles as needed
                    .antMatchers("/api/some_other_endpoint").hasAnyRole("ADMIN", "MODERATOR")
            .and()


* */