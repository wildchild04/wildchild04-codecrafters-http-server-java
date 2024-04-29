package github.com.wildchild04.http.routing;

import github.com.wildchild04.http.message.Request;
import github.com.wildchild04.http.message.Response;

public interface Handler {
    Response handle(Request request);
}
