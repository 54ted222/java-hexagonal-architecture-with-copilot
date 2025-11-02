package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	/**
	 * 註冊新使用者
	 * 
	 * @param user 使用者物件
	 * @return 註冊結果訊息
	 */
	public String registerUser(User user) {
		// 檢查使用者名稱是否已存在
		if (userRepository.existsByUsername(user.getUsername())) {
			return "使用者名稱已存在";
		}

		// 檢查電子郵件是否已存在
		if (userRepository.existsByEmail(user.getEmail())) {
			return "電子郵件已存在";
		}

		// 加密密碼
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		// 儲存使用者
		userRepository.save(user);
		return "註冊成功";
	}

	/**
	 * 使用者登入驗證
	 * 
	 * @param username 使用者名稱或電子郵件
	 * @param password 密碼
	 * @return 登入是否成功
	 */
	public boolean authenticateUser(String username, String password) {
		Optional<User> userOpt = userRepository.findByUsernameOrEmail(username, username);

		if (userOpt.isPresent()) {
			User user = userOpt.get();
			// 檢查帳號是否啟用
			if (!user.getIsActive()) {
				return false;
			}
			// 驗證密碼
			return passwordEncoder.matches(password, user.getPassword());
		}

		return false;
	}

	/**
	 * 根據使用者名稱查找使用者
	 * 
	 * @param username 使用者名稱
	 * @return 使用者物件
	 */
	public Optional<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	/**
	 * 根據電子郵件查找使用者
	 * 
	 * @param email 電子郵件
	 * @return 使用者物件
	 */
	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	/**
	 * 根據ID查找使用者
	 * 
	 * @param id 使用者ID
	 * @return 使用者物件
	 */
	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}

	/**
	 * 取得所有使用者
	 * 
	 * @return 使用者列表
	 */
	public List<User> findAllUsers() {
		return userRepository.findAll();
	}

	/**
	 * 取得所有啟用的使用者
	 * 
	 * @return 啟用的使用者列表
	 */
	public List<User> findActiveUsers() {
		return userRepository.findByIsActive(true);
	}

	/**
	 * 更新使用者資訊
	 * 
	 * @param user 使用者物件
	 * @return 更新後的使用者物件
	 */
	public User updateUser(User user) {
		return userRepository.save(user);
	}

	/**
	 * 停用使用者帳號
	 * 
	 * @param userId 使用者ID
	 * @return 操作是否成功
	 */
	public boolean deactivateUser(Long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setIsActive(false);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	/**
	 * 啟用使用者帳號
	 * 
	 * @param userId 使用者ID
	 * @return 操作是否成功
	 */
	public boolean activateUser(Long userId) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setIsActive(true);
			userRepository.save(user);
			return true;
		}
		return false;
	}

	/**
	 * 修改使用者密碼
	 * 
	 * @param userId      使用者ID
	 * @param oldPassword 舊密碼
	 * @param newPassword 新密碼
	 * @return 修改是否成功
	 */
	public boolean changePassword(Long userId, String oldPassword, String newPassword) {
		Optional<User> userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			// 驗證舊密碼
			if (passwordEncoder.matches(oldPassword, user.getPassword())) {
				// 設定新密碼
				user.setPassword(passwordEncoder.encode(newPassword));
				userRepository.save(user);
				return true;
			}
		}
		return false;
	}

	/**
	 * 根據姓名關鍵字搜尋使用者
	 * 
	 * @param keyword 關鍵字
	 * @return 使用者列表
	 */
	public List<User> searchUsersByName(String keyword) {
		return userRepository.findByNameContaining(keyword);
	}

	/**
	 * 刪除使用者
	 * 
	 * @param userId 使用者ID
	 * @return 刪除是否成功
	 */
	public boolean deleteUser(Long userId) {
		if (userRepository.existsById(userId)) {
			userRepository.deleteById(userId);
			return true;
		}
		return false;
	}
}