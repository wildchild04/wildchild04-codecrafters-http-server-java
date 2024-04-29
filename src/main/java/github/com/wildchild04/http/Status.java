package github.com.wildchild04.http;

public enum Status {
    OK(200, "OK"),
    NOT_FOUND(404, " Not Found");

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
