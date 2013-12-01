package com.blogspot.nurkiewicz.reactive.stackoverflow;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InjectErrorsWrapper implements StackOverflowClient {

	private static final Logger log = LoggerFactory.getLogger(InjectErrorsWrapper.class);

	private final StackOverflowClient target;
	private final Set<String> blackList;

	public InjectErrorsWrapper(StackOverflowClient target, String... blackList) {
		this.target = target;
		this.blackList = new HashSet<>(Arrays.asList(blackList));
	}

	@Override
	public String mostRecentQuestionAbout(String tag) {
		throwIfBlackListed(tag);
		return target.mostRecentQuestionAbout(tag);
	}

	@Override
	public Document mostRecentQuestionsAbout(String tag) {
		throwIfBlackListed(tag);
		return target.mostRecentQuestionsAbout(tag);
	}

	private void throwIfBlackListed(String tag) {
		if (blackList.contains(tag)) {
			ArtificialSleepWrapper.artificialSleep(400);
			log.warn("About to throw artifical exception due to: {}", tag);
			throw new IllegalArgumentException("Unsupported " + tag);
		}
	}

}
