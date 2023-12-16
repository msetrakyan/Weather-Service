package com.weather.weather.service.weather.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weather.exception.CityNotFoundException;
import com.weather.weather.model.weather.WeatherForecast;
import com.weather.weather.model.weather.WeatherInfo;
import com.weather.weather.service.ip.GeoIpService;
import com.weather.weather.service.weather.WeatherService;
import com.weather.weather.util.JsonConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    private final JsonConverter jsonConverter;

    private final RestTemplate restTemplate;



    @Override
    public WeatherInfo localWeather(HttpServletRequest httpServletRequest, String measurement) throws JsonProcessingException {
        String IP = httpServletRequest.getLocalAddr();
        String city = geoIpService.getCityByIP(IP);
        return getWeatherInfo(city, measurement);
    }

    @Override
    public WeatherInfo getWeatherInfo(String city, String measurement)  {
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class, city, apiKey);
            if (response.getStatusCode().is2xxSuccessful()) {
                return jsonConverter.getWeatherInfo(city, response.getBody(), measurement);
            }
            throw new CityNotFoundException("City by name " + city + " not found");
        }

    @Override
    public List<WeatherForecast> getWeatherForecast(String city, String measurement) throws JsonProcessingException {
        HashMap<String, String> latAndLon = geoIpService.getLatAndLon(city);
        ResponseEntity<String> response = restTemplate.getForEntity(forecastUrl, String.class, latAndLon.get("lat"),latAndLon.get("lon"), apiKey);
        String jsonResponse = response.getBody();
        if(jsonResponse == null) {
            throw new CityNotFoundException("City not found");
        }
        return jsonConverter.getWeatherForecast(jsonResponse, measurement);
    }

    @Override
    public List<WeatherForecast> getLocalWeatherForecast(HttpServletRequest httpServletRequest, String measurement) throws JsonProcessingException {
        String cityByIP = geoIpService.getCityByIP(httpServletRequest.getLocalAddr());
        return getWeatherForecast(cityByIP, measurement);
    }



}


