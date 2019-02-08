package main.exception;

/**
 * “Ï≥£¿‡
 * 
 * @author liucheng
 */

public class BPRPlusException extends RuntimeException {

	private static final long serialVersionUID = 4072415788185880975L;

	public BPRPlusException(String message) {
		super(message);
	}

	public BPRPlusException(Throwable exception) {
		super(exception);
	}

	public BPRPlusException(String message, Throwable exception) {
		super(message, exception);
	}
}
