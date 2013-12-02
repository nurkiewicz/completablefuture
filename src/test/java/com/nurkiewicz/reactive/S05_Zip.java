package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class S05_Zip extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S05_Zip.class);

	@Test
	public void thenCombine() throws Exception {
		final CompletableFuture<String> java = questions("java");
		final CompletableFuture<String> scala = questions("scala");

		final CompletableFuture<Integer> both = java.
				thenCombine(scala, (String javaTitle, String scalaTitle) ->
						javaTitle.length() + scalaTitle.length()
				);

		both.thenAccept(length -> log.debug("Total length: {}", length));
	}

	@Test
	public void either() throws Exception {
		final CompletableFuture<String> java = questions("java");
		final CompletableFuture<String> scala = questions("scala");

		final CompletableFuture<String> both = java.
				applyToEither(scala, title -> title.toUpperCase());

		both.thenAccept(title -> log.debug("First: {}", title));
	}


}

