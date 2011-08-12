package org.biblestudio.model;

public class BinaryFile extends ModelEntity {

	private byte[] bytes;
	
	public BinaryFile() {
		
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
	public byte[] getBytes() {
		return bytes;
	}
}
