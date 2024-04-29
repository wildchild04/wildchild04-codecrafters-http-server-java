package github.com.wildchild04.http.routing;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.Map;

public class RouterPathTest {

    @ParameterizedTest
    @ArgumentsSource(Providers.MatchPathArgs.class)
    void test_matchPath(String path, String input, boolean expectedMatch, Map<String, String> expectedValues) {
        RouterPath routerPath = new RouterPath(path);
        var match = routerPath.matchPath(input);
        Assertions.assertEquals(expectedMatch, match.first(), String.format("match should be equals, path:%s input:%s", path, input));
        Assertions.assertEquals(expectedValues, match.second(), String.format("Map with values should match, path:%s input:%s", path, input));
    }
}


