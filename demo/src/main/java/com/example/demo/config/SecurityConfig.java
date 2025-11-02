package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private Environment environment;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(authz -> {
					authz.requestMatchers("/api/users/register", "/api/users/login").permitAll();

					// 只有在開發模式下才允許訪問 H2 Console
					if (isDevelopmentMode()) {
						authz.requestMatchers("/h2-console/**").permitAll();
					} else {
						authz.requestMatchers("/h2-console/**").denyAll();
					}

					authz.anyRequest().permitAll();
				});

		// 只有在開發模式下才停用 frame options（用於 H2 Console）
		if (isDevelopmentMode()) {
			http.headers(headers -> headers
					.frameOptions(frameOptions -> frameOptions.disable())); // 允許 H2 Console 使用 iframe
		}

		return http.build();
	}

	/**
	 * 檢查是否為開發模式
	 * 
	 * @return true 如果是開發模式，false 否則
	 */
	private boolean isDevelopmentMode() {
		String[] activeProfiles = environment.getActiveProfiles();
		String[] defaultProfiles = environment.getDefaultProfiles();
		
		// 檢查 active profiles
		for (String profile : activeProfiles) {
			if ("dev".equals(profile) || "development".equals(profile)) {
				return true;
			}
			if ("prod".equals(profile) || "production".equals(profile)) {
				return false;
			}
		}
		
		// 如果沒有設定 active profiles，檢查 default profiles
		if (activeProfiles.length == 0) {
			for (String profile : defaultProfiles) {
				if ("default".equals(profile)) {
					return true; // 預設為開發模式
				}
			}
		}
		
		// 如果都沒有設定，預設為開發模式
		return activeProfiles.length == 0;
	}
}