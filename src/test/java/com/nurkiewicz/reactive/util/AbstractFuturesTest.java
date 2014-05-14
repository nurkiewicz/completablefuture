package com.nurkiewicz.reactive.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nurkiewicz.reactive.stackoverflow.ArtificialSleepWrapper;
import com.nurkiewicz.reactive.stackoverflow.FallbackStubClient;
import com.nurkiewicz.reactive.stackoverflow.HttpStackOverflowClient;
import com.nurkiewicz.reactive.stackoverflow.InjectErrorsWrapper;
import com.nurkiewicz.reactive.stackoverflow.LoggingWrapper;
import com.nurkiewicz.reactive.stackoverflow.StackOverflowClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractFuturesTest.class);

	protected final ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory("GeeCON"));

	@Rule
	public TestName testName = new TestName();

	protected ThreadFactory threadFactory(String nameFormat) {
		return new ThreadFactoryBuilder().setNameFormat(nameFormat + "-%d").build();
	}

	protected final StackOverflowClient client = new FallbackStubClient(
			new InjectErrorsWrapper(
					new LoggingWrapper(
							new ArtificialSleepWrapper(
									new HttpStackOverflowClient()
							)
					), "php"
			)
	);

	@Before
	public void logTestStart() {
		log.debug("Starting: {}", testName.getMethodName());
	}

	@After
	public void stopPool() throws InterruptedException {
		executorService.shutdown();
		executorService.awaitTermination(10, TimeUnit.SECONDS);
	}

	protected CompletableFuture<String> questions(String tag) {
		return CompletableFuture.supplyAsync(() ->
				client.mostRecentQuestionAbout(tag),
				executorService);
	}

}
