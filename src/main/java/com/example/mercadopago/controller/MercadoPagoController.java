package com.example.mercadopago.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.mercadopago.dto.BacksUrlDTO;
import com.example.mercadopago.dto.MpNotifyDTO;
import com.example.mercadopago.services.MercadoPagoService;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/mercadopago")
public class MercadoPagoController {

    @Autowired
    private MercadoPagoService mercadoPagoService;

    @PostMapping("/preference")
    public ResponseEntity<String> getIdPreference() {
        // Crear una instancia del logger
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String preferenceId = "ocurrio un error";

        try {
            BacksUrlDTO backsUrl = new BacksUrlDTO();
            backsUrl.setSuccess("URL DE TU FRONTEND /success");
            backsUrl.setPending("URL DE TU FRONTEND /pending");
            backsUrl.setFailure("URL DE TU FRONTEND /failed");

            // Llamada al servicio de MercadoPago para crear una preferencia de pago
            preferenceId = this.mercadoPagoService.createPreference("Titulo", 1, "ARS",
                    new BigDecimal("1000"), backsUrl,
                    "https://95d8-2803-9800-9803-5091-9552-4a64-f4f0-b9e.ngrok-free.app/api/v1/mercadopago/notify");

            //Dentro de este metodo podemos recibir información para pasarle por params al controlador de notify una vew que se haya realizado el pago, ej:el username del usuario,

            // Si se crea la preferencia correctamente, retornamos el ID
            return ResponseEntity.ok(preferenceId);
        } catch (MPException | MPApiException e) {
            // Capturamos excepciones específicas de MercadoPago
            logger.error("Error creando preferencia de pago en MercadoPago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creando preferencia de pago en MercadoPago: " + e.getMessage());
        } catch (Exception e) {
            // Capturamos cualquier otra excepción
            logger.error("Error inesperado creando preferencia de pago", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado creando preferencia de pago: " + e.getMessage());
        }

    }

    @PostMapping(value = "notify")
    public void notifyPay(@RequestBody MpNotifyDTO mpNotify) {
        // Crear una instancia del logger para registrar información y eventos.
        Logger logger = LoggerFactory.getLogger(this.getClass());

        // Registrar la notificación recibida.
        // Esto imprime la información de la notificación de pago a los registros
        // (logs).
        logger.info(mpNotify.toString());

        // Aquí recibimos la notificación del pago de MercadoPago.
        // Podemos realizar cualquier acción necesaria con esta información,
        // como guardar los detalles del pago en la base de datos,
        // actualizar el estado de una orden, enviar notificaciones a los usuarios, etc.
    }

}
