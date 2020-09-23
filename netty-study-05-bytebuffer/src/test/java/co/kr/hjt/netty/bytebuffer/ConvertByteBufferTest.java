package co.kr.hjt.netty.bytebuffer;

import static org.junit.Assert.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * 네티 바이트 버퍼와 자바 NIO바이트 버퍼간 변환이 가능하다.
 */
public class ConvertByteBufferTest {
    final String source = "Hello world";
    
    @Test
    public void convertNettyBufferToJavaBuffer() {
        ByteBuf buf = Unpooled.buffer(11);
        
        buf.writeBytes(source.getBytes());
        assertEquals(source, buf.toString(Charset.defaultCharset()));

        /**
         * 네티 바이트 버퍼의 nioBuffer 메서드로 자바 바이트 버퍼 객체 생성
         * 생성한 자바 바이트 버퍼와 네티 바이트 버퍼의 내부 배열은 서로 공유됨
         */
        ByteBuffer nioByteBuffer = buf.nioBuffer();
        assertNotNull(nioByteBuffer);
        assertEquals(source, new String(nioByteBuffer.array()));
    }

    @Test
    public void convertJavaBufferToNettyBuffer() {
        ByteBuffer byteBuffer = ByteBuffer.wrap(source.getBytes());
        ByteBuf nettyBuffer = Unpooled.wrappedBuffer(byteBuffer);

        assertEquals(source, nettyBuffer.toString(Charset.defaultCharset()));
    }
}
