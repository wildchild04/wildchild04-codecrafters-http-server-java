package github.com.wildchild04.http.routing;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Providers {
    public static class MatchPathArgs implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of("/test/{val}/pog", "/test/123/pog", true, Map.of("val", "123")),
                    Arguments.of("/test", "test", false, null),
                    Arguments.of("/test", "/test", true, new HashMap<String, String>()),
                    Arguments.of("/test/{val}/pog/1", "/test", false, null)
            );
        }
    }
}
