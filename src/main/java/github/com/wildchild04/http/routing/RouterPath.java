package github.com.wildchild04.http.routing;

import github.com.wildchild04.http.utils.Pair;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record RouterPath(String path) {

    private static final Pair<Boolean, Map<String ,String>> nope = new Pair<>(false, null);
    public Pair<Boolean, Map<String,String>> matchPath(String somePath) {
        var someSplit = somePath.split("/");
        var pathSplit = path.split("/");

        if (someSplit.length != pathSplit.length) {
            return nope;
        }
        var pathValues = new HashMap<String, String>();
        for(int i=1; i<someSplit.length; i++) {
                var vals = extractPathValue(someSplit[i],pathSplit[i]);
                if (vals != null) {
                    pathValues.put(vals[0],vals[1]);
                    continue;
                }

                if (!pathSplit[i].equals(someSplit[i])) {
                    return nope;
                }
        }

        return new Pair<>(true, pathValues);
    }

    private String[] extractPathValue(String path, String pathWithValue) {
        if (!isPathWithValue(pathWithValue)) {
            return null;
        }
        return new String[]{pathWithValue.substring(1, pathWithValue.length()-1), path};
    }

    private boolean isPathWithValue(String pathWithValue) {
        return pathWithValue.startsWith("{") && pathWithValue.endsWith("}");
    }
}
