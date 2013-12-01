package com.blogspot.nurkiewicz.reactive;

import com.blogspot.nurkiewicz.reactive.weather.Weather;
import com.blogspot.nurkiewicz.reactive.weather.WeatherStation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

import java.util.concurrent.TimeUnit;

public class S11_Introduction {

	private static final Logger log = LoggerFactory.getLogger(S11_Introduction.class);

	final WeatherStation station = WeatherStation.find("WAW");
	final Observable<Weather> observable = station.observations();

	/**
	 * Observable.subscribe()
	 */
	@Test
	public void subscribingToObservable() throws InterruptedException {
		observable.subscribe((Weather w) ->
				log.debug("Weather changed: {}", w.getTemperature()));
		TimeUnit.SECONDS.sleep(100);
	}

	/**
	 * - map() and filter()
	 * - show immutability
	 */
	@Test
	public void mapAndFilter() {
		final Observable<Float> temperatures = observable.map(weather -> weather.getTemperature());
		final Observable<Float> highTemp = temperatures.filter(t -> t > 30.0);
		final Observable<Float> lowTemp = temperatures.filter(t -> t < 15.0);
	}

}

