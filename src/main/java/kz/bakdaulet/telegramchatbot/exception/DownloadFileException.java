package kz.bakdaulet.telegramchatbot.exception;

public class DownloadFileException extends RuntimeException{
    public DownloadFileException(Throwable cause) {
        super(cause);
    }
    public DownloadFileException(String message) {
        super(message);
    }
}
