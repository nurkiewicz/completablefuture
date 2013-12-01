package com.blogspot.nurkiewicz.reactive;

import com.blogspot.nurkiewicz.reactive.twitter.TwitterObservable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import twitter4j.Status;

import java.util.concurrent.TimeUnit;

public class S14_TwitterDemo {

	private static final Logger log = LoggerFactory.getLogger(S14_TwitterDemo.class);

	private final Observable<Status> twitter = TwitterObservable.create();

	/**
	 * map/filter/mapMany/take
	 *
	 * @throws Exception
	 */
	@Test
	public void mapAndFilterButNoSubscriptions() throws Exception {
		final Observable<String> reTweets = twitter.
				map(status -> status.getText()).
				filter(text -> text.startsWith("RT")).
				take(1000);
	}

	@Test
	public void nonBlockingSubscription() throws Exception {
		final Observable<String> reTweets = twitter.
				map(status -> status.getText()).
				filter(text -> text.startsWith("RT")).
				take(1000);

		reTweets.subscribe(s -> log.debug(s));
		TimeUnit.MINUTES.sleep(1);
	}

	@Test
	public void blockingSubscription() throws Exception {
		final Observable<String> tweets = twitter.
				map(status -> status.getText()).
				filter(text -> text.startsWith("RT")).
				take(100);

		tweets.
				toBlockingObservable().
				forEach(s -> log.debug(s));
	}

	@Test
	public void tweetsPerSecond() throws Exception {
		final Observable<Integer> tweets = twitter.
				buffer(1, TimeUnit.SECONDS).
				map(list -> list.size()).
				take(10);

		tweets.
				toBlockingObservable().
				forEach(c -> log.debug("Tweets/s: {}", c));

	}
}