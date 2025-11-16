# 002-ADR-ADR 清單列表

- 2025/11/02 (架構師) - 初始版本

## 基本資訊 (Basic Information)

- 狀態 (Status): 已採納 (Accepted)
- 決策日期 (Decision Date): 2025/11/02
- 決策者 (Deciders): 架構師、開發團隊
- 相關決策 (Related Decisions):
- 背景 (Context):
  1. 為了提升 ADR 文件的可讀性與一致性，需建立一份選單、術語和縮寫清單

## 決策內容 (Decision)

1. 縮寫格式：

   - 選擇順序：IT 業界慣例 -> 專有術語縮寫 -> 公司內術語 -> 音節縮寫(遵守英文縮寫慣例) -> 自訂縮寫

2. 格式統一:

```
1. {標題}
  - {用途說明}
  - {區塊描述}
    - {中文說明}: {完整名稱} ({縮寫}) - {說明}
```

## 列表

1. ADR 文件類別縮寫:

   - 用於 標示 ADR 文件的分類與主題，**縮寫必須具有唯一性**
   - 使用中

     - 架構決策記錄: Architecture Decision Record (ADR) - ARD 文件相關決策
     - 架構: Architecture (Arch) - 通用系統架構相關決策

   - 以下是建議，如有使用再加入
     - 六邊形架構: Hexagonal Architecture (Hex) - 通用六邊形架構相關決策
     - 三層式架構: Three-Tier Architecture (3TA) - 通用三層式架構相關決策
     - 應用程式介面: Application Programming Interface (API) - 通用 API 相關決策
     - 資料庫: Database (DB) - 通用資料庫相關決策
     - 安全性: Security (Sec) - 通用安全性相關決策
     - 性能: Performance (Perf) - 通用性能相關決策
     - 測試: Testing (Test) - 通用測試相關決策
     - 部署: Deployment (Dep) - 通用部署相關決策
     - 開發流程: Development Process (DP) - 通用開發流程相關決策
     - 監控: Monitoring (Mon) - 通用監控相關決策
     - 日誌: Logging (Log) - 通用日誌相關決策
     - 錯誤處理: Error Handling (Err) - 通用錯誤處理相關決策
     - 緩存: Caching (Cache) - 通用緩存相關決策
     - 訊息佇列: Message Queue (MQ) - 通用訊息佇列相關決策
     - 持續整合: Continuous Integration (CI) - 通用持續整合相關決策
     - 持續部署: Continuous Deployment (CD) - 通用持續部署相關決策
     - 版本控制: Version Control (VC) - 通用版本控制相關決策
     - 使用者介面: User Interface (UI) - 通用使用者介面相關決策
     - 使用者體驗: User Experience (UX) - 通用使用者體驗相關決策
     - 國際化: Internationalization (I18N) - 通用國際化相關決策
     - 本地化: Localization (L10N) - 通用本地化相關決策
     - 備份與還原: Backup and Recovery (BR) - 通用備份與還原相關決策
     - 風格指南: Style Guide (SG) - 通用程式碼風格指南相關決策

2. 文件使用到的縮寫列表

- 待補充
