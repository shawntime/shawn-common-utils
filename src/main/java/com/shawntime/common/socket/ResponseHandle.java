package com.shawntime.common.socket;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

import com.shawntime.common.socket.pool.ProtoPack;

/**
 * protobuf响应处理封装抽象类
 * 
 * @author admin
 *
 */
public abstract class ResponseHandle<T> {
	
	private String ip;
	
	private int port;
	
	private int seqId;
	
	private final byte[] readIn(InputStream inputStream) throws IOException {
		int readBytes = 0;  
		byte[] buffer = new byte[1024];//1024可改成任何需要的值  
		int len = buffer.length;
		while (readBytes < len) {
			
			int read = inputStream.read(buffer, readBytes, len - readBytes);  
			
			//判断是不是读到了数据流的末尾 ，防止出现死循环。  
			if (read == -1 || read < (len - readBytes)) {
				readBytes += read; 
				break;
			}
			
			if(read == (len - readBytes)) {
				byte[] tmpBuffer = new byte[len * 2];
				System.arraycopy(buffer, 0, tmpBuffer, 0, buffer.length);
				buffer = tmpBuffer;
				len = buffer.length;
			}
		  
			readBytes += read;
		}
		
		byte[] endodedData = new byte[readBytes];
        System.arraycopy(buffer, 0, endodedData, 0, readBytes);
		
		return endodedData;
	}

	public final ProtoPack unpack(InputStream inputStream) throws IOException {
		byte[] data = readIn(inputStream);
		byte[] cache = new byte[1024 * 16];
		int end = 0;
		
		System.arraycopy(data, 0, cache, end, data.length);
		end += data.length;
		while (end > 0) {
			if (end < 9) {
				return null;
			}
			try {
				int headLen = readInt(cache, 1);
				int bodyLen = readInt(cache, 5);
				if (end < 10 + headLen + bodyLen) {
					return null;
				}

				byte[] headBuf = new byte[headLen];
				byte[] bodyBuf = new byte[bodyLen];
				System.arraycopy(cache, 9, headBuf, 0, headLen);
				System.arraycopy(cache, 9 + headLen, bodyBuf, 0, bodyLen);

				//数据前移
				int frameLen = 10 + headLen + bodyLen;
				int newEnd = end - frameLen;
				if (newEnd > 0) {
					System.arraycopy(cache, frameLen, cache, 0, newEnd);
				}
				end = newEnd;
				
				seqId = encodeHeaderReqId(headBuf);
				if(seqId > 0) {
					ProtoPack protoPack = new ProtoPack();
					protoPack.setHeader(headBuf);
					protoPack.setBody(bodyBuf);
					protoPack.setSeqId(seqId);
					return protoPack;
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	//按小端模式读取一个int
	private final int readInt(byte[] readBuffer, int pos) throws IOException {
		if (readBuffer.length < pos + 4) {
			throw new EOFException();
		}
		return (((int) (readBuffer[pos] & 255) << 24) + ((readBuffer[pos + 1] & 255) << 16) + ((readBuffer[pos + 2] & 255) << 8) + ((readBuffer[pos + 3] & 255) << 0));
	}
	
	public int encodeHeaderReqId(byte[] headBuf) {
		
//		try {
//			HeadOuterClass.Head header = HeadOuterClass.Head.parseFrom(headBuf);
//			int seqId = header.getMsgCsHead().getUint32Seq();
//			return seqId;
//		} catch (InvalidProtocolBufferException e) {
//			e.printStackTrace();
//		}
//
		return -1;
		
	}
	
	public abstract void encode(byte[] headBuf, byte[] bodyBuf);
	
	public abstract T get();

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getSeqId() {
		return seqId;
	}

	public void setSeqId(int seqId) {
		this.seqId = seqId;
	}
	
}
