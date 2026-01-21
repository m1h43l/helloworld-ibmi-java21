package helloworld;

import io.helidon.common.buffers.Bytes;

public class BytesTest {

    public static void main(String[] args) {
        byte[] bytes = new byte[] { 71, 69, 84, 32, 47, 32, 72, 84, 84, 80, 47, 49, 46, 49, 13, 10, 72, 111, 115, 116,
                58, 32, 49, 50, 55, 46, 48, 46, 48, 46, 49, 58, 56, 49, 56, 49, 13, 10, 85, 115, 101, 114, 45, 65, 103,
                101, 110, 116, 58, 32, 99, 117, 114, 108, 47, 56, 46, 54, 46, 48, 13, 10, 65, 99, 99, 101, 112, 116, 58,
                32, 42, 47, 42, 13, 10, 13, 10 };

        int crIndex = Bytes.firstIndexOf(bytes, 0, bytes.length, Bytes.CR_BYTE);
        System.out.println(crIndex);
        int lfIndex = Bytes.firstIndexOf(bytes, crIndex, bytes.length, Bytes.LF_BYTE);
        System.out.println(lfIndex);
    }

}
