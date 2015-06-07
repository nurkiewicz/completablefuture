package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.stackoverflow.Question;
import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.util.concurrent.CompletableFuture;

public class S04_FlatMap extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S04_FlatMap.class);

	@Test
	public void thenApplyIsWrong() throws Exception {
		final CompletableFuture<CompletableFuture<Question>> future =
				javaQuestions()
						.thenApply(doc ->
								findMostInterestingQuestion(doc));
	}

	@Test
	public void thenAcceptIsPoor() throws Exception {
		javaQuestions().thenAccept(document -> {
			findMostInterestingQuestion(document).thenAccept(question -> {
				googleAnswer(question).thenAccept(answer -> {
					postAnswer(answer).thenAccept(status -> {
						if (status == HttpStatus.OK.value()) {
							log.debug("OK");
						} else {
							log.error("Wrong status code: {}", status);
						}
					});
				});
			});
		});
	}

	@Test
	public void thenCompose() throws Exception {
		final CompletableFuture<Document> java = javaQuestions();

		final CompletableFuture<Question> questionFuture =
				java.thenCompose(doc -> findMostInterestingQuestion(doc));

		final CompletableFuture<String> answerFuture =
				questionFuture.thenCompose(question -> googleAnswer(question));

		final CompletableFuture<Integer> httpStatusFuture =
				answerFuture.thenCompose(answer -> postAnswer(answer));

		httpStatusFuture.thenAccept(status -> {
			if (status == HttpStatus.OK.value()) {
				log.debug("OK");
			} else {
				log.error("Wrong status code: {}", status);
			}
		});
	}

	@Test
	public void chained() throws Exception {
		javaQuestions().
				thenCompose(doc -> findMostInterestingQuestion(doc)).
				thenCompose(question -> googleAnswer(question)).
				thenCompose(answer -> postAnswer(answer)).
				thenAccept(status -> {
					if (status == HttpStatus.OK.value()) {
						log.debug("OK");
					} else {
						log.error("Wrong status code: {}", status);
					}
				});
	}

	private CompletableFuture<Document> javaQuestions() {
		return CompletableFuture.supplyAsync(() ->
						client.mostRecentQuestionsAbout("java"),
				executorService
		);
	}

	private CompletableFuture<Question> findMostInterestingQuestion(Document document) {
		return CompletableFuture.completedFuture(new Question());
	}

	private CompletableFuture<String> googleAnswer(Question q) {
		return CompletableFuture.completedFuture("42");
	}

	private CompletableFuture<Integer> postAnswer(String answer) {
		return CompletableFuture.completedFuture(200);
	}

}
