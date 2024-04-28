import github.com.wildchild04.http.message.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static github.com.wildchild04.http.message.HttpEncoder.encodeString;

public class App {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        // Uncomment this block to pass the first stage
        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);
            clientSocket = serverSocket.accept(); // Wait for connection from client.
            System.out.println("accepted new connection");
            var input = clientSocket.getInputStream();
            var incomingData = input.readNBytes(input.available());
            var dataStrings = new String(incomingData).split("\\r\\n");
            var requestInfo = dataStrings[0];
            var requestInfoStrings = requestInfo.split(" ");
            Message message;
            if (requestInfoStrings[1].equals("/")) {
                message = new Message(encodeString("HTTP/1.1 200 OK"), encodeString(""), new byte[]{});
            } else {
                message = new Message(encodeString("HTTP/1.1 404 OK"), encodeString(""), new byte[]{});
            }
            clientSocket.getOutputStream().write(message.toBytes());
            clientSocket.getOutputStream().flush();
            clientSocket.getOutputStream().close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
