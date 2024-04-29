package github.com.wildchild04.http.message;

import github.com.wildchild04.http.Method;
import github.com.wildchild04.http.Version;

import java.util.Map;

public record Request(Method method, Version version, Map<String, String> headers, Map<String, String> values ,byte[] body) {


}
