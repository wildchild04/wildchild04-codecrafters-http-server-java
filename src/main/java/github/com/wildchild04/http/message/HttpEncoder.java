package github.com.wildchild04.http.message;

import java.nio.ByteBuffer;

public class HttpEncoder {
    private HttpEncoder(){}

    public static byte[] encodeString(String s) {
        ByteBuffer res = ByteBuffer.allocate(s.length()+2);
        res.put(s.getBytes());
        res.put((byte) '\r');
        res.put((byte) '\n');

        return res.array();
    }
}
