package com.example.aravi.weather_app;

/*************************************************************************************************************************************************************
 Program: Plain Old Java Object (POJO) class for storing weather details
 Author: Aravind
 Date of creation: 25-Oct-2018
 *************************************************************************************************************************************************************/


//POJO class
public class Weather {

    private String cityInput;
    private String cityOutput;
    private String temperatureValue;
    private String minTemperature;
    private String maxTemperature;
    private String weatherValue;
    private String weatherDescValue;
    private String humidityValue;
    private String humidityText;
    private String cloudValue;
    private String cloudText;
    private boolean isDay;
    private boolean isNight;

    public Weather() {
        init();
    }

    public void init(){
        this.cityInput = cityInput;
        this.cityOutput = cityOutput;
        this.temperatureValue = temperatureValue;
        this.minTemperature = minTemperature;
        this.maxTemperature = maxTemperature;
        this.weatherValue = weatherValue;
        this.weatherDescValue = weatherDescValue;
        this.humidityValue = humidityValue;
        this.humidityText = humidityText;
        this.cloudValue = cloudValue;
        this.cloudText = cloudText;
        this.isDay = isDay;
        this.isNight = isNight;
    }

    public String getCityInput() {
        return cityInput;
    }

    public void setCityInput(String cityInput) {
        this.cityInput = cityInput;
    }

    public String getCityOutput() {
        return cityOutput;
    }

    public void setCityOutput(String cityOutput) {
        this.cityOutput = cityOutput;
    }

    public String getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(String temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getWeatherValue() {
        return weatherValue;
    }

    public void setWeatherValue(String weatherValue) {
        this.weatherValue = weatherValue;
    }

    public String getWeatherDescValue() {
        return weatherDescValue;
    }

    public void setWeatherDescValue(String weatherDescValue) {
        this.weatherDescValue = weatherDescValue;
    }

    public String getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(String humidityValue) {
        this.humidityValue = humidityValue;
    }

    public String getHumidityText() {
        return humidityText;
    }

    public void setHumidityText(String humidityText) {
        this.humidityText = humidityText;
    }

    public String getCloudValue() {
        return cloudValue;
    }

    public void setCloudValue(String cloudValue) {
        this.cloudValue = cloudValue;
    }

    public String getCloudText() {
        return cloudText;
    }

    public void setCloudText(String cloudText) {
        this.cloudText = cloudText;
    }

    public boolean isDay() {
        return isDay;
    }

    public void setDay(boolean day) {
        isDay = day;
    }

    public boolean isNight() {
        return isNight;
    }

    public void setNight(boolean night) {
        isNight = night;
    }
}
