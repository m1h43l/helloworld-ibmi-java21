package helloworld;

import java.nio.charset.StandardCharsets;

import io.helidon.common.buffers.DataReader;
import io.helidon.http.HttpPrologue;
import io.helidon.webserver.http1.Http1Prologue;

public class DataReaderTest {

    public static final String DATA = "GET / HTTP/1.1\r\nHost: 127.0.0.1:8181\r\nUser-Agent: curl/8.6.0\r\nAccept: */*\r\n\r\n";

    public static void main(String[] args) {
        DataReader reader = new DataReader(() -> DATA.getBytes(StandardCharsets.UTF_8));

        Http1Prologue prologue = new Http1Prologue(reader, 8192, false);
        HttpPrologue pro = prologue.readPrologue();

        System.out.println(pro);
    }
}
