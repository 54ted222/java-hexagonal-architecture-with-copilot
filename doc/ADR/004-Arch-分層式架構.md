# 004-Arch-分層式架構

- 2025/11/09 (架構師) - 初始版本

## 基本資訊 (Basic Information)

- 規範性質 (Nature): 強制性 (Mandatory)
- 狀態 (Status):
- 決策日期 (Decision Date):
- 決策者 (Deciders):
- 相關決策 (Related Decisions):
  1. [001-ADR](./001-ADR-ADR文件規範.md): ADR 文件規範
  2. [003-Arch](./003-Arch-SOLID%20原則.md): SOLID 原則
- 背景 (Context):
  1. 專案需要明確的架構分層規範，確保程式碼組織清晰，便於團隊協作與維護
  2. 明確的分層規範有助於 AI 工具生成符合架構要求的程式碼

## 決策內容 (Decision)

### Control Layer (控制層):

- 依賴關係: 依賴 Application Layer (應用層)

- 職責：

  - 最主要職責：負責提供應用程式的觸發；負責把外部世界的輸入轉成 Application 可以理解的 Command / Query，並將 Application 回傳的結果轉成外部世界可以理解的格式

  1. 處理通訊協議的請求與回應
  2. 驗證輸入資料 (確保只允許合法資料進入系統，ex:通訊格式結構驗證、權限驗證等)
  3. 將通訊協議 DTO 轉換為 應用層使用的 DTO (DTO Mapper)
  4. 呼叫應用層進行業務邏輯處理
  5. 提供通用錯誤處理機制
  6. 將應用層回傳的結果封裝成通訊協議 DTO (DTO Mapper)
  7. 回應給觸發來源（客戶端/OS）

- 類型：

  1. Presentation (用戶介面): REST API、GraphQL API、GRPC API、GUI、CLI
  2. Schedule (排程): 定時任務觸發
  3. Consumer (消費者): 消息隊列觸發
  4. Event Trigger (事件觸發): 事件驅動觸發
  5. Startup (主程式入口): 一次性執行啟動入口

### Application Layer (應用層)

- 依賴關係: 依賴 Application Layer 定義的 Port 介面，實務上也會引用到 Domain Layer 的模型作為資料結構
- 職責：

  - 最主要職責：提供核心服務的入口，處理核心服務的邏輯與流程控制

  1. 驗證輸入資料 (針對資料驗證，如: 欄位長度、格式、必填值等)
  2. 驗證業務規則 (確保業務邏輯的正確性)
  3. 處理交易管理 (使用抽象介面進行交易控制)
  4. 定義並呼叫資料存取相關的 Port 介面，將實際的資料存取細節隔離在 Infrastructure Layer（六邊形架構）
  5. 呼叫領域模型（充血模型）或組合領域服務，實現業務規則與政策；或在貧血模型風格下由 Application Service 實作業務邏輯
  6. 回傳輸出解果（Result DTO / View Model）

- 類型：

  1. Use Case (用例): 介面定義，提供業務用例的對外入口點
     - Command DTO (命令): 介面定義 (CQRS 架構)
     - Query DTO (查詢): 介面定義 (CQRS 架構)
     - Result DTO (結果): 介面定義
     - Handler (處理器): 接收請求並協調業務邏輯 (CQRS 架構)
     - Port (依賴介面): 介面定義，存取 Infrastructure 層 (六邊形架構)
     - Event DTO (事件): 介面定義，用於表示應用程式發生的重要事件 (事件驅動架構、事件朔源架構)
  2. Service (服務): 實現 Use Case 介面，做流程控制，例如：處理交易管理，呼叫資料存取層，物件轉換

     - Callback Port (回調處理介面): 抽象介面，處理回調邏輯，例如串流處理等
     - Transaction Port (交易管理介面): 抽象介面，定義交易控制方法 (開始、提交、回滾)
     - Repository Port (倉儲介面): 抽象介面，定義資料存取方法 (六邊形架構)\

  3. Business Rules/Policy/Workflow (商業規則 / 政策)：常以無狀態、流程導向的業務邏輯實現

     - 職責：從 Service 抽離「跨 Aggregates、跨資源」的流程性規則，避免 Application Service 過於肥大
     - 範例：訂單建立支付出貨流程處理器、優惠金額計算策略、信用卡結帳日與週期規則、合規性檢查策略等
     - 與 Domain Layer 的關係：
       - 處理單一聚合內的規則：建議放在 Domain Layer（Aggregate / Entity / Value Object / Domain Service）
       - 處理跨聚合 / 跨資源流程：可放在 Application Layer 的 Business Policy
       - 嚴格來說，算是層中層，屬於 Domain Layer 的一部分

### Domain Layer (領域層)

- 依賴關係: 無依賴
- 職責：

  - 最主要職責：定義領域模型，封裝核心業務邏輯與規則

  1. 驗證領域相關的業務規則
  2. 實現業務規則的狀態轉移
  3. 提供領域事件

  - 注意：沒有 Port, Repository 的概念
  - 備註：DDD 架構特有層

- 類型：

  1. Aggregates (聚合): 提供唯一對外操作入口點，跨 Entities 集合操作，交易的最小邊界。
  2. Entities (實體): 具有唯一標識符和生命周期。
  3. Value Objects (值物件): 不可變的物件，表示領域中的概念或屬性集合
  4. Domain Events DTO (領域事件): 領域事件模型，用於表示領域內發生的重要事件，只有業務事件

### Infrastructure Layer (基礎設施層):

- 依賴關係: 依賴 Application Layer (應用層)

- 職責：

  - 最主要職責：處理 Infrastructure 相關事務
  - 備註：除了應用層以外，其他都算是基礎設施層，嚴格來說控制層也算是基礎設施層的一部分，只是將控制層獨立出去。

  1. Adapter 實現應用層定義的 Port 介面
  2. 驗證 Infrastructure 相關的輸入驗證
  3. 實現資料存取的 CRUD 操作，例如: SQL、NoSQL、檔案系統、S3、API、Cache 等
  4. 管理 Infra 設定相關，例如：連線、執行緒、交易細節、資料同步機制、恢復機制 等

- 類型：

  1. Adapter (適配器) / Provider (提供者): 提供基礎設施的操作入口介面
     - 實現 Port 介面，做 Infra 的流程控制
     - 引用 持久/資料存取、閘道層、提供者層、生產者層 的 SDK 或函式庫 等
     - 處理 Port DTO 與 Infrastructure DTO 之間的轉換 (DTO Mapper 工具)
     - 算是層中層
  2. Persistence (持久) / Data Access (資料存取)
     - Repository (倉儲)
       - ORMEntity DTO (實體 DTO)
     - File System (檔案系統)
       - File Dto (檔案 DTO)
     - Gateway (閘道) / External API (外部 API)
       - API Request/Response DTO
     - Producer (生產者) / Broker (訊息代理)
       - Message DTO (訊息 DTO)
     - S3 Storage (S3 儲存)
     - Cache (快取)
       - Cache DTO (快取 DTO)
