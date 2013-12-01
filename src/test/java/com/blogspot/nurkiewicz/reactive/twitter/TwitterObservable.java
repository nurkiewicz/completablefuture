package com.blogspot.nurkiewicz.reactive.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.subscriptions.Subscriptions;
import twitter4j.*;

public class TwitterObservable {

	private static final Logger log = LoggerFactory.getLogger(TwitterObservable.class);

	public static Observable<Status> create() {
		log.debug("Creating Observable<Status>");
		return Observable.create(subscriber -> {
			log.debug("Connecting to Twitter stream API");
			final StatusListener listener = new StatusAdapter() {
				@Override
				public void onStatus(Status status) {
					subscriber.onNext(status);
				}

				@Override
				public void onException(Exception ex) {
					subscriber.onError(ex);
				}
			};
			final TwitterStream twitterStream = new TwitterStreamFactory().getInstance();
			twitterStream.addListener(listener);
			twitterStream.sample();
			return Subscriptions.create(() -> {
				log.debug("Closing observable");
				twitterStream.cleanUp();
			});
		});
	}

}
