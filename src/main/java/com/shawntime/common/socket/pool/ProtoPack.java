package com.shawntime.common.socket.pool;

public class ProtoPack {
private int seqId;
private byte[] header;
private byte[] body;
public int getSeqId() {
	return seqId;
}
public void setSeqId(int seqId) {
	this.seqId = seqId;
}
public byte[] getHeader() {
	return header;
}
public void setHeader(byte[] header) {
	this.header = header;
}
public byte[] getBody() {
	return body;
}
public void setBody(byte[] body) {
	this.body = body;
}

}
