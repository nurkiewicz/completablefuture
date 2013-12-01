package com.blogspot.nurkiewicz.reactive.weather;

import rx.Observable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface WeatherStation {

	static WeatherStation find(String stationId) {
		return new WeatherStationStub(stationId);
	}

	Weather getLast();

	Weather waitForNext();

	CompletableFuture<Weather> next();

	void listen(Consumer<Weather> consumer);

	javax.jms.ConnectionFactory jmsConnection();

	Observable<Weather> observations();

}
