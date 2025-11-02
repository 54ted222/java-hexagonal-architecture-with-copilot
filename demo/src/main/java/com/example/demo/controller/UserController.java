package com.example.demo.controller;

import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

	@Autowired
	private UserService userService;

	/**
	 * 使用者註冊
	 * 
	 * @param user 使用者資訊
	 * @return 註冊結果
	 */
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody User user) {
		try {
			String result = userService.registerUser(user);
			if (result.equals("註冊成功")) {
				return ResponseEntity.ok(result);
			} else {
				return ResponseEntity.badRequest().body(result);
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("註冊過程中發生錯誤: " + e.getMessage());
		}
	}

	/**
	 * 使用者登入
	 * 
	 * @param loginRequest 登入請求（包含使用者名稱和密碼）
	 * @return 登入結果
	 */
	@PostMapping("/login")
	public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest) {
		try {
			boolean isAuthenticated = userService.authenticateUser(
					loginRequest.getUsername(),
					loginRequest.getPassword());

			if (isAuthenticated) {
				return ResponseEntity.ok("登入成功");
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("使用者名稱或密碼錯誤");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("登入過程中發生錯誤: " + e.getMessage());
		}
	}

	/**
	 * 取得所有使用者
	 * 
	 * @return 使用者列表
	 */
	@GetMapping
	public ResponseEntity<List<User>> getAllUsers() {
		try {
			List<User> users = userService.findAllUsers();
			// 移除密碼資訊
			users.forEach(user -> user.setPassword(null));
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 根據ID取得使用者
	 * 
	 * @param id 使用者ID
	 * @return 使用者資訊
	 */
	@GetMapping("/{id}")
	public ResponseEntity<User> getUserById(@PathVariable Long id) {
		try {
			Optional<User> userOpt = userService.findById(id);
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				user.setPassword(null); // 移除密碼資訊
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 根據使用者名稱取得使用者
	 * 
	 * @param username 使用者名稱
	 * @return 使用者資訊
	 */
	@GetMapping("/username/{username}")
	public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
		try {
			Optional<User> userOpt = userService.findByUsername(username);
			if (userOpt.isPresent()) {
				User user = userOpt.get();
				user.setPassword(null); // 移除密碼資訊
				return ResponseEntity.ok(user);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 取得所有啟用的使用者
	 * 
	 * @return 啟用的使用者列表
	 */
	@GetMapping("/active")
	public ResponseEntity<List<User>> getActiveUsers() {
		try {
			List<User> users = userService.findActiveUsers();
			// 移除密碼資訊
			users.forEach(user -> user.setPassword(null));
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 更新使用者資訊
	 * 
	 * @param id   使用者ID
	 * @param user 更新的使用者資訊
	 * @return 更新結果
	 */
	@PutMapping("/{id}")
	public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User user) {
		try {
			Optional<User> existingUserOpt = userService.findById(id);
			if (existingUserOpt.isPresent()) {
				User existingUser = existingUserOpt.get();

				// 更新允許修改的欄位
				if (user.getFirstName() != null) {
					existingUser.setFirstName(user.getFirstName());
				}
				if (user.getLastName() != null) {
					existingUser.setLastName(user.getLastName());
				}
				if (user.getEmail() != null) {
					existingUser.setEmail(user.getEmail());
				}

				userService.updateUser(existingUser);
				return ResponseEntity.ok("使用者資訊更新成功");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("更新過程中發生錯誤: " + e.getMessage());
		}
	}

	/**
	 * 停用使用者帳號
	 * 
	 * @param id 使用者ID
	 * @return 操作結果
	 */
	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
		try {
			boolean success = userService.deactivateUser(id);
			if (success) {
				return ResponseEntity.ok("使用者帳號已停用");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("停用過程中發生錯誤: " + e.getMessage());
		}
	}

	/**
	 * 啟用使用者帳號
	 * 
	 * @param id 使用者ID
	 * @return 操作結果
	 */
	@PatchMapping("/{id}/activate")
	public ResponseEntity<String> activateUser(@PathVariable Long id) {
		try {
			boolean success = userService.activateUser(id);
			if (success) {
				return ResponseEntity.ok("使用者帳號已啟用");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("啟用過程中發生錯誤: " + e.getMessage());
		}
	}

	/**
	 * 修改使用者密碼
	 * 
	 * @param id              使用者ID
	 * @param passwordRequest 密碼修改請求
	 * @return 修改結果
	 */
	@PatchMapping("/{id}/password")
	public ResponseEntity<String> changePassword(@PathVariable Long id,
			@RequestBody PasswordChangeRequest passwordRequest) {
		try {
			boolean success = userService.changePassword(
					id,
					passwordRequest.getOldPassword(),
					passwordRequest.getNewPassword());

			if (success) {
				return ResponseEntity.ok("密碼修改成功");
			} else {
				return ResponseEntity.badRequest().body("舊密碼錯誤或使用者不存在");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("密碼修改過程中發生錯誤: " + e.getMessage());
		}
	}

	/**
	 * 根據姓名搜尋使用者
	 * 
	 * @param keyword 搜尋關鍵字
	 * @return 符合條件的使用者列表
	 */
	@GetMapping("/search")
	public ResponseEntity<List<User>> searchUsers(@RequestParam String keyword) {
		try {
			List<User> users = userService.searchUsersByName(keyword);
			// 移除密碼資訊
			users.forEach(user -> user.setPassword(null));
			return ResponseEntity.ok(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	/**
	 * 刪除使用者
	 * 
	 * @param id 使用者ID
	 * @return 刪除結果
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		try {
			boolean success = userService.deleteUser(id);
			if (success) {
				return ResponseEntity.ok("使用者已刪除");
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("刪除過程中發生錯誤: " + e.getMessage());
		}
	}

	// 內部類別用於接收登入請求
	public static class LoginRequest {
		private String username;
		private String password;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}
	}

	// 內部類別用於接收密碼修改請求
	public static class PasswordChangeRequest {
		private String oldPassword;
		private String newPassword;

		public String getOldPassword() {
			return oldPassword;
		}

		public void setOldPassword(String oldPassword) {
			this.oldPassword = oldPassword;
		}

		public String getNewPassword() {
			return newPassword;
		}

		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}
	}
}