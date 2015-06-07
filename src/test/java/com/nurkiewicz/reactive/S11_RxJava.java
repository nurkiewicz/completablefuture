package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import com.nurkiewicz.reactive.util.Futures;
import org.junit.Test;
import rx.Observable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.fest.assertions.api.Assertions.assertThat;

public class S11_RxJava extends AbstractFuturesTest {

	public static final String MSG = "Don't panic";

	@Test
	public void shouldConvertCompletedFutureToCompletedObservable() throws Exception {
		//given
		CompletableFuture<String> future = CompletableFuture.completedFuture("Abc");

		//when
		Observable<String> observable = Futures.toObservable(future);

		//then
		assertThat(observable.toList().toBlocking().single()).containsExactly("Abc");
	}

	@Test
	public void shouldConvertFailedFutureIntoObservableWithFailure() throws Exception {
		//given
		CompletableFuture<String> future = failedFuture(new IllegalStateException(MSG));

		//when
		Observable<String> observable = Futures.toObservable(future);

		//then
		final List<String> result = observable
				.onErrorReturn(Throwable::getMessage)
				.toList()
				.toBlocking()
				.single();
		assertThat(result).containsExactly(MSG);
	}

	@Test
	public void shouldConvertObservableWithManyItemsToFutureOfList() throws Exception {
		//given
		Observable<Integer> observable = Observable.just(1, 2, 3);

		//when
		CompletableFuture<List<Integer>> future = Futures.fromObservable(observable);

		//then
		assertThat(future.get(1, SECONDS)).containsExactly(1, 2, 3);
	}

	@Test
	public void shouldConvertObservableWithSingleItemToFuture() throws Exception {
		//given
		Observable<Integer> observable = Observable.just(1);

		//when
		CompletableFuture<Integer> future = Futures.fromSingleObservable(observable);

		//then
		assertThat(future.get(1, SECONDS)).isEqualTo(1);
	}

	<T> CompletableFuture<T> failedFuture(Exception error) {
		CompletableFuture<T> future = new CompletableFuture<>();
		future.completeExceptionally(error);
		return future;
	}


}

