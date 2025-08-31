package co.com.crediyaauthentication.api.dto;

public record UserResponseDto(
        String name,
        String lastname,
        String email,
        String documentIdentification
) {
}
