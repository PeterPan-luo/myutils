package io.github.luohong.serializer;

public class SerializeException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public SerializeException(String s) {
		super(s);
	}
	
	public SerializeException(Throwable e) {
		super(e);
	}
	
	public SerializeException(String s, Throwable e) {
		super(s,e);
	}

}
