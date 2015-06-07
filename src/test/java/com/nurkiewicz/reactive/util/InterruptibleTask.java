package com.nurkiewicz.reactive.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class InterruptibleTask implements Runnable, Supplier<Void> {

	private final CountDownLatch started = new CountDownLatch(1);
	private final CountDownLatch interrupted = new CountDownLatch(1);

	@Override
	public Void get() {
		run();
		return null;
	}

	@Override
	public void run() {
		started.countDown();
		try {
			Thread.sleep(10_000);
		} catch (InterruptedException ignored) {
			interrupted.countDown();
		}
	}

	public void blockUntilStarted() throws InterruptedException {
		started.await();
	}

	public void blockUntilInterrupted() throws InterruptedException, TimeoutException {
		if (!interrupted.await(1, TimeUnit.SECONDS)) {
			throw new TimeoutException("Not interrupted within 1 second");
		}
	}
}