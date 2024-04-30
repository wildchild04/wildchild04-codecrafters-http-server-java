package github.com.wildchild04.http;

import github.com.wildchild04.http.message.Request;
import github.com.wildchild04.http.routing.Router;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int THREAD_POOL_SIZE = 50;
    private final ServerSocket serverSocket;
    private final Router router;
    private final ExecutorService executor;

    public Server(int port, Router router) throws IOException {
        serverSocket = new ServerSocket(port);
        executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.router = router;
        serverSocket.setReuseAddress(true);
    }

    public void start() {
        while (true) {
            try {
                System.out.println("accepted new connection");
                var clientSocket = serverSocket.accept(); // Wait for connection from client.
                executor.execute(() -> handleConn(clientSocket));
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private void handleConn(Socket socket) {

        try {
            var input = socket.getInputStream();
            var incomingData = input.readNBytes(input.available());
            var dataStrings = new String(incomingData).split("\\r\\n");
            if (dataStrings.length == 1 && dataStrings[0].isEmpty()) {
                dataStrings = new String[]{"GET / HTTP/1.1"};
            }
            var requestInfo = dataStrings[0];
            var requestInfoStrings = requestInfo.split(" ");
            Method requestMethod;
            try {
                requestMethod = Method.valueOf(requestInfoStrings[0]);
            } catch (Exception e) {
                System.out.printf("Unrecognized method: %s\n", requestInfoStrings[0]);
                requestMethod = Method.GET;
            }
            var headers = new HashMap<String, String>();
            for (int i = 1; i < dataStrings.length; i++) {
                var firstColonIndex = dataStrings[i].indexOf(':');
                if (firstColonIndex == -1) {
                    break;
                }
                var header = new String[]{
                        dataStrings[i].substring(0,firstColonIndex),
                        dataStrings[i].substring(firstColonIndex+1, dataStrings[i].length()) };

                headers.put(header[0].trim(), header[1].trim());
            }
            byte[] body;
            if (requestMethod == Method.POST) {
                body = dataStrings[dataStrings.length-1].getBytes();
            } else {
                body = new byte[]{};
            }
            Request request = new Request(requestMethod, Version.getVersionFromValue(requestInfoStrings[2]), headers, new HashMap<>(), body);

            var handlerValPair = router.getHandler(requestInfoStrings[1]);
            request.values().putAll(handlerValPair.first());
            var response = handlerValPair.second().handle(request);

            var res = response.toBytes();
            System.out.printf("reply with %s\n", new String(res));
            socket.getOutputStream().write(res);
            socket.getOutputStream().flush();
            socket.getOutputStream().close();

        } catch (Exception e) {
            System.err.printf("Error %s", e);
            throw new RuntimeException(e);

        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
