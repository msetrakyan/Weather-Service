package com.weather.weather.service.ip;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weather.exception.CityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


@Service
@RequiredArgsConstructor
public class GeoIpService {


    @Value("${geolocation.api.key}")
    private String apiKey;

    @Value("${geolocation.api.url}")
    private String apiUrl;

    @Value("${geolocation.api.pos.url}")
    private String posUrl;

    @Value("${amazon.ip.api.url}")
    private String amazonGetIpUrl;

    @Value("${ninja.api.key}")
    private String ninjaApiKey;

    @Value("${ninja.geocode.api.key}")
    private String ninjaGeocodeApiUrl;

    public String getCityByIP(String IP) throws JsonProcessingException {

        try {
            IP = getPublicIpAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(apiUrl,String.class,IP, apiKey);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        String extractedCity = jsonNode.path("location").path("city").asText();

        return extractedCity;
    }

    public HashMap<String, String> getLatAndLon(String city) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Api-Key", ninjaApiKey);

        restTemplate.setInterceptors(List.of((request, body, execution) -> {
            request.getHeaders().addAll(headers);
            return execution.execute(request, body);
        }));

        ResponseEntity<String> response = restTemplate.getForEntity(ninjaGeocodeApiUrl,  String.class, city, ninjaApiKey);

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(response.getBody());

        if(jsonNode == null) {
            throw new CityNotFoundException("City not found");
        }

            JsonNode firstObject = jsonNode.get(0);

        if(firstObject == null) {
            throw new CityNotFoundException("City not found");
        }

            String lat = firstObject.path("latitude").asText();
            String lon = firstObject.path("longitude").asText();


        HashMap<String, String> latAndLon = new HashMap<>();

        latAndLon.put("lat", lat);
        latAndLon.put("lon", lat);

        return latAndLon;
    }

    private String getPublicIpAddress() throws IOException {
        URL url = new URL(amazonGetIpUrl);
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        return br.readLine().trim();
    }

}
