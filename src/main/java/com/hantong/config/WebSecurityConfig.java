package com.hantong.config;

import com.hantong.application.UserDetailServiceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
//https://spring.io/guides/gs/securing-web/
public class WebSecurityConfig {

        @Bean
        public static UserDetailServiceProvider userServiceProvier(){
                return new UserDetailServiceProvider();
        }

        @Bean
        public static PasswordEncoder md5PasswordEncoder(){
            return new PasswordEncoder() {
                private String toHex(byte[] bytes) {

                    final char[] HEX_DIGITS = "0123456789ABCDEF".toCharArray();
                    StringBuilder ret = new StringBuilder(bytes.length * 2);
                    for (int i=0; i<bytes.length; i++) {
                        ret.append(HEX_DIGITS[(bytes[i] >> 4) & 0x0f]);
                        ret.append(HEX_DIGITS[bytes[i] & 0x0f]);
                    }
                    return ret.toString();
                }

                private String getSecretKey() {
                    return "Yuenjee";
                }

                private String getDigestAlgorithm(){
                    return "HmacMD5";
                }

                @Override
                public String encode(CharSequence rawPassword) {
                    String encodePwd = "";
                    try {
                        SecretKey secretKey = new SecretKeySpec(this.getSecretKey().getBytes("UTF-8"), this.getDigestAlgorithm());
                        Mac mac = Mac.getInstance(this.getDigestAlgorithm());
                        mac.init(secretKey);
                        mac.update(rawPassword.toString().getBytes("UTF-8"));
                        byte[]bytes=  mac.doFinal();
                        encodePwd = this.toHex(bytes);
                    }catch (Exception e){}

                    return encodePwd;
                }

                @Override
                public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    return this.encode(rawPassword).equals(encodedPassword.toUpperCase());
                }
            };
        }

        @Configuration
        @EnableWebSecurity
        @EnableGlobalMethodSecurity(securedEnabled = true)
        @Order(1)
        public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

                @Override
                protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                        auth.userDetailsService(WebSecurityConfig.userServiceProvier())
                                .passwordEncoder(WebSecurityConfig.md5PasswordEncoder());
                }

                protected void configure(HttpSecurity http) throws Exception {
                    http.csrf().disable();
                    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                            .and().antMatcher("/api/**")
                            .authorizeRequests()
                            .antMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()
                            .antMatchers("/api/admin/**").hasRole("ADMIN")
                            .and().httpBasic();
                }
        }

        @Configuration
        @EnableWebSecurity
        @EnableGlobalMethodSecurity(securedEnabled = true)
        @Order(2)
        public static class StaticWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(HttpSecurity http) throws Exception {
                http
                        .authorizeRequests()
                        .antMatchers("/static/**")
                        .permitAll();
            }
        }

        @Configuration
        @EnableWebSecurity
        @EnableGlobalMethodSecurity(securedEnabled = true)
        public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

            @Override
            protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.userDetailsService(WebSecurityConfig.userServiceProvier())
                        .passwordEncoder(WebSecurityConfig.md5PasswordEncoder());
            }
                @Override
                protected void configure(HttpSecurity http) throws Exception {
                        http
                        .authorizeRequests()
                        .antMatchers("/","/swagger-resources/**", "/v2/api-docs","/swagger-ui.html","/webjars/**")
                        .permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .formLogin()
                        .loginPage("/login")
                        .permitAll()
                        .and()
                        .logout()
                        .permitAll();
                }
        }
}
