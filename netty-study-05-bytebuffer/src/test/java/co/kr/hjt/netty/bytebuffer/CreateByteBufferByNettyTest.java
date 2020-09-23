package co.kr.hjt.netty.bytebuffer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * 네티 바이트 버퍼만 사용하고 싶으면 netty-buffer-4.1.52.Final.jar만 사용
 * 네티는 프레임워크 레벨에서 버퍼 풀링을 재공해서 생성된 버퍼를 재사용할 수 있다.
 */
public class CreateByteBufferByNettyTest {

    /**
     *  11바이트 크기의 풀링 하지 않는 힙버퍼 생성 
     */
    @Test
    public void createUnpooledHeapBufferTest() {
        ByteBuf buf = Unpooled.buffer(11);

        testBuffer(buf, false);
    }

    /**
     * 11바이트 크기의 풀링하지 않는 다이렉트버퍼 생성
     */
    @Test
    public void createUnpooledDirectBufferTest() {
        ByteBuf buf = Unpooled.directBuffer(11);

        testBuffer(buf, true);
    }

    /**
     * 11바이트 크기의 풀링 힙버퍼 생성 
     */
    @Test
    public void createPooledHeapBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);

        testBuffer(buf, false);
    }

    /**
     * 11바이트 크기의 풀링 다이렉트 버퍼 생성 
     */
    @Test
    public void createPooledDirectBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);

        testBuffer(buf, true);
    }

    private void testBuffer(ByteBuf buf, boolean isDirect) {
        assertEquals(11, buf.capacity());

        assertEquals(isDirect, buf.isDirect());

        assertEquals(0, buf.readableBytes());
        assertEquals(11, buf.writableBytes());
    }

}
