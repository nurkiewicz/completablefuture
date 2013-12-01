package com.blogspot.nurkiewicz.reactive.weather;

import com.google.common.base.Objects;

public class Weather {

	private final String stationId;
	private final float temperature;
	private final float wind;
	private final float humidity;

	public Weather(String stationId, float temperature, float wind, float humidity) {
		this.stationId = stationId;
		this.temperature = temperature;
		this.wind = wind;
		this.humidity = humidity;
	}

	public String getStationId() {
		return stationId;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getWind() {
		return wind;
	}

	public float getHumidity() {
		return humidity;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this).
				add("stationId", stationId).
				add("temperature", temperature).
				add("wind", wind).
				add("humidity", humidity).
				toString();
	}
}
