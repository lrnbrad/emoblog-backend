# EmoBlog Backend

於「應用軟體設計實習」課程中使用 Spring Boot 設計 Blog 後端，透過 Spring Data JPA、Spring Security 與 OpenAPI 整合資料庫存取、使用者驗證與 API 文件端點，提供使用者註冊 / 登入、個人資料管理、貼文、留言與按讚等功能。專案目前預設使用 SQLite 作為資料庫，並用 JWT 保護除了登入、註冊與 API 文件以外的所有 API。

這個 repo 只包含後端服務，不包含前端頁面、網域設定或反向代理設定。

## 功能概覽

- JWT 註冊與登入
- 使用者個人資料查詢與更新
- 貼文建立、查詢、修改、刪除
- 留言與按讚功能
- Swagger UI / OpenAPI 文件
- SQLite 單檔資料庫，啟動時自動更新 schema

## 技術棧

- Java 17
- Kotlin 2.2
- Spring Boot 4
- Spring Security
- Spring Data JPA + Hibernate
- SQLite
- springdoc-openapi
- Docker / Docker Compose

## 執行需求

- JDK 17
- Docker 與 Docker Compose（如果使用容器方式啟動）
- 專案根目錄下可寫入的 `db/` 目錄

## 設定方式

專案主要透過環境變數配置：

| 變數 | 預設值 | 說明 |
| --- | --- | --- |
| `SECURITY_JWT_SECRET` | 內建開發用 secret | JWT 簽章金鑰，必須是 Base64 編碼後的 32-byte secret。正式環境一定要覆蓋。 |
| `SECURITY_JWT_EXPMINUTES` | `60` | JWT 過期時間，單位是分鐘。 |
| `SPRING_DATASOURCE_URL` | `jdbc:sqlite:db/emoblog.db` | SQLite 資料庫路徑。可改成其他檔案位置。 |
| `SPRING_DATASOURCE_PASSWORD` | `password` | JDBC 密碼欄位。對 SQLite 不是主要防護手段，實際保護仍依賴檔案權限。 |

可直接使用範本檔：

```bash
cp .env.example .env
```

`SECURITY_JWT_SECRET` 可以用下列指令產生：

```bash
openssl rand -base64 32
```

注意：

- 不要把正式環境的 JWT secret 提交到 git。
- `db/emoblog.db` 不存在時，第一次啟動會自動建立。
- 專案目前使用 `spring.jpa.hibernate.ddl-auto=update`，適合開發與小型部署，不等於正式 migration 流程。

## 本機啟動

1. 建立設定檔並填入實際值：

   ```bash
   cp .env.example .env
   ```

2. 載入環境變數：

   ```bash
   set -a
   source .env
   set +a
   ```

3. 建立資料夾：

   ```bash
   mkdir -p db
   ```

4. 啟動服務：

   ```bash
   ./gradlew bootRun
   ```

   Windows:

   ```powershell
   .\gradlew.bat bootRun
   ```

5. 啟動後可檢查：

   - Swagger UI: `http://localhost:8080/swagger-ui/index.html`
   - OpenAPI JSON: `http://localhost:8080/v3/api-docs`
   - Health Check: `http://localhost:8080/actuator/health`

## 使用 Docker Compose 啟動

1. 建立 `.env`：

   ```bash
   cp .env.example .env
   ```

2. 建立資料夾：

   ```bash
   mkdir -p db
   ```

3. 啟動：

   ```bash
   docker compose up --build -d
   ```

4. 查看日誌：

   ```bash
   docker compose logs -f app
   ```

5. 停止服務：

   ```bash
   docker compose down
   ```

說明：

- 如果你的環境還在使用舊版 CLI，請把 `docker compose` 替換成 `docker-compose`。
- Compose 會把本機的 `./db` 掛載到容器內的 `/app/db`。
- 實際資料庫檔案會保存在 `db/emoblog.db`。
- 如果要備份資料，直接備份 `db/` 目錄即可。

## API 文件與驗證

公開端點：

- `POST /auth/register`
- `POST /auth/login`
- `GET /actuator/health`
- `/swagger-ui/**`
- `/v3/api-docs/**`

其餘 API 都需要 JWT，請在 request header 帶上：

```http
Authorization: Bearer <token>
```

Swagger UI 也可以直接點右上角 `Authorize` 後貼上 token。

### 註冊範例

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "firstName": "Jane",
    "lastName": "Doe",
    "password": "StrongP@ss1",
    "birthday": "2000-01-01"
  }'
```

### 登入範例

```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user1",
    "password": "StrongP@ss1"
  }'
```

### 帶 token 呼叫受保護 API

```bash
curl http://localhost:8080/posts/feed \
  -H "Authorization: Bearer <token>"
```

## 常用開發指令

```bash
./gradlew test
./gradlew bootRun
./gradlew bootJar
```

## 部署注意事項

- 這個專案目前比較適合單機部署；若你要多實例、高併發或多人同時寫入，建議改用 PostgreSQL / MySQL 等伺服器型資料庫。
- 專案目前沒有額外的 CORS 設定。如果前端與 API 分不同網域或不同 port 部署，通常還要再補 `CorsConfigurationSource` 或對應設定。
- 正式環境建議把 Spring Boot 放在反向代理後面，例如 Nginx 或 Caddy，並只對外暴露代理層。
- 請確保 `db/` 目錄的備份與檔案權限有被管理好，因為 SQLite 的資料保護主要靠檔案系統。
