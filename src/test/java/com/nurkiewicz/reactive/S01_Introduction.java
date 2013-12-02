package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.stackoverflow.LoadFromStackOverflowTask;
import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Sources: <a href="https://github.com/nurkiewicz/wjug-reactive">github.com/nurkiewicz/wjug-reactive</a>
 */
public class S01_Introduction extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S01_Introduction.class);

	@Test
	public void blockingCall() throws Exception {
		final String title = client.mostRecentQuestionAbout("java");
		log.debug("Most recent Java question: '{}'", title);
	}

	@Test
	public void executorService() throws Exception {
		final Callable<String> task = new Callable<String>() {
			@Override
			public String call() throws Exception {
				return client.mostRecentQuestionAbout("java");
			}
		};
		final Future<String> javaQuestionFuture = executorService.submit(task);
		final String javaQuestion = javaQuestionFuture.get();
		log.debug("Found: '{}'", javaQuestion);
	}

	@Test
	public void waitForFirstOrAll() throws Exception {
		final Future<String> java = findQuestionsAbout("java");
		final Future<String> scala = findQuestionsAbout("scala");

		//???
	}

	@Test
	public void runWhenFirstFinished() throws Exception {
		final Future<String> java = findQuestionsAbout("java");
		final Future<String> scala = findQuestionsAbout("scala");
		//???
	}

	private Future<String> findQuestionsAbout(String tag) {
		final Callable<String> task = new LoadFromStackOverflowTask(client, tag);
		return executorService.submit(task);
	}

}

