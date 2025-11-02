# Spring Boot 使用者管理系統 - AI 編程助手指導

## 架構概觀

這是一個標準的三層 Spring Boot 應用程式，實作使用者註冊與登入功能：

- **Entity層**: `com.example.demo.entity.User` - JPA實體，使用`@Table(name = "users")`
- **Repository層**: `com.example.demo.repository.UserRepository` - Spring Data JPA repository
- **Service層**: `com.example.demo.service.UserService` - 業務邏輯，包含密碼加密 (BCrypt)
- **Controller層**: `com.example.demo.controller.UserController` - RESTful API endpoints

## 關鍵開發模式

### 1. 環境配置模式
專案使用 Spring Profiles 進行環境隔離：
- **開發模式**: `application-dev.properties` (預設) - 啟用H2 Console、詳細日誌
- **生產模式**: `application-prod.properties` - 停用H2 Console、檔案資料庫

```bash
# 開發環境啟動 (預設)
./mvnw spring-boot:run

# 生產環境啟動
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

### 2. 安全配置模式
`SecurityConfig.java` 根據環境動態配置：
- 開發模式：允許 H2 Console 訪問 (`/h2-console/**`)
- 生產模式：完全禁止 H2 Console 訪問
- 使用 `isDevelopmentMode()` 方法檢測當前環境

### 3. 資料初始化模式
`DataInitializer` 實作 `CommandLineRunner`，啟動時自動建立測試帳號：
- 管理員：`admin` / `admin123`
- 測試用戶：`testuser` / `test123`

### 4. 密碼處理模式
所有密碼使用 BCrypt 加密：
- 註冊時：`passwordEncoder.encode(password)`
- 登入驗證：`passwordEncoder.matches(rawPassword, encodedPassword)`

## API 設計慣例

### 端點命名
- 註冊：`POST /api/users/register`
- 登入：`POST /api/users/login`
- CRUD操作：標準 RESTful 風格 (`GET /api/users`, `PUT /api/users/{id}`)

### 回應格式
- 成功：返回中文成功訊息 (`"註冊成功"`, `"登入成功"`)
- 失敗：返回中文錯誤訊息 (`"使用者名稱已存在"`, `"密碼錯誤"`)

### 內嵌類別模式
Controller 使用靜態內嵌類別處理特定請求：
```java
public static class LoginRequest {
    private String username;
    private String password;
    // getters and setters
}
```

## 資料庫策略

### 開發環境
- H2 記憶體資料庫：`jdbc:h2:mem:testdb`
- JPA設定：`ddl-auto=create-drop` (每次重啟重建)
- H2 Console：`http://localhost:8080/h2-console`

### 生產環境
- H2 檔案資料庫：`jdbc:h2:file:./data/userdb`
- JPA設定：`ddl-auto=validate` (只驗證 schema)
- 預設在 `./data/` 目錄建立資料庫檔案

## 實體設計慣例

`User` 實體特點：
- 使用 `@Column` 註解指定資料庫欄位名稱 (如 `first_name`, `last_name`)
- 唯一約束：`username` 和 `email`
- 自動時間戳：`createdAt`, `updatedAt`
- 軟刪除支援：`isActive` 欄位

## 建置與測試

```bash
# 編譯專案
./mvnw clean compile

# 運行測試
./mvnw test

# 打包應用程式
./mvnw clean package

# 啟動應用程式
./mvnw spring-boot:run
```

## 常見開發工作流程

1. **新增API端點**：先在 Controller 定義，然後在 Service 實作業務邏輯
2. **資料庫變更**：修改 Entity，開發環境會自動重建 schema
3. **安全配置**：在 `SecurityConfig.authorizeHttpRequests()` 中添加路徑規則
4. **環境切換**：修改 `application.properties` 中的 `spring.profiles.active` 值

## 關鍵檔案參考

- `SecurityConfig.java` - 安全配置與環境檢測邏輯
- `DataInitializer.java` - 測試資料初始化
- `application-{env}.properties` - 環境特定配置
- `UserService.java` - 核心業務邏輯與密碼處理