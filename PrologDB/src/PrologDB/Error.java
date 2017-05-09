package PrologDB;

public class Error extends RuntimeException {

    /**
     * Stop PrologDB execution with error message msg
     *
     * @param msg
     */
    public Error(String msg) {
        super(msg);
    }
}
