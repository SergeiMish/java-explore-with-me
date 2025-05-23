package ru.practicum.service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {
    @NotBlank(message = "Name must not be blank")
    @Size(min = 2, max = 250, message = "Name length must be between 2 and 250 characters")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email should be valid")
    @Size(min = 6, max = 254, message = "Email length must be between 6 and 254 characters")
    private String email;
}