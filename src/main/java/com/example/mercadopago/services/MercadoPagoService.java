package com.example.mercadopago.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.mercadopago.dto.BacksUrlDTO;
import com.example.mercadopago.dto.PagoDTO;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.preference.Preference;

@Service
public class MercadoPagoService {

    @Value("${meli.accesToken}")
    private String accesToken;

    private String urlMeli = "https://api.mercadopago.com/v1/payments/";

    public String createPreference(String title, Integer quantity, String currencyId, BigDecimal unitPrice,
            BacksUrlDTO backsUrl, String notificationUrl) throws MPException, MPApiException {

        // Configura el token de acceso para MercadoPago con la variable 'accesToken'.
        MercadoPagoConfig.setAccessToken(accesToken);

        // Crear un objeto 'PreferenceItemRequest' para definir el artículo de la
        // preferencia de pago.
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(title) // Título del artículo.
                .quantity(quantity) // Cantidad del artículo.
                .currencyId(currencyId) // Moneda en la que se hará la transacción.
                .unitPrice(unitPrice) // Precio unitario del artículo.
                .build();

        // Crear una lista para contener los artículos de la preferencia de pago.
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest); // Añadir el artículo a la lista.

        // Crear un objeto 'PreferenceBackUrlsRequest' para definir las URLs de retorno.
        PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                .success(backsUrl.getSuccess()) // URL a la que se redirige en caso de éxito.
                .pending(backsUrl.getPending()) // URL a la que se redirige en caso de que el pago esté pendiente.
                .failure(backsUrl.getFailure()) // URL a la que se redirige en caso de fallo.
                .build();

        // Crear un objeto 'PreferenceRequest' para definir la preferencia de pago.
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(items) // Lista de artículos.
                .backUrls(backUrls) // URLs de retorno.
                .autoReturn("approved") // Configurar retorno automático en caso de que el pago sea aprobado.
                .notificationUrl(notificationUrl) // URL para notificaciones de pago.
                .build();

        // Crear un cliente de preferencias para interactuar con la API de MercadoPago.
        PreferenceClient preferenceClient = new PreferenceClient();

        // Crear la preferencia de pago usando el cliente de preferencias.
        Preference preference = preferenceClient.create(preferenceRequest);

        // Retornar el punto de inicio de la preferencia (URL de redirección para
        // iniciar el pago).
        return preference.getInitPoint();
    }

    public PagoDTO getPago(String id_payment) {
        // Crear una instancia de RestTemplate para realizar solicitudes HTTP.
        RestTemplate restTemplate = new RestTemplate();

        // Crear un objeto HttpHeaders para configurar los encabezados de la solicitud.
        HttpHeaders httpHeaders = new HttpHeaders();
        // Configurar el encabezado de autorización con el token de acceso.
        httpHeaders.set("Authorization", "Bearer " + accesToken);

        // Crear un objeto HttpEntity con los encabezados configurados.
        HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

        // Realizar una solicitud GET a la URL de MercadoPago para obtener la
        // información del pago.
        // La URL se construye concatenando 'urlMeli' con el 'id_payment'.
        // 'response' contendrá la respuesta de la solicitud, mapeada al tipo PagoDTO.
        ResponseEntity<PagoDTO> response = restTemplate.exchange(
                urlMeli + id_payment, // URL de la solicitud
                HttpMethod.GET, // Método HTTP GET
                entity, // Entidad que contiene los encabezados
                PagoDTO.class // Clase de respuesta esperada
        );

        // Retornar el cuerpo de la respuesta, que contiene la información del pago en
        // forma de PagoDTO.
        return response.getBody();
    }

}
