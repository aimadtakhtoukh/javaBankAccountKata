package bankaccount.exceptions;

public class OverdrawnException extends Exception {

    public OverdrawnException(String message) {
        super(message);
    }
}
