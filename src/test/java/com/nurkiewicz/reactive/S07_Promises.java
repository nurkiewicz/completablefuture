package com.nurkiewicz.reactive;

import com.nurkiewicz.reactive.util.AbstractFuturesTest;
import org.junit.Ignore;
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
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Ignore
public class S07_Promises extends AbstractFuturesTest {

	private static final Logger log = LoggerFactory.getLogger(S07_Promises.class);

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

