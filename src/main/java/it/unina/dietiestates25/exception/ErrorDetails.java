package it.unina.dietiestates25.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ErrorDetails implements Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String path;

    public ErrorDetails(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    public String toJson() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "{" +
                "\"timestamp\": \"" + (timestamp != null ? timestamp.format(formatter) : null) + "\"," +
                "\"status\": " + status + "," +
                "\"error\": \"" + escapeJson(error) + "\"," +
                "\"message\": \"" + escapeJson(message) + "\"," +
                "\"path\": \"" + escapeJson(path) + "\"" +
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) return null;
        return value.replace("\"", "\\\"");
    }
}

