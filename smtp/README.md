## README.md

### 概述

所提供的Java程式碼包含兩個類別：
1. `MailSender` - 用於與電子郵件伺服器建立連接並通過它發送電子郵件。
2. `MainUI` - 利用Swing創建一個基本的GUI，允許用戶輸入電子郵件詳細信息並利用`MailSender`發送電子郵件。

### 詳細分析

#### 1. `MailSender` 類別

##### 功能：

- **建立連接：** 使用Socket連接到郵件伺服器，並通過輸入/輸出流與之交互。
- **SSL安全連接：** 在發送`STARTTLS`命令後將連接升級為SSL/TLS，以進行安全的數據傳輸。
- **身份驗證：** 使用`AUTH PLAIN`和Base64編碼的憑據向郵件伺服器進行身份驗證。
- **電子郵件傳輸：** 通過SMTP協議發送電子郵件，利用`MAIL FROM`、`RCPT TO`和`DATA`等命令。
- **退出：** 最後，發送`QUIT`命令並關閉連接。

##### Method：

- `sender(...)`: 這是一個主要方法，接受伺服器詳細信息、發件人電子郵件、收件人電子郵件、主題和內容等參數，並執行電子郵件傳輸。
- `sendCommand(...)`: 用於向伺服器發送命令並確保它寫入的方法。
- `receiveServerResponse(...)`: 用於從伺服器讀取響應並將其打印到控制台的方法。

#### 2. `SSLSocketFactoryProvider` 類別

這個類別包含一個單獨的靜態方法，該方法返回默認`SSLSocketFactory`的實例，使應用程序能夠創建SSL套接字，主要用於在`MailSender`中的`STARTTLS`命令後升級現有套接字為SSL。

#### 3. `MainUI` 類別

##### 功能：

- **GUI：** 為用戶提供一個基於Swing的GUI，用於輸入SMTP伺服器的IP地址和端口、發件人的電子郵件、憑據、收件人的電子郵件、主題和內容。
- **觸發郵件：** 在點擊“發送”按鈕後，獲取輸入的詳細信息，並使用`MailSender`的實例發送電子郵件。
- **成功對話框：** 發送郵件後，它將顯示指示成功的對話框。
- **退出：** "Quit"按鈕允許用戶關閉應用程序。

##### 組件：

- `JTextField` 組件用於輸入文字詳細信息。
- `JTextArea` 用於輸入電子郵件內容。
- `JPasswordField` 安全地輸入密碼。
- `JButton` 組件執行操作（發送電子郵件或退出）。

---

這個README提供了代碼庫的全面概述、使用指南和未來的範疇。根據具體用例或開發更新，可能需要進行調整和添加。