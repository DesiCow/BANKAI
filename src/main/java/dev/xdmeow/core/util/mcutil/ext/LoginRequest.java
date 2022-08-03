package dev.xdmeow.core.util.mcutil.ext;

import dev.xdmeow.core.util.mcutil.DefinedPacket;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class LoginRequest extends DefinedPacket {
    public String data;

    public LoginRequest(String heil) {
        this.data = heil;
    }

    public void write(ByteBuf buf) {
        DefinedPacket.writeString(this.data, buf);
    }

    public void writeC(ByteBuf buf) {
        DefinedPacket.writeStringC(this.data, buf);
    }

    public byte[] getWrappedPacket() {
        ByteBuf allocated = Unpooled.buffer();
        allocated.writeByte(0);
        this.write(allocated);
        ByteBuf wrapped = Unpooled.buffer();
        DefinedPacket.writeVarInt(allocated.readableBytes(), wrapped);
        wrapped.writeBytes(allocated);
        byte[] bytes = new byte[wrapped.readableBytes()];
        wrapped.getBytes(0, (byte[])bytes);
        wrapped.release();
        return bytes;
    }

    public byte[] getWrappedPacketC() {
        ByteBuf allocated = Unpooled.buffer();
        allocated.writeByte(0);
        this.writeC(allocated);
        ByteBuf wrapped = Unpooled.buffer();
        DefinedPacket.writeVarInt(allocated.readableBytes(), wrapped);
        wrapped.writeBytes(allocated);
        byte[] bytes = new byte[wrapped.readableBytes()];
        wrapped.getBytes(0, (byte[])bytes);
        wrapped.release();
        return bytes;
    }
}