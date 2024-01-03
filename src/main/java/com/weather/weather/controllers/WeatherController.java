package com.weather.weather.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weather.model.weather.WeatherForecast;
import com.weather.weather.model.weather.WeatherInfo;
import com.weather.weather.service.weather.WeatherService;
import com.weather.weather.util.Measurement;
import jakarta.annotation.security.RolesAllowed;
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

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping("/{city}")
    public ResponseEntity<WeatherInfo> getWeather(@PathVariable String city,
    @RequestParam(required = false) String measurement) throws JsonProcessingException {
        if (measurement == null) {
            measurement = "KELVIN";
        }
        WeatherInfo weatherInfo = weatherService.getWeatherInfo(city, measurement);
        return ResponseEntity.ok(weatherInfo);
    }

    @RolesAllowed({"USER", "ADMIN"})
    @GetMapping
    public ResponseEntity<WeatherInfo> getLocalWeather(HttpServletRequest httpServletRequest,
                                                        @RequestParam(required = false) String measurement) throws JsonProcessingException {
        if (measurement == null) {
            measurement = "KELVIN";
        }
        return ResponseEntity.ok(weatherService.localWeather(httpServletRequest, measurement));
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/forecast/{city}")
    public ResponseEntity<List<WeatherForecast>> getWeatherForecast(@PathVariable String city,
                                                                    @RequestParam(required = false) String measurement) throws JsonProcessingException {
        if (measurement == null) {
            measurement = "KELVIN";
        }
        return ResponseEntity.ok(weatherService.getWeatherForecast(city,measurement));
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherForecast>> getLocalWeatherForecast(HttpServletRequest httpServletRequest,
    @RequestParam(required = false) String measurement) throws JsonProcessingException {
        if (measurement == null) {
            measurement = "KELVIN";
        }
        return ResponseEntity.ok(weatherService.getLocalWeatherForecast(httpServletRequest, measurement));
    }



}
