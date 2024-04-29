import github.com.wildchild04.http.*;
import github.com.wildchild04.http.message.Request;
import github.com.wildchild04.http.message.Response;
import github.com.wildchild04.http.routing.Handler;
import github.com.wildchild04.http.routing.Router;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");
        try {
            Router router = new Router();
            router.registerHandler("/echo/{string}", request -> {
                String body = request.values().get("string");
                Map<String, String> headers = Map.of(
                        "Content-Type", "text/plain",
                        "Content-Length", Integer.toString(body.length())
                );
                return new Response(Version.HTTP11, Status.OK, headers, body.getBytes());
            });
            router.registerHandler("/", new Handler() {
                @Override
                public Response handle(Request request) {
                    return new Response(Version.HTTP11, Status.OK, Collections.emptyMap(), new byte[]{});
                }
            });

            Server server = new Server(4221, router);
            server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
