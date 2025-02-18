package com.shop.config;

import com.shop.service.MemberService;
import com.shop.service.CustomOAuth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // CSRF 설정
                .csrf()
                .ignoringAntMatchers("/api/**", "/qna/**") //
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()

                // 로그인 설정
                .formLogin()
                .loginPage("/members/login")
                .loginProcessingUrl("/members/login/process")
                .defaultSuccessUrl("/", true)
                .failureUrl("/members/login/error")
                .usernameParameter("email")
                .and()

                // 로그아웃 설정
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/")
                .and()

                // 권한 설정
                .authorizeRequests()
                .antMatchers("/", "/members/login", "/members/new", "/css/**", "/js/**", "/img/**", "/images/**", "/members/check-email", "/item/**", "/category/**", "/api/mileage/summary", "/api/members/info", "/api/reservations/**", "/api/qna/**", "/api/chatbot/**",
                        "/api/faqs/**", "/customer-service/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/admin/**", "/admin/**","/coupon/**").hasRole("ADMIN")
                .antMatchers("/mypage/**","/api/members/info","/orders/**").authenticated()
                .anyRequest().authenticated()
                .and()

                // OAuth2 로그인 설정
                .oauth2Login()
                .loginPage("/members/login")
                .defaultSuccessUrl("/", true)
                .failureUrl("/members/login/error")
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        // H2 콘솔 사용을 위한 설정
        http.headers().frameOptions().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }
}