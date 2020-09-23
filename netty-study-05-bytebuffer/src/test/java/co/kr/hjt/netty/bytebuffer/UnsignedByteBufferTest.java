package co.kr.hjt.netty.bytebuffer;

import static org.junit.Assert.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import org.junit.Test;

/**
 * 부호없는 값 읽기
 *
 */
public class UnsignedByteBufferTest {
    final String source = "Hello world";

    @Test
    public void unsignedBufferToJavaBuffer() {
        ByteBuf buf = Unpooled.buffer(11);

        //바이트 버퍼에 -1을 기록
        buf.writeShort(-1);

        /**
         * 자바에서 1바이트 데이터를 부호없는 데이터로 변환하는 방법은 2바이트 데이터형에
         * 데이터를 저장하는 것이다. 부호없는 데이터를 읽을 때는 읽을 데이터보다
         * 큰 데이터형에 할당한다.
         */
        assertEquals(65535, buf.getUnsignedShort(0));
    }
}
