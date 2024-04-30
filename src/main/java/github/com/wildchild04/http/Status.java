package github.com.wildchild04.http;

public enum Status {
    OK(200, "OK"),
    CREATED(201, "Created"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method not allowed"),
    SERVER_ERR(500, "Server error");

    private final int value;
    private final String message;

    Status(int i, String message) {
        this.value = i;
        this.message = message;
    }

    public int getIntValue() {
        return value;
    }

    public String getStringValue() {
        return  message;
    }
}
