package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class S07_AsyncCallback extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S07_AsyncCallback.class);

	protected final ExecutorService poolAlpha = Executors.newFixedThreadPool(10, threadFactory("Alpha"));
	protected final ExecutorService poolBeta  = Executors.newFixedThreadPool(10, threadFactory("Beta"));
	protected final ExecutorService poolGamma = Executors.newFixedThreadPool(10, threadFactory("Gamma"));

	@Test
	public void whichThreadInvokesCallbacks() throws Exception {
		final CompletableFuture<String> java = CompletableFuture
				.supplyAsync(() -> client.mostRecentQuestionAbout("java"), poolAlpha);
		final CompletableFuture<String> scala = CompletableFuture
				.supplyAsync(() -> client.mostRecentQuestionAbout("scala"), poolBeta);

		final CompletableFuture<String> first = java.applyToEither(scala, question -> {
			log.debug("First: {}", question);
			return question.toUpperCase();
		});

		first.thenAccept(q -> log.debug("Sync: {}", q));
		first.thenAcceptAsync(q -> log.debug("Async: {}", q));
		first.thenAcceptAsync(q -> log.debug("Async (pool): {}", q), poolGamma);
		first.get();        //block
	}

}

