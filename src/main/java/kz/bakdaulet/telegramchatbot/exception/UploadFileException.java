package kz.bakdaulet.telegramchatbot.exception;

public class UploadFileException extends RuntimeException{
    public UploadFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public UploadFileException(Throwable cause) {
        super(cause);
    }
}
