package com.nurkiewicz.reactive;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class S10_AsyncHttp extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S10_AsyncHttp.class);

	private static final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

	@AfterClass
	public static void closeClient() {
		asyncHttpClient.close();
	}

	@Test
	public void asyncHttpWithCallbacks() throws Exception {
		loadTag(
				"java",
				response -> log.debug("Got: {}", response),
				throwable -> log.error("Mayday!", throwable)
		);
		TimeUnit.SECONDS.sleep(5);
	}

	public void loadTag(
			String tag,
			Consumer<String> onSuccess,
			Consumer<Throwable> onError) throws IOException {
		asyncHttpClient
				.prepareGet("http://stackoverflow.com/questions/tagged/" + tag)
				.execute(
						new AsyncCompletionHandler<Void>() {

							@Override
							public Void onCompleted(Response response) throws Exception {
								onSuccess.accept(response.getResponseBody());
								return null;
							}

							@Override
							public void onThrowable(Throwable t) {
								onError.accept(t);
							}
						}
				);
	}

	public CompletableFuture<String> loadTag(String tag) throws IOException {
		final CompletableFuture<String> promise = new CompletableFuture<>();
		asyncHttpClient.prepareGet("http://stackoverflow.com/questions/tagged/" + tag).execute(
				new AsyncCompletionHandler<Void>() {

					@Override
					public Void onCompleted(Response response) throws Exception {
						promise.complete(response.getResponseBody());
						return null;
					}

					@Override
					public void onThrowable(Throwable t) {
						promise.completeExceptionally(t);
					}
				}
		);
		return promise;
	}
}

