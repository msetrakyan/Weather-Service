package com.weather.weather.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weather.model.weather.WeatherForecast;
import com.weather.weather.model.weather.WeatherInfo;
import com.weather.weather.service.weather.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/{city}")
    public ResponseEntity<WeatherInfo> getWeather(@PathVariable String city) throws JsonProcessingException {
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(city);
        return ResponseEntity.ok(weatherInfo);
    }

    @GetMapping
    public ResponseEntity<WeatherInfo> getLocalWeather(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        WeatherInfo weatherInfo = weatherService.localWeather(httpServletRequest);
        return ResponseEntity.ok(weatherInfo);
    }

    @GetMapping("/forecast/{city}")
    public ResponseEntity<List<WeatherForecast>> getWeatherForecast(@PathVariable String city) throws JsonProcessingException {
        return ResponseEntity.ok(weatherService.getWeatherForecast(city));
    }

    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherForecast>> getLocalWeatherForecast(HttpServletRequest httpServletRequest) throws JsonProcessingException {
        return ResponseEntity.ok(weatherService.getLocalWeatherForecast(httpServletRequest));
    }



}
