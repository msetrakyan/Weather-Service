package com.weather.weather.service.weather.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weather.exception.CityNotFoundException;
import com.weather.weather.model.weather.WeatherForecast;
import com.weather.weather.model.weather.WeatherInfo;
import com.weather.weather.service.ip.GeoIpService;
import com.weather.weather.service.weather.WeatherService;
import com.weather.weather.util.Measurement;
import com.weather.weather.util.WeatherCondition;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.query.JSqlParserUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherServiceImpl implements WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

    @Value("${weather.api.url}")
    private String apiUrl;

    @Value("${weather.api.forecast.url}")
    private String forecastUrl;

    private final GeoIpService geoIpService;



    @Override
    public WeatherInfo localWeather(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String IP = httpServletRequest.getLocalAddr();
        String city = geoIpService.getCityByIP(IP);
        return getWeatherInfo(city);
    }

    @Override
    public WeatherInfo getWeatherInfo(String city)  {

            RestTemplate restTemplate = new RestTemplate();

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class, city, apiKey);

            WeatherInfo weatherInfo = new WeatherInfo();

            if (response.getStatusCode().is2xxSuccessful()) {

                String jsonResponse = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;

                try {
                    jsonNode = objectMapper.readTree(jsonResponse);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }

                String temperature = jsonNode.path("main").path("temp").asText();
                String weatherDescription = jsonNode.path("weather").get(0).path("description").asText();

                return new WeatherInfo(city, temperature, Measurement.KELVIN, weatherDescription);
            }

            throw new CityNotFoundException("City by name " + city + " not found");
        }

    @Override
    public List<WeatherForecast> getWeatherForecast(String city) throws JsonProcessingException {

        HashMap<String, String> latAndLon = geoIpService.getLatAndLon(city);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(forecastUrl, String.class, latAndLon.get("lat"),latAndLon.get("lon"), apiKey);


        String jsonResponse = response.getBody();

        if(jsonResponse == null) {
            throw new CityNotFoundException("City not found");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonData = objectMapper.readTree(jsonResponse);

        JsonNode listArray = jsonData.path("list");
        List<WeatherForecast> weatherForecasts = new ArrayList<>();

        for (JsonNode entry : listArray) {
            String temperature = entry.path("main").path("temp").asText();
            String weatherCondition = entry.path("weather").get(0).path("description").asText();
            String timestamp = entry.path("dt_txt").asText();

            WeatherForecast weatherForecast = new WeatherForecast(String.valueOf(temperature), timestamp, weatherCondition);
            weatherForecasts.add(weatherForecast);
        }

        return weatherForecasts;
    }

    @Override
    public List<WeatherForecast> getLocalWeatherForecast(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        String cityByIP = geoIpService.getCityByIP(httpServletRequest.getLocalAddr());
        return getWeatherForecast(cityByIP);
    }



}


