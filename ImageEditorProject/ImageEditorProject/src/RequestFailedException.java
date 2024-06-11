/**
 * An exception indicating that a request could not be fulfilled.
 */
public class RequestFailedException extends Exception {
  /**
   * Creates an exception.
   *
   * @param message a message describing why the request could not be fulfilled
   */
  public RequestFailedException(String message) {
    super(message);
  }
}
