package com.weather.weather.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.weather.model.weather.WeatherForecast;
import com.weather.weather.model.weather.WeatherInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JsonConverter {

    private final ObjectMapper objectMapper;
    private JsonNode jsonNode;


    public WeatherInfo getWeatherInfo(String city, String responseBody, String measurement) {


        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String temperature = jsonNode.path("main").path("temp").asText();

        if(!measurement.equals(Measurement.KELVIN.getName())) {
            temperature = TemperatureConverter.converter(Measurement.valueOf(measurement), temperature);
        }

        String weatherDescription = jsonNode.path("weather").get(0).path("description").asText();


        return new WeatherInfo(city, temperature, Measurement.valueOf(measurement), weatherDescription);
    }


    public List<WeatherForecast> getWeatherForecast(String responseBody, String measurement) {

        try {
            jsonNode = objectMapper.readTree(responseBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return getWeatherForecasts(jsonNode, measurement);

    }

    private List<WeatherForecast> getWeatherForecasts(JsonNode jsonNode, String measurement) {
        JsonNode listArray = jsonNode.path("list");
        List<WeatherForecast> weatherForecasts = new ArrayList<>();

        for (JsonNode entry : listArray) {
            String temperature = entry.path("main").path("temp").asText();
            String weatherCondition = entry.path("weather").get(0).path("description").asText();
            String timestamp = entry.path("dt_txt").asText();

            if(!measurement.equals(Measurement.KELVIN.getName())) {
                temperature = TemperatureConverter.converter(Measurement.valueOf(measurement), temperature);
            }

            WeatherForecast weatherForecast = new WeatherForecast(String.valueOf(temperature), timestamp, weatherCondition);
            weatherForecasts.add(weatherForecast);
        }

        return weatherForecasts;
    }


}
