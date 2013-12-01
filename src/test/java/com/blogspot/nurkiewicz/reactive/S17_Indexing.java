package com.blogspot.nurkiewicz.reactive;

import com.blogspot.nurkiewicz.reactive.weather.Weather;
import com.blogspot.nurkiewicz.reactive.weather.WeatherStation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.util.Timestamped;

import java.util.Date;

public class S17_Indexing {

	private static final Logger log = LoggerFactory.getLogger(S17_Indexing.class);

	@Test
	public void mapWithIndex() throws Exception {
		Observable.from("A", "B", "C", "D", "E").
				mapWithIndex((s, i) -> s + i).
				subscribe(s -> log.debug(s));
	}

	@Test
	public void timestamped() throws Exception {
		final Observable<Timestamped<Weather>> waw = WeatherStation.find("WAW").observations().
				timestamp();
		waw.
				map((Timestamped<Weather> stamped) ->
						new Date(stamped.getTimestampMillis()) + "\t" + stamped.getValue().getTemperature()).
				take(100).
				toBlockingObservable().
				forEach(s -> log.debug(s));
	}

	@Test
	public void cache() throws Exception {
		final Observable<Weather> cached = WeatherStation.find("WAW").
				observations().
				cache();

		cached.
				timestamp().
				map(t -> new Date(t.getTimestampMillis()) + "\t"+ t.getValue().getTemperature()).
				take(5).
				toBlockingObservable().
				forEach(s -> log.debug(s));

		cached.
				timestamp().
				map(t -> new Date(t.getTimestampMillis()) + "\t"+ t.getValue().getTemperature()).
				take(5).
				toBlockingObservable().
				forEach(s -> log.debug(s));
	}

}
