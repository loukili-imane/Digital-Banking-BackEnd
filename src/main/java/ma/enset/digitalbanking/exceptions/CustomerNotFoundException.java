package ma.enset.digitalbanking.exceptions;

public class CustomerNotFoundException extends Exception{
    public CustomerNotFoundException(String msg){
        super(msg);
    }
}
