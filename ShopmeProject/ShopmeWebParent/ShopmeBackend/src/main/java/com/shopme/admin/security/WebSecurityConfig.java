package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Bean
	public  UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	public DaoAuthenticationProvider authenticationProvider() { //nhin user tu database
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http.authorizeRequests()
		 	.antMatchers("/users/**").hasAuthority("Admin")
		 	.antMatchers("/Categories/**").hasAnyAuthority("Admin", "Editor")
		 	.antMatchers("/Brands/**").hasAnyAuthority("Admin", "Editor")
		 	.antMatchers("/Products/**").hasAnyAuthority("Admin", "Editor","Salesperson","Shipper")
		 	.antMatchers("/Questions/**").hasAnyAuthority("Admin", "Assistant")
		 	.antMatchers("/Reviews/**").hasAnyAuthority("Admin", "Assistant")
		 	.antMatchers("/Customers/**").hasAnyAuthority("Admin", "Salesperson")
		 	.antMatchers("/Shippings/**").hasAnyAuthority("Admin", "Salesperson")
		 	.antMatchers("/Orders/**").hasAnyAuthority("Admin", "Salesperson","Shipper")
		 	.antMatchers("/Reports/**").hasAnyAuthority("Admin", "Salesperson")
		 	.antMatchers("/Articles/**").hasAnyAuthority("Admin", "Editor")
		 	.antMatchers("/Menus/**").hasAnyAuthority("Admin", "Editor")
		 	.antMatchers("/Settings/**").hasAuthority("Admin")
		 .anyRequest()
//		 .permitAll(); //chop phep vao web ma khong can dang nhap
		 .authenticated()
		 .and()
		 .formLogin()
		 	.loginPage("/login")
		 	.usernameParameter("email")
		 	.permitAll()
		 .and().logout().permitAll()
//		 .and().rememberMe(); // dong nay de giu trang thai dang nhap cua user ke ca khi tat trinh duyet bat lai
		 						//nhung neu khoi dong lai ung dung thi se nphai dang nhap lai, do trinh duyet tu lay random 1 token nen khi khoi dong lai token thay doi
		 .and()
		 	.rememberMe()
		 	.key("abcafdsdfsdff_1234567890") // fix cung token => khoi dong lai ung dung van luu trang thai dang nhap tren web
		 	.tokenValiditySeconds(7 * 24 * 60 * 60); // thoi gian ton tai cua token(tuong ung voi thoi gian ton tai cua cookies),
		     										//het thoi gian nay token mat hieu luc va nguoi dung phai dang nhap lai
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception{
		web.ignoring().antMatchers("/images/**", "/js/**", "/webjars/**");
	}

}
