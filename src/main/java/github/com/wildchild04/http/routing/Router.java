package github.com.wildchild04.http.routing;

import github.com.wildchild04.http.Status;
import github.com.wildchild04.http.Version;
import github.com.wildchild04.http.message.Request;
import github.com.wildchild04.http.message.Response;
import github.com.wildchild04.http.utils.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Router {

    private final Map<RouterPath, Handler> handlers;

    public Router() {
        this.handlers = new HashMap<>();
    }

    public Pair<Map<String, String>, Handler> getHandler(String path) {
        Handler handler = null;
        Map<String, String> values = null;
        for (Map.Entry<RouterPath, Handler> entry : handlers.entrySet()) {
            var matchRes = entry.getKey().matchPath(path);
            if (matchRes.first()) {
                values = matchRes.second();
                handler = entry.getValue();
            }
        }


        if (handler == null) {
            return new Pair<>(Collections.emptyMap(), new Handler() {
                @Override
                public Response handle(Request request) {
                    return new Response(Version.HTTP11, Status.NOT_FOUND, Collections.emptyMap(), new byte[]{});
                }
            });
        }
        return new Pair<>(values, handler);
    }

    public void registerHandler(String path, Handler handler) {
        handlers.put(new RouterPath(path), handler);
    }
}
