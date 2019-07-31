package com.github.mogikanen9.devtest.api;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
       //HTTP Basic authentication
       http.httpBasic()
       .and()
       .authorizeRequests()
       .antMatchers(HttpMethod.GET, "/book/**").hasAnyRole("WRITER","READER")
       .antMatchers(HttpMethod.POST, "/book").hasRole("WRITER")
       .antMatchers(HttpMethod.PUT, "/book/**").hasRole("WRITER")
       .antMatchers(HttpMethod.PATCH, "/book/**").hasRole("WRITER")
       .antMatchers(HttpMethod.DELETE, "/book/**").hasRole("WRITER")
       .and()
       .csrf().disable()
       .formLogin().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("importer").password("{noop}Welcome13").roles("WRITER","READER")
                .and()
                .withUser("guiapp").password("{noop}Welcome2").roles("READER");
    }

}