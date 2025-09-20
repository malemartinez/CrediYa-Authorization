package co.com.crediyaauthentication.api.error;

public record ApiErrorResponse(
        int status,
        String error,
        Object message,
        String path
) {}
