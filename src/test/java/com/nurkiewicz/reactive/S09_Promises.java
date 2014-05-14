package com.nurkiewicz.reactive;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class S09_Promises extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S09_Promises.class);

	private static final ScheduledExecutorService pool = Executors.newScheduledThreadPool(10,
			new ThreadFactoryBuilder()
					.setDaemon(true)
					.setNameFormat("FutureOps-%d")
					.build()
	);

	public static <T> CompletableFuture<T> never() {
		return new CompletableFuture<>();
	}

	public static <T> CompletableFuture<T> timeoutAfter(Duration duration) {
		final CompletableFuture<T> promise = new CompletableFuture<>();
		pool.schedule(
				() -> promise.completeExceptionally(new TimeoutException()),
				duration.toMillis(), TimeUnit.MILLISECONDS);
		return promise;
	}

	public static <T> CompletableFuture<T> delay(CompletableFuture<T> future, Duration duration) {
		final CompletableFuture<T> promise = new CompletableFuture<>();
		future.thenAccept(result -> {
			pool.schedule(() -> promise.complete(result), duration.toMillis(), TimeUnit.MILLISECONDS);
		});
		return promise;
	}

	@Test
	public void promises() throws Exception {
		final CompletableFuture<Path> future = newFilePromise();

		log.debug("New file found on desktop {}", future.get());
	}

	private CompletableFuture<Path> newFilePromise() {
		final CompletableFuture<Path> promise = new CompletableFuture<>();

		new Thread("FileSystemWatcher") {
			@Override
			public void run() {
				try {
					promise.complete(waitForNewFile());
				} catch (Exception e) {
					promise.completeExceptionally(e);
				}
			}
		}.start();

		return promise;
	}

	private Path waitForNewFile() throws IOException, InterruptedException {
		WatchService watchService = FileSystems.getDefault().newWatchService();
		final WatchKey key = Paths.
				get(System.getProperty("user.home"), "Desktop").
				register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
		while (!Thread.currentThread().isInterrupted()) {
			final List<WatchEvent<?>> events = key.pollEvents();
			if (!events.isEmpty()) {
				return (Path) events.get(0).context();
			}
			key.reset();
		}
		throw new InterruptedException();
	}

}

