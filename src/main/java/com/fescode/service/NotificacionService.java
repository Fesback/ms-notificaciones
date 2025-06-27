package com.fescode.service;

import com.fescode.dto.request.CorreoAdjuntoRequestDTO;
import com.fescode.dto.request.NotificacionRequestDTO;
import org.springframework.scheduling.annotation.Async;

public interface NotificacionService {
    void enviarCorreo(NotificacionRequestDTO request);

    @Async
    void enviarCorreoConPdfAdjunto(CorreoAdjuntoRequestDTO request);
}
