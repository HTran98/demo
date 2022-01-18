package com.h3phonestore.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.h3phonestore.jwt.JwtAuthenticationFilter;

@EnableWebSecurity
public class WebConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userService;

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter();
	}

	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// Get AuthenticationManager bean
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService) // Cung cáp userservice cho spring security
				.passwordEncoder(passwordEncoder()); // cung cấp password encoder
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/h3phone/**", "/register", "/forgotPassword/**", "/css/**", "/js/**", "/admin/**",
						"/images/**", "/src/main/resources/static/images/imagesProduct/**")
				.permitAll()
				.antMatchers("/adminPage/home", "/adminPage/invoice/*", "/adminPage/productDetail/*",
						"/adminPage/contact/*", "/adminPage/headPhone/*")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_MANAGER")
				.antMatchers("/adminPage/user/*", "/adminPage/role/*", "/adminPage/brand/*", "/adminPage/report/*",
						"/admin/reportRevenue/*")
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")
				.antMatchers("/editUser/**", "/shoppingCart/**", "/delivery/**", "/customerReview/**", "/checkout/**",
						"/contact/**")
				.hasAuthority("ROLE_USER").anyRequest().authenticated().and().formLogin().loginPage("/login")
				.permitAll().defaultSuccessUrl("/h3phone").and().rememberMe()
				.tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30)).key("h3phoneStore").and().logout()
				.clearAuthentication(true).invalidateHttpSession(true).deleteCookies("JSESSIONID","remember-me").logoutSuccessUrl("/login").and()
				.exceptionHandling().accessDeniedPage("/403");

		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
