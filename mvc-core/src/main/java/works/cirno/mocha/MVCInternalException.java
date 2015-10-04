package works.cirno.mocha;

public class MVCInternalException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4736592789363537011L;

	public MVCInternalException() {
		super();
	}

	public MVCInternalException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MVCInternalException(String message, Throwable cause) {
		super(message, cause);
	}

	public MVCInternalException(String message) {
		super(message);
	}

	public MVCInternalException(Throwable cause) {
		super(cause);
	}

}
