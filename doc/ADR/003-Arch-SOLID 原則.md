# 003-Arch-SOLID 原則

- 2025/11/09 (架構師) - 初始版本

## 基本資訊 (Basic Information)

- 規範性質 (Nature): 建議性 (Recommendation)
- 狀態 (Status):
- 決策日期 (Decision Date):
- 決策者 (Deciders):
- 相關決策 (Related Decisions):
  1. [001-ADR](./001-ADR-ADR文件規範.md): ADR 文件規範
- 背景 (Context):
  1. 為確保程式碼品質與可維護性，需建立一套明確的物件導向設計原則
  2. SOLID 原則是業界公認的物件導向設計最佳實踐，有助於提升系統的可擴展性與可測試性
  3. 團隊使用 AI 工具輔助開發，明確的 SOLID 原則規範有助於 AI 生成符合設計原則的程式碼

## 決策內容 (Decision)

### 1. 單一職責原則 (Single Responsibility Principle, SRP)

- 定義: 一個類別應該只有一個引起它變化的原因，即一個類別只負責一項職責。

- 範例 (Example)

  - 不符合 SRP 的範例:

  ```java
  class UserService {
  		/**  創建使用者邏輯 */
  		public void createUser(User user) { }
  		/** 發送歡迎郵件邏輯 */
  		public void sendWelcomeEmail(User user) { }
  }
  ```

  - 符合 SRP 的範例:

  ```java
  class CreateUserService {
  		public void create(User user) {}
  }
  class SendWelcomeEmailService {
  		public void send(User user) {}
  }
  ```

### 2. 開放封閉原則 (Open-Closed Principle, OCP)

- 定義: 軟體實體（類別、模組、函式等）應該對擴展開放，對修改封閉。

- 範例 (Example)

  - 不符合 OCP 的範例:

  ```java
  class PasswordEncoder {
    public String encode(String password, String type) {
      if (type.equals("BCrypt")) {
        return encodeBCrypt(password);
      } else if (type.equals("SHA256")) {
        return encodeSHA256(password);
      }
      // 每次新增加密方式都需要修改此類別
      return password;
    }
  }
  ```

  - 符合 OCP 的範例:

  ```java
  interface PasswordEncoder {
    String encode(String password);
  }

  class BCryptPasswordEncoder implements PasswordEncoder {
    public String encode(String password) {
      return encodeBCrypt(password);
    }
  }

  class SHA256PasswordEncoder implements PasswordEncoder {
    public String encode(String password) {
      return encodeSHA256(password);
    }
  }
  // 新增加密方式只需新增類別，不需修改既有程式碼
  ```

### 3. 里氏替換原則 (Liskov Substitution Principle, LSP)

- 定義: 子類別必須能夠替換其父類別，且不影響程式的正確性。

- 範例 (Example)

  - 不符合 LSP 的範例:

    ```java
    class UserService {
        public void updateEmail(User user, String newEmail) {
            user.setEmail(newEmail);
            sendVerificationEmail(user);
        }

        protected void sendVerificationEmail(User user) {
            // 發送驗證郵件
        }
    }

    // 違反 LSP:子類別改變了父類別的行為
    class GuestUserService extends UserService {
        @Override
        protected void sendVerificationEmail(User user) {
            throw new UnsupportedOperationException("訪客不支援郵件驗證");
        }
    }
    ```

    - 符合 LSP 的範例:

    ```java
    interface UserService {
        void updateEmail(User user, String newEmail);
    }

    @Service
    class RegisteredUserService implements UserService {
        public void updateEmail(User user, String newEmail) {
            user.setEmail(newEmail);
            sendVerificationEmail(user);
        }
    }

    @Service
    class GuestUserService implements UserService {
        public void updateEmail(User user, String newEmail) {
            // 訪客用戶直接更新,不發送驗證郵件
            user.setEmail(newEmail);
        }
    }
    // 兩個實作都遵守相同契約,可安全替換
    ```

### 4. 介面隔離原則 (Interface Segregation Principle, ISP)

- 定義: 客戶端不應該被迫依賴它不使用的方法,應該將大介面拆分成更小、更具體的介面。

- 範例 (Example)

  - 不符合 ISP 的範例:

  ```java
  // 臃腫的介面強迫所有實作類別依賴不需要的方法
  interface UserService {
    void register(User user);
    void login(String username, String password);
    void sendWelcomeEmail(User user);
    void generateUserReport(Long id);
  }

  // 簡單的登入服務被迫實作不需要的方法
  @Service
  class LoginService implements UserService {
    public void register(User user) {
      throw new UnsupportedOperationException();
    }
    public void login(String username, String password) {
      // 實際登入邏輯
    }
    public void sendWelcomeEmail(User user) {
      throw new UnsupportedOperationException();
    }
    public void generateUserReport(Long id) {
      throw new UnsupportedOperationException();
    }
  }
  ```

  - 符合 ISP 的範例:

  ```java
  // 將大介面拆分成職責明確的小介面
  interface AuthenticationService {
    void login(String username, String password);
  }

  interface RegistrationService {
    void register(User user);
  }

  interface NotificationService {
    void sendWelcomeEmail(User user);
  }

  // 登入服務只實作需要的介面
  @Service
  class LoginService implements AuthenticationService {
    public void login(String username, String password) {
      // 登入邏輯
    }
  }

  // 註冊服務組合需要的介面
  @Service
  class UserRegistrationService implements RegistrationService {
    @Autowired
    private NotificationService notificationService;

    public void register(User user) {
      // 註冊邏輯
      notificationService.sendWelcomeEmail(user);
    }
  }
  ```

### 5. 依賴反轉原則 (Dependency Inversion Principle, DIP)

- 定義: 高層模組不應該依賴低層模組，兩者都應該依賴抽象；抽象不應該依賴細節，細節應該依賴抽象。

- 範例 (Example)

  - 不符合 DIP 的範例:

  ```java
  class MySQLUserRepository {
    public User findById(Long id) {
      // MySQL 資料庫查詢邏輯
      return new User();
    }
  }

  @Service
  class UserService {
    // 直接依賴具體實作類別
    private MySQLUserRepository userRepository;

    public UserService() {
      this.userRepository = new MySQLUserRepository();
    }

    public User getUser(Long id) {
      return userRepository.findById(id);
    }
  }
  // 如果要切換到 PostgreSQL，需要修改 UserService
  ```

  - 符合 DIP 的範例:

  ```java
  // 定義抽象介面
  interface UserRepository {
    User findById(Long id);
  }

  // 具體實作依賴抽象
  @Repository
  class MySQLUserRepository implements UserRepository {
    public User findById(Long id) {
      // MySQL 資料庫查詢邏輯
      return new User();
    }
  }

  @Repository
  class PostgreSQLUserRepository implements UserRepository {
    public User findById(Long id) {
      // PostgreSQL 資料庫查詢邏輯
      return new User();
    }
  }

  // 高層模組依賴抽象介面
  @Service
  class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
      this.userRepository = userRepository;
    }

    public User getUser(Long id) {
      return userRepository.findById(id);
    }
  }
  // 可透過 Spring 配置切換不同的 Repository 實作，無需修改 UserService
  ```
