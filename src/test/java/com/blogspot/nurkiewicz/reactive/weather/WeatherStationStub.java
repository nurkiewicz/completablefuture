package com.blogspot.nurkiewicz.reactive.weather;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Observer;
import rx.subscriptions.Subscriptions;

import javax.jms.ConnectionFactory;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class WeatherStationStub implements WeatherStation {

	private static final Logger log = LoggerFactory.getLogger(WeatherStationStub.class);

	private final Random random = new Random();

	private final long createdTime = System.currentTimeMillis();

	private final String id;

	private final float updatesPerMinute = 30 + random.nextInt(60);

	public WeatherStationStub(String id) {
		this.id = id;
	}

	@Override
	public Weather getLast() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Weather waitForNext() {
		throw new UnsupportedOperationException();
	}

	@Override
	public CompletableFuture<Weather> next() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void listen(Consumer<Weather> consumer) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConnectionFactory jmsConnection() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Observable<Weather> observations() {
		log.debug("Bulding Observable<Weather>");
		return Observable.create(subscriber -> {
			log.debug("Subscribed to Observable<Weather>");
			final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
			scheduleRandomly(subscriber, executor);
			return Subscriptions.create(() -> {
				log.debug("Unsubscribing");
				executor.shutdownNow();
			});
		});
	}

	private void scheduleRandomly(Observer<? super Weather> subscriber, ScheduledExecutorService executor) {
		executor.schedule(() -> {
			subscriber.onNext(randomWeather());
			scheduleRandomly(subscriber, executor);
		}, (int) gaussian(60_000.0f / updatesPerMinute, 500.0f), TimeUnit.MILLISECONDS);
	}

	private float gaussian(float expected, float stdDev) {
		return (float) (expected + random.nextGaussian() * stdDev);
	}

	private Weather randomWeather() {
		final double cycle = (((System.currentTimeMillis() - createdTime) % 30_000) / 30_000f) * Math.PI * 2;
		final float expectedTemperature = (float) (10 + Math.sin(cycle) * 10);
		return new Weather(id, gaussian(expectedTemperature, 1), gaussian(5, 2), gaussian(60, 5));
	}
}
