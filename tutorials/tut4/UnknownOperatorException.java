public class UnknownOperatorException extends Exception {
    public UnknownOperatorException() {
        super("Unknown operator exception occurred.");
    }
    public UnknownOperatorException(char op) {
        super(op + " is an unknown operator.");
    }
    public UnknownOperatorException(String message) {
        super(message + " is an unknown operator.");
    }
}