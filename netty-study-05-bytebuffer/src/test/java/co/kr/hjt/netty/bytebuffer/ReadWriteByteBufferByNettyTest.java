package co.kr.hjt.netty.bytebuffer;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * 네티 바이트 버퍼는 Nio 바이트 버퍼와는 달리
 * 읽기와 쓰기 인덱스가 별개로 관리되기 때문에  
 * Nio 바이트 버퍼처럼 쓰기 읽기간 전환을 위한flip()을
 * 호출할 필요가 없다 
 */
public class ReadWriteByteBufferByNettyTest {
    @Test
    public void createUnpooledHeapBufferTest() {
        
        ByteBuf buf = Unpooled.buffer(11);

        testBuffer(buf, false);
    }

    @Test
    public void createUnpooledDirectBufferTest() {
        ByteBuf buf = Unpooled.directBuffer(11);

        testBuffer(buf, true);
    }

    @Test
    public void createPooledHeapBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.heapBuffer(11);

        testBuffer(buf, false);
    }

    @Test
    public void createPooledDirectBufferTest() {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.directBuffer(11);

        testBuffer(buf, true);
    }

    private void testBuffer(ByteBuf buf, boolean isDirect) {
        assertEquals(11, buf.capacity());

        assertEquals(isDirect, buf.isDirect());
        /**
         * 4바이트 크기의 정수 65537을 기록
         * 기록한 데이터 크기만큼 writeIndex 속성값을 증가
         */
        buf.writeInt(65537);
        
        //바이트 버퍼에서 읽어 들일 수 있는 바이트 크기가 4인지 확인한다.
        assertEquals(4, buf.readableBytes()); 
        
        //바이트 버퍼에 기록할 수 있는 바이트 크기가 7인지 확인한다.11 - 4 = 7
        assertEquals(7, buf.writableBytes());
        
        //2바이트 크기의 정수를 읽는다
        assertEquals(1, buf.readShort());
        assertEquals(2, buf.readableBytes());
        assertEquals(7, buf.writableBytes());
        
        buf.clear();
        
        assertEquals(0, buf.readableBytes());
        assertEquals(11, buf.writableBytes());
    }
}
