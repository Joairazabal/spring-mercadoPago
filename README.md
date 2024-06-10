# Integración con Mercado Pago

Este proyecto demuestra cómo integrar la API de Mercado Pago en una aplicación para gestionar pagos. Proporciona endpoints para crear preferencias de pago y manejar notificaciones de pago.

## Requisitos Previos

- JDK 11 o superior
- Maven
- Cuenta de Mercado Pago y token de acceso (Access Token)
- Ngrok (para pruebas locales con webhooks)

## Configuración

1. **Configurar Credenciales**:

   Asegúrate de tener tu `accessToken` de Mercado Pago. Este debe configurarse en tu aplicación. Puedes externalizarlo en un archivo de configuración como `application.properties`:

   ```properties
   mercado.pago.access.token=YOUR_ACCESS_TOKEN
   mercado.pago.url.base=https://api.mercadopago.com/v1/payments/
   ```

## Endpoints

### Crear Preferencia de Pago

Este endpoint crea una preferencia de pago en Mercado Pago.

- URL: /mercadopago/preference
- Método: POST
- Retorno: String (ID de la preferencia de pago)

```java

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
```

## Recibir Notificación de Pago

### Este endpoint recibe notificaciones de pago de Mercado Pago.

- URL: /mercadopago/notify
- Método: POST
- Parámetros: MpNotifyDTO (información de la notificación)

```java
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
```

## Discord de Mercado Pago

En este discord podras hacer consultas por tu integración

https://discord.gg/AKMPpTAd

