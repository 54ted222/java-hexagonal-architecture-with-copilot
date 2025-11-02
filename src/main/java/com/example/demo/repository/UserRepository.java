package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * 根據使用者名稱查找使用者
	 * 
	 * @param username 使用者名稱
	 * @return 使用者物件
	 */
	Optional<User> findByUsername(String username);

	/**
	 * 根據電子郵件查找使用者
	 * 
	 * @param email 電子郵件
	 * @return 使用者物件
	 */
	Optional<User> findByEmail(String email);

	/**
	 * 檢查使用者名稱是否已存在
	 * 
	 * @param username 使用者名稱
	 * @return 是否存在
	 */
	boolean existsByUsername(String username);

	/**
	 * 檢查電子郵件是否已存在
	 * 
	 * @param email 電子郵件
	 * @return 是否存在
	 */
	boolean existsByEmail(String email);

	/**
	 * 查找所有啟用狀態的使用者
	 * 
	 * @param isActive 啟用狀態
	 * @return 使用者列表
	 */
	List<User> findByIsActive(Boolean isActive);

	/**
	 * 根據使用者名稱或電子郵件查找使用者（用於登入）
	 * 
	 * @param username 使用者名稱
	 * @param email    電子郵件
	 * @return 使用者物件
	 */
	@Query("SELECT u FROM User u WHERE u.username = :username OR u.email = :email")
	Optional<User> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

	/**
	 * 根據姓名關鍵字搜尋使用者
	 * 
	 * @param keyword 關鍵字
	 * @return 使用者列表
	 */
	@Query("SELECT u FROM User u WHERE u.firstName LIKE %:keyword% OR u.lastName LIKE %:keyword%")
	List<User> findByNameContaining(@Param("keyword") String keyword);
}