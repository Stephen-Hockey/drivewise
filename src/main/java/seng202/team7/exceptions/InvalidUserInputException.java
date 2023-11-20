package seng202.team7.exceptions;

/**
 * Custom exception to be thrown when there is unacceptable user input that would not be caught automatically.
 * @author Stephen Hockey
 */
public class InvalidUserInputException extends Exception {

    /**
     * Simple constructor that passes to parent Exception class
     * @param message error message
     */
    public InvalidUserInputException(String message) {
        super(message);
    }
}