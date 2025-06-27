package com.fescode.dto.request;

import lombok.Data;

@Data
public class CorreoAdjuntoRequestDTO {
    private String correo;
    private String asunto;
    private String mensaje;
    private Long pedidoId;
    private String token;
}
