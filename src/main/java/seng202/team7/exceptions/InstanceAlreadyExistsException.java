package seng202.team7.exceptions;

/**
 * Custom exception to be thrown when illegal access of a singleton instance is requested
 * In this case when we are trying to create an instance when one already exists
 * @author Morgan English
 */
public class InstanceAlreadyExistsException extends Exception {
    /**
     * Simple constructor that passes to parent Exception class
     * @param message error message
     */
    public InstanceAlreadyExistsException(String message) {
        super(message);
    }
}
