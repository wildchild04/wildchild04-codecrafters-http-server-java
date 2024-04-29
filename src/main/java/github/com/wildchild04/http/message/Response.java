package github.com.wildchild04.http.message;

import github.com.wildchild04.http.Status;
import github.com.wildchild04.http.Version;

import java.nio.ByteBuffer;
import java.util.Map;
import java.util.stream.Collectors;

public record Response(Version version, Status status, Map<String, String> headers, byte[] body) {

    public byte[] toBytes() {
        String stringHeaders  = headers.entrySet().stream().map(entry -> String.format("%s:%s", entry.getKey(), entry.getValue())).collect(Collectors.joining("\r\n"));
        byte[] encodedHeaders = HttpEncoder.encodeString(stringHeaders);
        String responseInfo = String.format("%s %d %s", version.getValue(), status.getIntValue(), status.getStringValue());
        var encodedResponseInfo = HttpEncoder.encodeString(responseInfo);
        int totalSize = encodedResponseInfo.length + body.length + encodedHeaders.length+2;
        ByteBuffer res = ByteBuffer.allocate(totalSize);

        res.put(encodedResponseInfo);
        res.put(encodedHeaders);
        res.put((byte) '\r');
        res.put((byte) '\n');
        res.put(body);
        return res.array();
    }
}
