import github.com.wildchild04.http.*;
import github.com.wildchild04.http.message.Request;
import github.com.wildchild04.http.message.Response;
import github.com.wildchild04.http.routing.Handler;
import github.com.wildchild04.http.routing.Router;

import java.io.*;
import java.nio.CharBuffer;
import java.util.*;

public class App {
    public static Map<String, String> CONFIG = new HashMap<>();
    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        if (args.length>= 2) {
            for (int i=0; i< args.length; i++) {
                ServerConfig config = ServerConfig.getFromArg(args[0]);
                if (ServerConfig.DIR == config) {
                    CONFIG.put(ServerConfig.DIR.name(), args[1]);
                }
            }
        }
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
            router.registerHandler("/user-agent", new Handler() {
                @Override
                public Response handle(Request request) {
                    var agent = request.headers().get("User-Agent");
                    var headers = Map.of(
                            "Content-Type", "text/plain",
                            "Content-Length" ,Integer.toString(agent.length())
                    );
                    return new Response(Version.HTTP11, Status.OK, headers, agent.getBytes());
                }
            });

            router.registerHandler("/files/{file-name}", new Handler() {
                @Override
                public Response handle(Request request) {
                    var baseDir = CONFIG.get(ServerConfig.DIR.name());
                    File f = new File(baseDir+"/"+request.values().get("file-name"));
                    if (f.exists()) {
                        try ( FileInputStream fileReader = new FileInputStream(f)){
                            var fileBytes = new byte[(int)f.length()];
                            fileReader.read(fileBytes);
                            var headers = Map.of(
                                    "Content-Type", "application/octet-stream",
                                    "Content-Length" ,Integer.toString(fileBytes.length)
                            );
                            return new Response(Version.HTTP11, Status.OK, headers, fileBytes);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }



                    return new Response(Version.HTTP11, Status.NOT_FOUND, Collections.emptyMap(), new byte[]{});
                }
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
