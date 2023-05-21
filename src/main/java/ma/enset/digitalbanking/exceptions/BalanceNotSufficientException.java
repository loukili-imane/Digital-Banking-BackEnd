package ma.enset.digitalbanking.exceptions;

public class BalanceNotSufficientException extends Exception {
    public BalanceNotSufficientException(String msg) {
        super(msg);
    }
}
