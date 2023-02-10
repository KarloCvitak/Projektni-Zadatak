package hr.java.projektnizadatak.iznimke;

public class PromjeneException extends RuntimeException{
    public PromjeneException() {
    }

    public PromjeneException(String message) {
        super(message);
    }

    public PromjeneException(String message, Throwable cause) {
        super(message, cause);
    }

    public PromjeneException(Throwable cause) {
        super(cause);
    }
}
