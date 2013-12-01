package com.blogspot.nurkiewicz.reactive;

import com.blogspot.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class S06_AllAny extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S06_AllAny.class);

	@Test
	public void allOf() throws Exception {
		final CompletableFuture<String> java = questions("java");
		final CompletableFuture<String> scala = questions("scala");
		final CompletableFuture<String> clojure = questions("clojure");
		final CompletableFuture<String> groovy = questions("groovy");

		final CompletableFuture<Void> allCompleted = CompletableFuture.allOf(
				java, scala, clojure, groovy
		);

		allCompleted.thenRun(() -> {
			try {
				log.debug("Loaded: {}", java.get());
				log.debug("Loaded: {}", scala.get());
				log.debug("Loaded: {}", clojure.get());
				log.debug("Loaded: {}", groovy.get());
			} catch (InterruptedException | ExecutionException e) {
				log.error("", e);
			}
		});
	}

	@Test
	public void anyOf() throws Exception {
		final CompletableFuture<String> java = questions("java");
		final CompletableFuture<String> scala = questions("scala");
		final CompletableFuture<String> clojure = questions("clojure");
		final CompletableFuture<String> groovy = questions("groovy");

		final CompletableFuture<Object> firstCompleted = CompletableFuture.anyOf(
				java, scala, clojure, groovy
		);

		firstCompleted.thenAccept((Object result) -> {
			log.debug("First: {}", result);
		});
	}

}

