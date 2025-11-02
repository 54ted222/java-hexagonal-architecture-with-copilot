# 使用者登入系統 Demo

這是一個使用 Spring Boot 開發的使用者登入系統，包含使用者註冊、登入、管理等功能。

## 功能特色

- ✅ 使用者註冊與登入
- ✅ 密碼加密儲存 (BCrypt)
- ✅ 使用者資料管理 (CRUD)
- ✅ RESTful API
- ✅ H2 內存資料庫
- ✅ Spring Security 整合
- ✅ JPA/Hibernate ORM

## 技術堆疊

- **後端**: Spring Boot 3.5.4
- **資料庫**: H2 Database (開發環境)
- **安全**: Spring Security + BCrypt
- **ORM**: Spring Data JPA + Hibernate
- **建置工具**: Maven
- **Java 版本**: 21

## 快速開始

### 1. 環境配置

系統支援多種環境配置：

#### 開發環境 (預設)

```bash
# 使用開發環境配置
./mvnw spring-boot:run

# 或明確指定開發環境
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

#### 生產環境

```bash
# 使用生產環境配置
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

**重要安全特性**：

- 🔒 **H2 Console 僅在開發模式下可用**
- 🔒 生產環境自動停用 H2 Console 存取
- 🔒 生產環境使用較低的日誌級別

### 2. 啟動應用程式

```bash
./mvnw spring-boot:run
```

應用程式將在 `http://localhost:8080` 啟動

### 3. 測試帳號

系統已預建兩個測試帳號：

- **管理員帳號**: `admin` / `admin123`
- **一般使用者**: `testuser` / `test123`

### 4. H2 資料庫控制台（僅開發模式）

**僅在開發模式下**可訪問 `http://localhost:8080/h2-console` 來查看資料庫

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **使用者名稱**: `sa`
- **密碼**: `password`

**注意**：生產環境 (`spring.profiles.active=prod`) 下，H2 Console 將被完全停用以確保安全。

## API 端點

### 使用者註冊

```http
POST /api/users/register
Content-Type: application/json

{
  "username": "newuser",
  "password": "password123",
  "email": "user@example.com",
  "firstName": "用戶",
  "lastName": "姓名"
}
```

### 使用者登入

```http
POST /api/users/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}
```

### 取得所有使用者

```http
GET /api/users
```

### 根據 ID 取得使用者

```http
GET /api/users/{id}
```

### 根據使用者名稱取得使用者

```http
GET /api/users/username/{username}
```

### 取得所有啟用的使用者

```http
GET /api/users/active
```

### 更新使用者資訊

```http
PUT /api/users/{id}
Content-Type: application/json

{
  "firstName": "新名字",
  "lastName": "新姓氏",
  "email": "newemail@example.com"
}
```

### 修改密碼

```http
PATCH /api/users/{id}/password
Content-Type: application/json

{
  "oldPassword": "currentPassword",
  "newPassword": "newPassword123"
}
```

### 停用使用者帳號

```http
PATCH /api/users/{id}/deactivate
```

### 啟用使用者帳號

```http
PATCH /api/users/{id}/activate
```

### 搜尋使用者（根據姓名）

```http
GET /api/users/search?keyword=關鍵字
```

### 刪除使用者

```http
DELETE /api/users/{id}
```

## 資料庫結構

### users 表格

| 欄位名稱   | 資料類型     | 說明             |
| ---------- | ------------ | ---------------- |
| id         | BIGINT       | 主鍵，自動遞增   |
| username   | VARCHAR(50)  | 使用者名稱，唯一 |
| password   | VARCHAR(255) | 加密後的密碼     |
| email      | VARCHAR(100) | 電子郵件，唯一   |
| first_name | VARCHAR(50)  | 名字             |
| last_name  | VARCHAR(50)  | 姓氏             |
| created_at | TIMESTAMP    | 建立時間         |
| updated_at | TIMESTAMP    | 更新時間         |
| is_active  | BOOLEAN      | 帳號啟用狀態     |

## 使用 cURL 測試

### 註冊新使用者

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "password": "password123",
    "email": "newuser@example.com",
    "firstName": "新",
    "lastName": "使用者"
  }'
```

### 使用者登入

```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### 取得所有使用者

```bash
curl -X GET http://localhost:8080/api/users
```

## 安全設定

- 🔐 密碼使用 BCrypt 加密
- 🔐 API 端點中 `/register` 和 `/login` 允許匿名存取
- 🔐 **H2 Console 僅在開發環境中啟用**
- 🔐 生產環境自動停用 H2 Console 存取
- 🔐 環境感知的安全配置
- 🔐 其他端點目前允許匿名存取（可根據需求調整）

## 環境配置

### 開發環境 (`dev`)

- H2 Console: ✅ **啟用**
- 詳細日誌: ✅ 啟用
- SQL 顯示: ✅ 啟用
- 內存資料庫: ✅ 使用

### 生產環境 (`prod`)

- H2 Console: ❌ **完全停用**
- 詳細日誌: ❌ 停用
- SQL 顯示: ❌ 停用
- 資料庫: 💾 檔案型或外部資料庫

### 切換環境

**開發模式**：

```bash
./mvnw spring-boot:run -Dspring.profiles.active=dev
```

**生產模式**：

```bash
./mvnw spring-boot:run -Dspring.profiles.active=prod
```

## 開發注意事項

1. **環境安全**：

   - ✅ H2 Console 已設定為僅開發模式可用
   - ✅ 生產環境會自動拒絕 H2 Console 存取
   - ✅ 不同環境使用不同的日誌級別和資料庫配置

2. **生產環境部署**：

   - 更換為正式資料庫（MySQL、PostgreSQL 等）
   - 更新安全配置
   - 加入 JWT 或 Session 管理
   - 更嚴格的 API 權限控制
   - 確保使用 `spring.profiles.active=prod`

3. **安全最佳實務**：
   - 密碼政策可以根據需求進一步加強
   - 建議在生產環境使用 HTTPS
   - 定期更新密碼加密強度

## 授權

此專案僅供學習和示範使用。
