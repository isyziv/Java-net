以下是對你的程式碼的解讀及相關的說明：

```plaintext
# 程式碼解讀及說明

## 1. 主程式 (Main class)

```java
public static void main(String[] args)
```
- 這是程式的進入點，主要功能是建立一個伺服器套接字 (ServerSocket) 並等待客戶端連接。
- 使用 ExecutorService 來管理執行緒池，每當有新的客戶端連接，就會提交一個執行緒給執行緒池處理。

```java
try (ServerSocket serverSocket = new ServerSocket(port)) {
    // ...
    while (true) {
        Socket socket = serverSocket.accept();
        executorService.execute(new ClientHandler(socket));
    }
} catch (IOException e) {
    e.printStackTrace();
}
```
- 建立 ServerSocket 並指定監聽的埠號 (port)。
- 使用 try-with-resources 確保 ServerSocket 在使用完畢後能夠正確關閉。
- 進入無窮迴圈，等待客戶端連接，每當有新的連接，就創建一個 `ClientHandler` 實例，交由執行緒池處理。

## 2. 客戶端處理 (ClientHandler class)

```java
static class ClientHandler implements Runnable {
    // ...
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
            // ...  // 客戶端處理邏輯
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // ...
}
```
- `ClientHandler` 實現了 Runnable 介面，負責處理每個客戶端的請求。
- 使用 BufferedReader 讀取客戶端的輸入流，並使用 PrintWriter 寫入回應。
- 解析 HTTP 請求中的方法、參數等資訊，並根據 `SendRequest` 方法向指定的 URL 發送請求。
- 最後，確保關閉與客戶端的連接。

```java
public static String SendRequest(String url, String requestMethod) throws IOException {
    // ...  // 向指定 URL 發送請求的邏輯
}
```
- `SendRequest` 方法用於向指定 URL 發送 HTTP 請求，並讀取回應內容。