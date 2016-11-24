package com.shawntime.common.socket;

import java.io.OutputStream;

/**
 * protobuf请求封装抽象类
 * @author admin
 *
 */
public abstract class RequestHandle {
    private int seqId;
	public final void send(OutputStream outputStream) throws Exception {
		
		byte[] data = pack();
		
		outputStream.write(data);
		outputStream.flush();
	}
	
	/**
	 * 将输入的数据打包成TCP通讯包，返回值： 打包好的TCP数据
	 * @throws Exception 
	 */
	private final byte[] pack() throws Exception {
		
		byte[] headBuf = packHead();
		byte[] bodyBuf = packBody();
		
		//生成sendBuf
		byte[] sendBuf = new byte[headBuf.length + bodyBuf.length + 10];
		sendBuf[0] = (byte) '(';
		writeInt(sendBuf, headBuf.length, 1);
		writeInt(sendBuf, bodyBuf.length, 5);
		System.arraycopy(headBuf, 0, sendBuf, 9, headBuf.length);
		System.arraycopy(bodyBuf, 0, sendBuf, headBuf.length + 9, bodyBuf.length);
		sendBuf[sendBuf.length - 1] = (byte) ')';
		return sendBuf;
	}
	
	//按小端模式写入int
	private final void writeInt(byte[] writeBuffer, int v, int pos) {
		writeBuffer[pos] = (byte) (v >>> 24);
		writeBuffer[pos + 1] = (byte) (v >>> 16);
		writeBuffer[pos + 2] = (byte) (v >>> 8);
		writeBuffer[pos + 3] = (byte) (v >>> 0);
	}
	
	protected abstract byte[] packBody();
	
	/**
	 * 请求头内容
	 * @return
	 */
	protected abstract byte[] packHead();

	public int getSeqId() {
		return seqId;
	}

	public final void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	
}
