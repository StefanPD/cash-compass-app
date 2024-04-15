package com.financing.app.bootstrap_module.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path
) {

    public static String toJsonString(ErrorResponse errorResponse) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        return "{" +
                "\"timestamp\": \"" + errorResponse.timestamp().format(formatter) + "\"," +
                "\"status\": " + errorResponse.status() + "," +
                "\"error\": \"" + escapeJson(errorResponse.error()) + "\"," +
                "\"message\": \"" + escapeJson(errorResponse.message()) + "\"," +
                "\"path\": \"" + escapeJson(errorResponse.path()) + "\"" +
                "}";
    }

    private static String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

}



