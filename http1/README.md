# 簡單的Java HTTP伺服器

這是一個簡單的Java HTTP伺服器，監聽在連接埠8080上，用於回應傳入的HTTP請求。 它可以處理GET和POST請求，並提供基本的HTML回應。

## 功能

- 在連接埠8080上監聽傳入連線。
- 處理GET、POST和HEAD HTTP請求。
- 解析並列印從客戶端接收的HTTP頭。
- 產生一個簡單的HTML回應。
- 在回應頭中傳回目前時間戳記。

## 程式碼結構

- 伺服器的主入口點是`Main`類別。
- 它建立一個`ServerSocket`來監聽連接埠8080並接受傳入連線。
- 對於每個傳入連接，都會建立一個新的`ClientHandler`執行緒來處理請求。
- `ClientHandler`類別讀取和處理HTTP請求，包括頭部和請求體。
- 它產生一個HTML回應並將其發送回客戶端。