package github.com.wildchild04.http.message;

import java.nio.ByteBuffer;

public record Message(byte[] requestLine, byte[] headers, byte[] body) {

    public byte[] toBytes() {
        int totalSize = requestLine.length + headers.length + body.length;
        ByteBuffer res = ByteBuffer.allocate(totalSize);

        res.put(requestLine);
        res.put(headers);
        res.put(body);

        return res.array();
    }
}
