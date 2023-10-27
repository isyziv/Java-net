import java.io.*;
import java.net.Socket;
import java.util.Base64;

public class MailSender {
    public static void sender(String host,int port,String email_address, String password ,String send_email, String recipient_email ,String subject , String content)
    {
        try {
            // Connect to the mail server
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Receive server's welcoming message
            System.out.println(reader.readLine());

            // Send EHLO and receive the response
            sendCommand(writer, "EHLO Alice\r\n");
            receiveServerResponse(reader);

            // Send STARTTLS and set the socket to SSL
            sendCommand(writer, "STARTTLS\r\n");
            receiveServerResponse(reader);

            // Upgrade the socket to SSL and recreate streams
            socket = SSLSocketFactoryProvider.getSSLSocketFactory().createSocket(
                    socket, socket.getInetAddress().getHostAddress(), socket.getPort(), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            // Send EHLO again
            sendCommand(writer, "EHLO Alice\r\n");
            receiveServerResponse(reader);

            // Encode and send user credentials
            String auth = email_address + "\0" + email_address + "\0" + password;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            sendCommand(writer, "AUTH PLAIN " + encodedAuth + "\r\n");
            receiveServerResponse(reader);

            // MAIL FROM, RCPT TO, and DATA commands
            sendCommand(writer, "MAIL FROM:<" + send_email + ">\r\n");
            receiveServerResponse(reader);

            sendCommand(writer, "RCPT TO:<" + recipient_email + ">\r\n");
            receiveServerResponse(reader);

            sendCommand(writer, "DATA\r\n");
            receiveServerResponse(reader);

            // Sending email data
            String emailData = "Subject: " + subject + "\r\n" +
                    "From: " + send_email+ "\r\n" +
                    "To: " + recipient_email + "\r\n" +
                    "MIME-Version: 1.0\r\n" +
                    "Content-Type: text/plain\r\n\r\n" +
                    content+"\r\n.\r\n";
            sendCommand(writer, emailData);
            receiveServerResponse(reader);

            // QUIT
            sendCommand(writer, "QUIT\r\n");
            receiveServerResponse(reader);

            // Close the connection
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static void sendCommand(BufferedWriter writer, String command) throws IOException {
        writer.write(command);
        writer.flush();
    }

    private static void receiveServerResponse(BufferedReader reader) throws IOException {
        String line;
        do {
            line = reader.readLine();
            System.out.println(line);
        } while (line != null && line.charAt(3) == '-');
    }
}

class SSLSocketFactoryProvider {
    // This class provides SSL Socket Factory. It's useful in the case of STARTTLS.
    static javax.net.ssl.SSLSocketFactory getSSLSocketFactory() {
        return (javax.net.ssl.SSLSocketFactory) javax.net.ssl.SSLSocketFactory.getDefault();
    }
}
