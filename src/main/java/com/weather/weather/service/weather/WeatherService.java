package com.weather.weather.service.weather;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.weather.model.weather.WeatherForecast;
import com.weather.weather.model.weather.WeatherInfo;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface WeatherService {

    WeatherInfo localWeather(HttpServletRequest httpServletRequest, String measurement) throws JsonProcessingException;

    WeatherInfo getWeatherInfo(String city, String measurement) throws JsonProcessingException;

    List<WeatherForecast> getWeatherForecast(String  city, String measurement) throws JsonProcessingException;

    List<WeatherForecast> getLocalWeatherForecast(HttpServletRequest httpServletRequest, String measurement) throws JsonProcessingException;




}
