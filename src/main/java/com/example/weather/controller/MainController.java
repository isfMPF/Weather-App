package com.example.weather.controller;

import com.example.weather.service.WeatherIntegration;
import com.example.weather.util.WeatherInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDate;

@Controller
@RequestMapping("/weather")
public class MainController {

    @Autowired
    private WeatherIntegration weatherIntegration;

    @GetMapping
    public String show(Model model) throws IOException, InterruptedException {
        return "weather/weather";
    }

    @PostMapping("/query")
    public String weatherCity(@RequestParam String query,
                              @RequestParam(required = false) LocalDate date,
                              Model model) throws IOException, InterruptedException {

        try {
            model.addAttribute("weather",weatherIntegration.getWeather(query,date));
            model.addAttribute("query",query);
            model.addAttribute("date",date);
            return "weather/weather";
        } catch (Exception e) {
            model.addAttribute("error", "Город не найден или произошла ошибка при получении данных.");
            return "weather/weather";
        }

    }

}
