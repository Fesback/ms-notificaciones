package com.fescode.controller;

import com.fescode.dto.request.CorreoAdjuntoRequestDTO;
import com.fescode.dto.request.NotificacionRequestDTO;
import com.fescode.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping("/correo")
    public ResponseEntity<?> enviarCorreo(@RequestBody @Valid NotificacionRequestDTO request) {
        notificacionService.enviarCorreo(request);
        return ResponseEntity.ok("Correo enviado exitosamente");
    }

    @PostMapping("/correo-adjunto")
    public ResponseEntity<?> enviarCorreoConPdfAdjunto(@RequestBody @Valid CorreoAdjuntoRequestDTO request){
        notificacionService.enviarCorreoConPdfAdjunto(request);
        return ResponseEntity.ok("Correo con PDF adjunto enviado exitosamente");
    }
}
