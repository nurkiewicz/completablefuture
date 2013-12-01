package com.blogspot.nurkiewicz.reactive;

import com.blogspot.nurkiewicz.reactive.twitter.TwitterObservable;
import com.blogspot.nurkiewicz.reactive.util.HeartBeat;
import com.blogspot.nurkiewicz.reactive.weather.WeatherStation;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.Status;

import java.util.concurrent.TimeUnit;

public class S15_Filtering {

	private static final Logger log = LoggerFactory.getLogger(S15_Filtering.class);

	/**
	 * sample/throttleFirst/throttleLast
	 */
	@Test
	public void sample() throws Exception {
		final Observable<Status> observable = TwitterObservable.create();

		observable.
				sample(1, TimeUnit.SECONDS).
				map(status -> status.getText()).
				take(10).
				toBlockingObservable().
				forEach(s -> log.debug(s));
	}

	@Test
	public void distinctUntilChanged() throws Exception {
		WeatherStation.find("WAW").observations().
				map(w -> (int) w.getTemperature()).
				distinctUntilChanged().
				map(i -> i.toString()).
				toBlockingObservable().
				forEach(s -> log.debug(s));
	}

	@Test
	public void throttleWithTimeout() throws Exception {
		WeatherStation.find("WAW").observations().
				throttleWithTimeout(1, TimeUnit.SECONDS).
				map(w -> w.getTemperature()).
 				toBlockingObservable().
				forEach(s -> log.debug("Temp: {}", s));
	}

	@Test
	public void timeout() throws Exception {
		final Observable<HeartBeat> observable = HeartBeat.monitorServer("foo");
		observable.
				timeout(1, TimeUnit.SECONDS).
				toBlockingObservable().
				forEach(h -> log.debug("Heart beat: {}", h));
	}

}
