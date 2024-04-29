package github.com.wildchild04.http;

public enum Version {
    HTTP11("HTTP/1.1");

    private final String value;

    Version(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Version getVersionFromValue(String s) {
        for (Version v: Version.values()) {
            if (v.value.equals(s)) {
                return v;
            }
        }
        return null;
    }
}
