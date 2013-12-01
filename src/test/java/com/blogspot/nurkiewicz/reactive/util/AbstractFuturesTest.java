package com.blogspot.nurkiewicz.reactive.util;

import com.blogspot.nurkiewicz.reactive.stackoverflow.*;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(AbstractFuturesTest.class);

	protected final ExecutorService executorService = Executors.newFixedThreadPool(10, threadFactory());

	@Rule
	public TestName testName = new TestName();

	private ThreadFactory threadFactory() {
		return new ThreadFactoryBuilder().setNameFormat("WJUG-pool-%d").build();
	}

	protected final StackOverflowClient client = new LoggingWrapper(
			new InjectErrorsWrapper(
					new ArtificialSleepWrapper(
							new HttpStackOverflowClient()
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
