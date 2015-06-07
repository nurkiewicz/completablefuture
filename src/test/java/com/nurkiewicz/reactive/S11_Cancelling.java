package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import com.nurkiewicz.reactive.util.InterruptibleTask;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.*;

public class S11_Cancelling extends AbstractFuturesTest {

	private static ExecutorService myThreadPool;

	@BeforeClass
	public static void init() {
		myThreadPool = Executors.newFixedThreadPool(10);
	}

	@AfterClass
	public static void close() {
		myThreadPool.shutdownNow();
	}

	@Test
	public void shouldCancelFuture() throws InterruptedException, TimeoutException {
		//given
		InterruptibleTask task = new InterruptibleTask();
		Future future = myThreadPool.submit(task);
		task.blockUntilStarted();

		//when
		future.cancel(true);

		//then
		task.blockUntilInterrupted();
	}

	@Ignore("Fails with CompletableFuture")
	@Test
	public void shouldCancelCompletableFuture() throws InterruptedException, TimeoutException {
		//given
		InterruptibleTask task = new InterruptibleTask();
		CompletableFuture<Void> future = CompletableFuture.supplyAsync(task, myThreadPool);
		task.blockUntilStarted();

		//when
		future.cancel(true);

		//then
		task.blockUntilInterrupted();
	}
}

