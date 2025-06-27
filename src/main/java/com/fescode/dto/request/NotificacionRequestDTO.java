package com.fescode.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class NotificacionRequestDTO {
    @Email
    @NotBlank
    private String correo;
    @NotBlank
    private String asunto;
    @NotBlank
    private String contenido;

}


