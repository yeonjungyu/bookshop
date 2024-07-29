package com.avi6.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.avi6.service.UserDetailService;
import com.avi6.service.UserService;

import lombok.AllArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


@EnableWebSecurity
@AllArgsConstructor
@Configuration
public class SecurityConfig {
	
	private final UserDetailService userService;
	
	@Bean
		public SecurityFilterChain FilterChain(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
			.requestMatchers("/**","/login","/register").permitAll()
			.requestMatchers("/community/new-talk").permitAll()
			 .requestMatchers("/community/**").authenticated()
			.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/index")
			.permitAll()
		.and()
		.logout()
	    .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 로그아웃 요청을 처리할 URL을 지정합니다.
	    .logoutSuccessUrl("/") // 로그아웃 성공 후 리다이렉트할 URL을 지정합니다.
	    .invalidateHttpSession(true) // HTTP 세션을 무효화할지 여부를 지정합니다.
	    .deleteCookies("JSESSIONID") // 로그아웃 시 삭제할 쿠키 이름을 지정합니다.
	    .permitAll()
		.and()
			.csrf().disable();
		
		return http.build();
		}
	
	// 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
        throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService) // 사용자 정보 서비스 설정
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }
    
    
 // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}