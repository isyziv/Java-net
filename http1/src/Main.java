import java.io.*;
import java.net.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

                while ((line = in.readLine()) != null) {
                    head.append("\r\n").append(line); // Build the HTTP headers
                    if (line.isEmpty()) {
                        break;
                    }
                    if (requestMethod.isEmpty()) {
                        requestMethod = line.split(" ")[0];
                        if (line.contains("?")) {
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
                if (requestMethod.equals("GET") || requestMethod.equals("POST")) {
                    out.println("<html><body><h1>Hello, World!</h1></body>");
                    if (!path.equals("/")) {
                        out.println("<p>Unkown path: " + path + "</p>");
                    }
                    System.out.println("params" + params);
                    if (!params.equals("")){
                        out.println("<p>params: <br><pre>" + params + "</pre></p>");
                    }
                    out.println("<p>Request Head: <br><pre>" + head + "</pre></p>");
                    if (!requestBody.isEmpty()) {
                        out.println("<p>Request Body: <br><pre>" + requestBody + "</pre></p>");
                    }
                    out.println("</html>");
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
    }
}