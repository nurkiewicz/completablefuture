package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class S02_Creating extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S02_Creating.class);

	/**
	 * Already completed future
	 */
	@Test
	public void completed() throws Exception {
		final CompletableFuture<Integer> answer =
				CompletableFuture.completedFuture(42);

		final int fortyTwo = answer.get();  //does not block
	}

	/**
	 * Built-in thread pool
	 */
	@Test
	public void supplyAsync() throws Exception {
		final CompletableFuture<String> java =
				CompletableFuture.supplyAsync(() ->
								client.mostRecentQuestionAbout("java")
				);
		log.debug("Found: '{}'", java.get());
	}

	/**
	 * Custom thread pool, equivalent (*) to submit()
	 */
	@Test
	public void supplyAsyncWithCustomExecutor() throws Exception {
		final CompletableFuture<String> java =
				CompletableFuture.supplyAsync(
						() -> client.mostRecentQuestionAbout("java"),
						executorService
				);
		log.debug("Found: '{}'", java.get());
	}

}

