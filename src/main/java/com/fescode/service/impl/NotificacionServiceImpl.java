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
            String html = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "    <title>Bienvenido a NovaShop</title>" +
                    "    <style>" +
                    "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                    "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; }" +
                    "        .email-container { max-width: 600px; margin: 0 auto; background: #ffffff; }" +
                    "        .header { background: linear-gradient(135deg, #000000 0%, #333333 100%); padding: 50px 20px; text-align: center; position: relative; }" +
                    "        .header::before { content: ''; position: absolute; top: 0; left: 0; right: 0; bottom: 0; background: url('data:image/svg+xml,<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 100 100\"><defs><pattern id=\"grain\" width=\"100\" height=\"100\" patternUnits=\"userSpaceOnUse\"><circle cx=\"50\" cy=\"50\" r=\"1\" fill=\"%23ffffff\" opacity=\"0.1\"/></pattern></defs><rect width=\"100\" height=\"100\" fill=\"url(%23grain)\"/></svg>') repeat; }" +
                    "        .logo-container { width: 120px; height: 60px; background: #ffffff; border-radius: 8px; margin: 0 auto 25px; display: flex; align-items: center; justify-content: center; position: relative; z-index: 1; }" +
                    "        .logo-text { color: #000000; font-weight: bold; font-size: 18px; }" +
                    "        .welcome-icon { width: 100px; height: 100px; margin: 0 auto 25px; background: #ffffff; border-radius: 50%; display: flex; align-items: center; justify-content: center; position: relative; z-index: 1; }" +
                    "        .welcome-emoji { font-size: 50px; }" +
                    "        .header h1 { color: #ffffff; font-size: 32px; font-weight: 700; margin-bottom: 15px; position: relative; z-index: 1; }" +
                    "        .header .subtitle { color: #e0e0e0; font-size: 18px; font-weight: 300; position: relative; z-index: 1; }" +
                    "        .content { padding: 50px 40px; background: #ffffff; }" +
                    "        .welcome-message { text-align: center; margin-bottom: 40px; }" +
                    "        .welcome-message h2 { color: #000000; font-size: 26px; margin-bottom: 20px; font-weight: 600; }" +
                    "        .welcome-message p { color: #666666; font-size: 16px; margin-bottom: 15px; line-height: 1.7; }" +
                    "        .benefits-section { background: #f8f9fa; border-radius: 12px; padding: 35px; margin: 40px 0; }" +
                    "        .benefits-title { color: #000000; font-size: 20px; font-weight: 600; text-align: center; margin-bottom: 25px; }" +
                    "        .benefit-item { display: flex; align-items: flex-start; margin-bottom: 20px; padding: 15px; background: #ffffff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }" +
                    "        .benefit-icon { width: 40px; height: 40px; background: #000000; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-right: 15px; flex-shrink: 0; }" +
                    "        .benefit-icon span { color: #ffffff; font-size: 18px; }" +
                    "        .benefit-content h4 { color: #000000; font-size: 16px; font-weight: 600; margin-bottom: 5px; }" +
                    "        .benefit-content p { color: #666666; font-size: 14px; line-height: 1.5; }" +
                    "        .cta-section { text-align: center; margin: 50px 0; padding: 40px; background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%); border-radius: 12px; }" +
                    "        .cta-title { color: #000000; font-size: 22px; font-weight: 600; margin-bottom: 15px; }" +
                    "        .cta-subtitle { color: #666666; font-size: 16px; margin-bottom: 25px; }" +
                    "        .cta-button { display: inline-block; background: #000000; color: #ffffff; padding: 18px 35px; text-decoration: none; border-radius: 8px; font-weight: 600; font-size: 16px; transition: all 0.3s ease; box-shadow: 0 4px 12px rgba(0,0,0,0.2); }" +
                    "        .cta-button:hover { background: #333333; transform: translateY(-2px); }" +
                    "        .exclusive-message { background: #000000; color: #ffffff; padding: 30px; border-radius: 12px; text-align: center; margin: 40px 0; }" +
                    "        .exclusive-message h3 { font-size: 20px; margin-bottom: 15px; font-weight: 600; }" +
                    "        .exclusive-message p { font-size: 16px; opacity: 0.9; line-height: 1.6; }" +
                    "        .signature { margin: 50px 0 30px; padding: 25px; border-left: 4px solid #000000; background: #f8f9fa; }" +
                    "        .signature p { color: #666666; font-size: 16px; margin-bottom: 10px; }" +
                    "        .signature .team { color: #000000; font-weight: 600; font-size: 16px; }" +
                    "        .footer { background: #f8f9fa; padding: 40px 30px; text-align: center; border-top: 1px solid #e9ecef; }" +
                    "        .footer-content { margin-bottom: 25px; }" +
                    "        .footer-content p { color: #666666; font-size: 14px; margin-bottom: 10px; }" +
                    "        .social-links { margin: 25px 0; }" +
                    "        .social-links a { display: inline-block; margin: 0 15px; color: #000000; text-decoration: none; font-weight: 500; }" +
                    "        .divider { height: 1px; background: linear-gradient(to right, transparent, #e9ecef, transparent); margin: 30px 0; }" +
                    "        .footer-legal { font-size: 12px; color: #999999; line-height: 1.5; }" +
                    "        @media only screen and (max-width: 600px) {" +
                    "            .content { padding: 30px 20px; }" +
                    "            .header { padding: 40px 15px; }" +
                    "            .header h1 { font-size: 26px; }" +
                    "            .welcome-message h2 { font-size: 22px; }" +
                    "            .benefits-section { padding: 25px 20px; }" +
                    "            .benefit-item { flex-direction: column; text-align: center; }" +
                    "            .benefit-icon { margin: 0 auto 15px; }" +
                    "            .cta-section { padding: 30px 20px; }" +
                    "            .exclusive-message { padding: 25px 20px; }" +
                    "        }" +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='email-container'>" +
                    "        <div class='header'>" +
                    "            <div class='logo-container'>" +
                    "                <span class='logo-text'>NovaShop</span>" +
                    "            </div>" +
                    "            <div class='welcome-icon'>" +
                    "                <span class='welcome-emoji'>🎉</span>" +
                    "            </div>" +
                    "            <h1>¡Bienvenido a NovaShop!</h1>" +
                    "            <p class='subtitle'>Tu nueva experiencia de compra exclusiva comienza aquí</p>" +
                    "        </div>" +
                    "        <div class='content'>" +
                    "            <div class='welcome-message'>" +
                    "                <h2>Gracias por unirte a nuestra comunidad</h2>" +
                    "                <p>Has dado el primer paso hacia una experiencia de compra donde la <strong>elegancia</strong> y la <strong>calidad</strong> son nuestra prioridad absoluta.</p>" +
                    "                <p>En NovaShop, no solo compras productos: accedes a una experiencia exclusiva diseñada para personas únicas, exigentes y con buen gusto como tú.</p>" +
                    "            </div>" +
                    "            <div class='benefits-section'>" +
                    "                <h3 class='benefits-title'>Lo que te espera en NovaShop</h3>" +
                    "                <div class='benefit-item'>" +
                    "                    <div class='benefit-icon'><span>✨</span></div>" +
                    "                    <div class='benefit-content'>" +
                    "                        <h4>Catálogo Refinado</h4>" +
                    "                        <p>Productos cuidadosamente seleccionados que reflejan calidad y estilo excepcional.</p>" +
                    "                    </div>" +
                    "                </div>" +
                    "                <div class='benefit-item'>" +
                    "                    <div class='benefit-icon'><span>👥</span></div>" +
                    "                    <div class='benefit-content'>" +
                    "                        <h4>Atención Personalizada</h4>" +
                    "                        <p>Servicio al cliente dedicado que entiende tus necesidades y preferencias únicas.</p>" +
                    "                    </div>" +
                    "                </div>" +
                    "                <div class='benefit-item'>" +
                    "                    <div class='benefit-icon'><span>🎁</span></div>" +
                    "                    <div class='benefit-content'>" +
                    "                        <h4>Beneficios Exclusivos</h4>" +
                    "                        <p>Acceso prioritario a nuevos productos, ofertas especiales y eventos exclusivos.</p>" +
                    "                    </div>" +
                    "                </div>" +
                    "            </div>" +
                    "            <div class='cta-section'>" +
                    "                <h3 class='cta-title'>¿Listo para explorar?</h3>" +
                    "                <p class='cta-subtitle'>Descubre nuestro catálogo exclusivo y encuentra productos que elevarán tu estilo</p>" +
                    "                <a href='#' class='cta-button'>Explorar NovaShop</a>" +
                    "            </div>" +
                    "            <div class='exclusive-message'>" +
                    "                <h3>🌟 Desde ahora estás dentro</h3>" +
                    "                <p>NovaShop está aquí para elevar tus estándares y acompañarte en cada compra con la excelencia que mereces. Bienvenido a una nueva forma de comprar.</p>" +
                    "            </div>" +
                    "            <div class='signature'>" +
                    "                <p>Con la más cordial bienvenida,</p>" +
                    "                <p class='team'>El equipo de NovaShop</p>" +
                    "            </div>" +
                    "        </div>" +
                    "        <div class='footer'>" +
                    "            <div class='footer-content'>" +
                    "                <p><strong>NovaShop</strong> - Donde la elegancia encuentra la calidad</p>" +
                    "                <p>Gracias por elegirnos como tu destino de compras exclusivo</p>" +
                    "            </div>" +
                    "            <div class='social-links'>" +
                    "                <a href='#'>Facebook</a> |" +
                    "                <a href='#'>Instagram</a> |" +
                    "                <a href='#'>Twitter</a>" +
                    "            </div>" +
                    "            <div class='divider'></div>" +
                    "            <div class='footer-legal'>" +
                    "                <p>Este correo fue generado automáticamente. Por favor, no respondas a este mensaje.</p>" +
                    "                <p>Si tienes alguna pregunta, contacta nuestro equipo de atención al cliente.</p>" +
                    "                <p style='margin-top: 15px;'>© 2024 NovaShop. Todos los derechos reservados.</p>" +
                    "            </div>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";
            helper.setText(html, true);
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
            String html = "<!DOCTYPE html>" +
                    "<html lang='es'>" +
                "<head>" +
                "    <meta charset='UTF-8'>" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "    <title>Confirmación de Compra - NovaShop</title>" +
                "    <style>" +
                "        * { margin: 0; padding: 0; box-sizing: border-box; }" +
                "        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; line-height: 1.6; }" +
                "        .email-container { max-width: 600px; margin: 0 auto; background: #ffffff; }" +
                "        .header { background: linear-gradient(135deg, #000000 0%, #333333 100%); padding: 40px 20px; text-align: center; }" +
                "        .logo { width: 150px; height: auto; margin-bottom: 20px; }" +
                "        .header h1 { color: #ffffff; font-size: 28px; font-weight: 700; margin-bottom: 10px; }" +
                "        .header p { color: #e0e0e0; font-size: 16px; }" +
                "        .content { padding: 40px 30px; background: #ffffff; }" +
                "        .success-icon { width: 80px; height: 80px; margin: 0 auto 30px; background: #000000; border-radius: 50%; display: flex; align-items: center; justify-content: center; }" +
                "        .checkmark { color: #ffffff; font-size: 40px; font-weight: bold; }" +
                "        .main-message { text-align: center; margin-bottom: 40px; }" +
                "        .main-message h2 { color: #000000; font-size: 24px; margin-bottom: 15px; }" +
                "        .main-message p { color: #666666; font-size: 16px; margin-bottom: 10px; }" +
                "        .order-details { background: #f8f9fa; border: 1px solid #e9ecef; border-radius: 8px; padding: 25px; margin: 30px 0; }" +
                "        .order-details h3 { color: #000000; font-size: 18px; margin-bottom: 15px; border-bottom: 2px solid #000000; padding-bottom: 10px; }" +
                "        .detail-row { display: flex; justify-content: space-between; margin-bottom: 10px; padding: 8px 0; }" +
                "        .detail-label { color: #666666; font-weight: 500; }" +
                "        .detail-value { color: #000000; font-weight: 600; }" +
                "        .attachment-notice { background: #000000; color: #ffffff; padding: 20px; border-radius: 8px; text-align: center; margin: 30px 0; }" +
                "        .attachment-notice h4 { font-size: 16px; margin-bottom: 8px; }" +
                "        .attachment-notice p { font-size: 14px; opacity: 0.9; }" +
                "        .cta-section { text-align: center; margin: 40px 0; }" +
                "        .cta-button { display: inline-block; background: #000000; color: #ffffff; padding: 15px 30px; text-decoration: none; border-radius: 6px; font-weight: 600; font-size: 16px; transition: all 0.3s ease; }" +
                "        .cta-button:hover { background: #333333; }" +
                "        .footer { background: #f8f9fa; padding: 30px 20px; text-align: center; border-top: 1px solid #e9ecef; }" +
                "        .footer p { color: #666666; font-size: 14px; margin-bottom: 10px; }" +
                "        .social-links { margin: 20px 0; }" +
                "        .social-links a { display: inline-block; margin: 0 10px; color: #000000; text-decoration: none; }" +
                "        .divider { height: 1px; background: linear-gradient(to right, transparent, #e9ecef, transparent); margin: 30px 0; }" +
                "        @media only screen and (max-width: 600px) {" +
                "            .content { padding: 20px 15px; }" +
                "            .header { padding: 30px 15px; }" +
                "            .header h1 { font-size: 24px; }" +
                "            .main-message h2 { font-size: 20px; }" +
                "            .order-details { padding: 20px 15px; }" +
                "            .detail-row { flex-direction: column; }" +
                "            .detail-value { margin-top: 5px; }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class='email-container'>" +
                "        <div class='header'>" +
                "            <h1>¡Compra Confirmada!</h1>" +
                "            <p>Tu pedido ha sido procesado exitosamente</p>" +
                "        </div>" +
                "        <div class='content'>" +
                "            <div class='success-icon'>" +
                "                <span class='checkmark'>✓</span>" +
                "            </div>" +
                "            <div class='main-message'>" +
                "                <h2>¡Gracias por tu compra!</h2>" +
                "                <p>Hemos recibido tu pedido y está siendo procesado.</p>" +
                "                <p>Te enviaremos actualizaciones sobre el estado de tu envío.</p>" +
                "            </div>" +
                "            <div class='order-details'>" +
                "                <h3>Detalles del Pedido</h3>" +
                "                <div class='detail-row'>" +
                "                    <span class='detail-label'>Número de Pedido:</span>" +
                "                    <span class='detail-value'>#" + request.getPedidoId() + "</span>" +
                "                </div>" +
                "                <div class='detail-row'>" +
                "                    <span class='detail-label'>Fecha de Compra:</span>" +
                "                    <span class='detail-value'>" + java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "</span>" +
                "                </div>" +
                "                <div class='detail-row'>" +
                "                    <span class='detail-label'>Estado:</span>" +
                "                    <span class='detail-value'>Confirmado</span>" +
                "                </div>" +
                "            </div>" +
                "            <div class='attachment-notice'>" +
                "                <h4>📄 Boleta Adjunta</h4>" +
                "                <p>Tu boleta de compra está adjunta a este correo en formato PDF</p>" +
                "            </div>" +
                "            <div class='divider'></div>" +
                "            <div class='cta-section'>" +
                "                <p style='color: #666666; margin-bottom: 20px;'>¿Necesitas ayuda con tu pedido?</p>" +
                "                <a href='#' class='cta-button'>Contactar Soporte</a>" +
                "            </div>" +
                "            <div class='divider'></div>" +
                "            <div style='text-align: center; color: #666666;'>" +
                "                <p style='margin-bottom: 15px;'><strong>¿Qué sigue?</strong></p>" +
                "                <p style='margin-bottom: 10px;'>• Recibirás un correo cuando tu pedido sea enviado</p>" +
                "                <p style='margin-bottom: 10px;'>• Podrás rastrear tu envío con el número de seguimiento</p>" +
                "                <p style='margin-bottom: 10px;'>• El tiempo estimado de entrega es de 3-5 días hábiles</p>" +
                "            </div>" +
                "        </div>" +
                "        <div class='footer'>" +
                "            <p><strong>NovaShop</strong> - Tu tienda de confianza</p>" +
                "            <p>Esperamos que disfrutes tu producto. ¡Gracias por confiar en NovaShop!</p>" +
                "            <div class='social-links'>" +
                "                <a href='#'>Facebook</a> |" +
                "                <a href='#'>Instagram</a> |" +
                "                <a href='#'>Twitter</a>" +
                "            </div>" +
                "            <div class='divider'></div>" +
                "            <p style='font-size: 12px; color: #999999;'>" +
                "                Este correo fue generado automáticamente. No respondas a este mensaje.<br>" +
                "                Si tienes alguna pregunta, contacta nuestro equipo de soporte." +
                "            </p>" +
                "            <p style='font-size: 12px; color: #999999; margin-top: 10px;'>" +
                "               Copyright © 2025 NovaShop. Todos los derechos reservados." +
                "            </p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
            helper.setText(html, true);

            helper.addAttachment("boleta_" + request.getPedidoId() + ".pdf",
                    new ByteArrayResource(pdfAdjunto));

            mailSender.send(mensaje);

        } catch (Exception e) {
            throw new RuntimeException("❌ Error al enviar el correo con adjunto", e);
        }
    }
}
