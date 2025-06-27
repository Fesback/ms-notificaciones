package com.fescode.service.impl;

import com.fescode.dto.request.CorreoAdjuntoRequestDTO;
import com.fescode.dto.request.NotificacionRequestDTO;
import com.fescode.service.NotificacionService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@EnableAsync
@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final JavaMailSender mailSender;
    private final RestTemplate restTemplate;

    @Override
    public void enviarCorreo(NotificacionRequestDTO request) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(request.getCorreo());
            helper.setSubject(request.getAsunto());
            helper.setText(request.getContenido(), true);
            mailSender.send(mensaje);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: ", e);
        }
    }

    @Async
    @Override
    public void enviarCorreoConPdfAdjunto(CorreoAdjuntoRequestDTO request) {
        try {
            String url = "http://localhost:8081/api/facturacion/boleta/" + request.getPedidoId();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_PDF));
            headers.set("Authorization", "Bearer " + request.getToken());

            HttpEntity<Void> httpEntity = new HttpEntity<>(headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    byte[].class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("No se pudo obtener el PDF de la boleta");
            }

            byte[] pdfAdjunto = response.getBody();


            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(request.getCorreo());
            helper.setSubject(request.getAsunto());
            helper.setText(request.getMensaje(), true);

            helper.addAttachment("boleta_" + request.getPedidoId() + ".pdf",
                    new ByteArrayResource(pdfAdjunto));

            mailSender.send(mensaje);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error al enviar el correo con adjunto", e);
        }
    }
}
