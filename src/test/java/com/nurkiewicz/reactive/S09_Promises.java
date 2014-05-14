package com.nurkiewicz.reactive;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class S09_Promises extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S09_Promises.class);

	private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10,
			new ThreadFactoryBuilder()
					.setDaemon(true)
					.setNameFormat("FutureOps-%d")
					.build()
	);

	public static <T> CompletableFuture<T> never() {
		return new CompletableFuture<>();
	}

	public static <T> CompletableFuture<T> timeoutAfter(Duration duration) {
		final CompletableFuture<T> promise = new CompletableFuture<>();
		pool.schedule(
				() -> promise.completeExceptionally(new TimeoutException()),
				duration.toMillis(), TimeUnit.MILLISECONDS);
		return promise;
	}

	public static <T> CompletableFuture<T> delay(CompletableFuture<T> future, Duration duration) {
		final CompletableFuture<T> promise = new CompletableFuture<>();
		future.thenAccept(result -> {
			pool.schedule(() -> promise.complete(result), duration.toMillis(), TimeUnit.MILLISECONDS);
		});
		return promise;
	}

}

