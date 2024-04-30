package github.com.wildchild04.http;

public enum ServerConfig {

    DIR("--directory");

    private final String arg;

    ServerConfig(String s) {
        arg = s;
    }

    public static ServerConfig getFromArg(String arg) {
        for(ServerConfig sc : ServerConfig.values()) {
            if (sc.arg.equals(arg)) {
                return sc;
            }
        }
        return null;
    }
}
