package com.blogspot.nurkiewicz.reactive.stackoverflow;

import org.jsoup.nodes.Document;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ArtificialSleepWrapper implements StackOverflowClient {

	private static final Random RANDOM = new Random();

	private final StackOverflowClient target;

	public ArtificialSleepWrapper(StackOverflowClient target) {
		this.target = target;
	}

	@Override
	public String mostRecentQuestionAbout(String tag) {
		artificialSleep(1000);
		return target.mostRecentQuestionAbout(tag);
	}

	@Override
	public Document mostRecentQuestionsAbout(String tag) {
		artificialSleep(1000);
		return target.mostRecentQuestionsAbout(tag);
	}

	protected static void artificialSleep(int expected) {
		try {
			TimeUnit.MILLISECONDS.sleep((long) (expected + RANDOM.nextGaussian() * expected / 2));
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
