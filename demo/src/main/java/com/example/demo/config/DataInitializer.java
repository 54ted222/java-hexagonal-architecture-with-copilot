package com.example.demo.config;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Override
	public void run(String... args) throws Exception {
		// 建立測試使用者資料
		if (userService.findByUsername("admin").isEmpty()) {
			User admin = new User("admin", "admin123", "admin@example.com");
			admin.setFirstName("管理員");
			admin.setLastName("系統");
			userService.registerUser(admin);
			System.out.println("已建立管理員帳號: admin / admin123");
		}

		if (userService.findByUsername("testuser").isEmpty()) {
			User testUser = new User("testuser", "test123", "test@example.com");
			testUser.setFirstName("測試");
			testUser.setLastName("使用者");
			userService.registerUser(testUser);
			System.out.println("已建立測試帳號: testuser / test123");
		}
	}
}