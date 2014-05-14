package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class S08_ErrorHandling extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S08_ErrorHandling.class);

	@Test
	public void exceptionsShortCircuitFuture() throws Exception {
		final CompletableFuture<String> questions = questions("php");

		questions.thenApply(r -> {
			log.debug("Success!");
			return r;
		});
	}

	@Test
	public void handleExceptions() throws Exception {
		//given
		final CompletableFuture<String> questions = questions("php");

		//when
		final CompletableFuture<String> recovered = questions
				.handle((result, throwable) -> {
					if (throwable != null) {
						return "No PHP today due to: " + throwable;
					} else {
						return result.toUpperCase();
					}
				});

		//then
		log.debug("Handled: {}", recovered.get());
	}

	@Test
	public void shouldHandleExceptionally() throws Exception {
		//given
		final CompletableFuture<String> questions = questions("php");

		//when
		final CompletableFuture<String> recovered = questions
				.exceptionally(throwable -> "Sorry, try again later");

		//then
		log.debug("Done: {}", recovered.get());
	}

}

