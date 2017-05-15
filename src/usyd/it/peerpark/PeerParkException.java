package usyd.it.peerpark;

/*
 * Exception class to enclose other and prevent exposing details to the client
 */


/**
 *
 * @author Bryn
 */
public class PeerParkException extends Exception {

    public PeerParkException(String message) {
        super(message);
    }

    public PeerParkException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
