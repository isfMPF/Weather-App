package com.example.weather.service;

import com.example.weather.util.WeatherInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;


@Service
public class WeatherIntegration {

    private final String API_KEY="06b18d156b6d461998e84920251404";


    public WeatherInfo getWeather(String query, LocalDate date) throws IOException, InterruptedException {

        try {
            HttpClient client = HttpClient.newHttpClient();
            WeatherInfo weatherInfo = new WeatherInfo();

            if(date == null){

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://api.weatherapi.com/v1/current.json?key=%s&q=%s&aqi=no" .formatted(API_KEY,query)))
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.body());
                JsonNode current = root.get("current");

                double temp = current.get("temp_c").asDouble();
                double feel = current.get("feelslike_c").asDouble();
                double wind = current.get("wind_kph").asDouble();
                int humidity = (int) current.get("humidity").asInt();
                weatherInfo.setTemp(temp);
                weatherInfo.setFeelsLike(feel);
                weatherInfo.setWind(wind);
                weatherInfo.setHumidity(humidity);

            }else {

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("http://api.weatherapi.com/v1/history.json?key=%s&q=%s&dt=%s" .formatted(API_KEY,query,date)))
                        .header("Accept", "application/json")
                        .build();

                HttpResponse<String> response = client.send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.body());
                JsonNode forecastDayNode = rootNode.path("forecast").path("forecastday").get(0);
                double temp = forecastDayNode.path("day").path("maxtemp_c").asDouble();
                double feel = forecastDayNode.path("day").path("avgtemp_c").asDouble();
                double wind = forecastDayNode.path("day").path("maxwind_kph").asDouble();
                int humidity = forecastDayNode.path("day").path("avghumidity").asInt();
                weatherInfo.setTemp(temp);
                weatherInfo.setFeelsLike(feel);
                weatherInfo.setWind(wind);
                weatherInfo.setHumidity(humidity);

            }
            return weatherInfo;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
