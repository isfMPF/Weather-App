package com.example.weather.util;

import lombok.Data;

@Data
public class WeatherInfo {
    private Double temp;        //температура
    private Double feelsLike;   //ощущается как
    private Double wind;        //ветер (км/ч)
    private int humidity;       //влажность в %
}
