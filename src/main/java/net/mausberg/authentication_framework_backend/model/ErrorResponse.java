package net.mausberg.authentication_framework_backend.model;

/**
 * ErrorResponse is a simple class that represents an error message.
 */
public class ErrorResponse {
    private String message;

    /**
     * Constructs an ErrorResponse with the specified message.
     * 
     * @param message the error message
     */
    public ErrorResponse(String message) {
        this.message = message;
    }

    /**
     * Returns the error message.
     * 
     * @return the error message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the error message.
     * 
     * @param message the error message
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
