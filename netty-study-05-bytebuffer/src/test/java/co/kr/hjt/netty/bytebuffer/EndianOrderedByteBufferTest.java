package co.kr.hjt.netty.bytebuffer;

import static org.junit.Assert.assertEquals;

import java.nio.ByteOrder;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class EndianOrderedByteBufferTest {
    @Test
    public void pooledHeapBufferTest() {
        ByteBuf buf = Unpooled.buffer(11);
        /**
         * order 확인 필요없이 get타입LE라고 명명된 메소드를 호출해서
         * 리틀엔디안에 해당하는 타입 값을 가져올 수 있따.
         */
        assertEquals(ByteOrder.BIG_ENDIAN, buf.order());

        /**
         * 현재 읽기 인덱스 위치를 마킹한다.
         */
        buf.markReaderIndex();
        buf.writeShort(1);
        assertEquals(1, buf.readShort());

        /**
         * 마킹한 읽기 인덱스 위치로 이동한다.
         */
        buf.resetReaderIndex();


        /**
         * 네티 바이트버퍼의 order메서드는 새로운 바이트 버퍼 생성하는 것이 아닌
         * 주어진 바이트 버퍼의 내용을 공유하는 파생 바이트 버퍼 객체를 생성하므로 유의해야한다.
         */
        ByteBuf lettleEndianBuf = buf.order(ByteOrder.LITTLE_ENDIAN);
        /**
         * 빅엔디안 0x0001을 리틀엔디안으로 변환하면 0x0100이므로 256이된다.
         */
        assertEquals(256, lettleEndianBuf.readShort());
    }
}
