import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) {
        int port = 8080;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is listening on port " + port);
            ExecutorService executorService = Executors.newFixedThreadPool(5);

            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(new ClientHandler(socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        public static Map<String, String> stringMap = new HashMap<>();
        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

                String line;
                String params = "";
                String path = "";
                String requestMethod = "";
                String requestBody = "";
                StringBuilder body = new StringBuilder();
                StringBuilder head = new StringBuilder();
                String url = "";
                String resp = "";
                while ((line = in.readLine()) != null) {
                    head.append("\r\n").append(line); // Build the HTTP headers
                    if (line.isEmpty()) {
                        break;
                    }
                    if (requestMethod.isEmpty()) {
                        requestMethod = line.split(" ")[0];
                        if (line.contains("?")) {
                            String value = getParameterValue(line, "url");
                            url = value;
                            System.out.println("URL參數值: " + value);
                            params = line.split("\\?")[1];
                            params = params.split(" ")[0];
                            path = line.split("\\?")[0];
                            path = path.split(" ")[1];
                        }
                        else {
                            path = line.split(" ")[1];
                            //path = path.split("/")[1];
                        }
                    }
                }

                if (requestMethod.equals("POST")) {
                    while (in.ready()) {
                        body.append((char) in.read());
                    }
                    requestBody = body.toString();
                }

                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
                String currentTime = dateFormat.format(new Date());

                out.println("HTTP/1.0 200 OK");
                out.println("Content-Type: text/html");
                out.println("Connection: Keep-Alive");
                //out.println("Connection: close");
                out.println("Timestamp: " + currentTime);
                out.println();
                params = params.replace("&", "\r\n");
                if (requestMethod.equals("GET")) {
                    if (stringMap.containsKey(url))
                    {
                        System.out.println("use cache");
                        resp = stringMap.get(url);
                    }
                    else {
                        System.out.println("get page");
                        resp = get_http(url);
                        stringMap.put(url, resp);
                    }
                    out.println(resp);
                }
                else {
                    out.println("<html><body><h1>Method not allowed!</h1></body></html>");
                }

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
        private static String getParameterValue(String url, String parameterName) {
            // 找到 "?" 的位置
            int questionMarkIndex = url.indexOf("?");

            // 如果沒有問號或者參數名不存在，返回 null
            if (questionMarkIndex == -1 || !url.contains(parameterName + "=")) {
                return null;
            }

            // 截取 "?" 之後的字符串
            String queryString = url.substring(questionMarkIndex + 1);

            // 將查詢字符串分割成鍵值對
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                // 分割鍵值對
                String[] keyValue = pair.split("=");

                // 檢查參數名是否匹配
                if (keyValue.length == 2 && keyValue[0].equals(parameterName)) {
                    // 返回參數值
                    return keyValue[1];
                }
            }

            // 如果參數不存在，返回 null
            return null;
        }

        public static String get_http(String url) {
            // 使用者輸入的網址
            //String url = "http://httpbin.org/";

            try {
                String response = sendGetRequest(url);
                return response;
            } catch (IOException e) {
                return "error";
            }
        }

        private static String getUserInput(String prompt) {
            System.out.print(prompt);
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                return reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException("讀取使用者輸入時發生錯誤: " + e.getMessage());
            }
        }

        private static String sendGetRequest(String url) throws IOException {
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

            // 設定請求類型為GET
            connection.setRequestMethod("GET");

            // 取得回應碼
            int responseCode = connection.getResponseCode();

            // 讀取回應內容
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = reader.readLine()) != null) {
                response.append(inputLine);
            }
            reader.close();

            return response.toString();
        }
    }
}