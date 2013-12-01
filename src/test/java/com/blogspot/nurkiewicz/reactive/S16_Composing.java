package com.blogspot.nurkiewicz.reactive;

import com.blogspot.nurkiewicz.reactive.weather.Weather;
import com.blogspot.nurkiewicz.reactive.weather.WeatherStation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;

public class S16_Composing {

	private static final Logger log = LoggerFactory.getLogger(S16_Composing.class);

	private final Observable<Weather> warsaw = WeatherStation.find("WAW").observations();
	private final Observable<Weather> krakow = WeatherStation.find("KRA").observations();

	@Test
	public void merge() throws Exception {
		Observable.merge(warsaw, krakow).
				map(w -> w.getStationId() + ":\t" + w.getTemperature()).
				take(100).
				toBlockingObservable().
				forEach(s -> log.debug(s));
	}

	@Test
	public void zip() throws Exception {
		final Observable<Float> averageTemp = Observable.zip(
				warsaw.map(w -> w.getTemperature()),
				krakow.map(w -> w.getTemperature()),
				(w, k) -> (w + k) / 2
		);
	}

	@Test
	public void combineLatest() throws Exception {
		final Observable<Float> averageTemp = Observable.combineLatest(
				warsaw.map(w -> w.getTemperature()),
				krakow.map(w -> w.getTemperature()),
				(w, k) -> (w + k) / 2
		);
	}

}
