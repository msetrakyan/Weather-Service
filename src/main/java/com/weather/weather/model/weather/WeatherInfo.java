package com.weather.weather.model.weather;

import com.weather.weather.util.Measurement;
import com.weather.weather.util.WeatherCondition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeatherInfo {

    private String city;

    private String temperature;

    private Measurement measurement;

    private String weatherCondition;

}
