package com.blogspot.nurkiewicz.reactive.stackoverflow;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingWrapper implements StackOverflowClient {

	private static final Logger log = LoggerFactory.getLogger(LoggingWrapper.class);

	private final StackOverflowClient target;

	public LoggingWrapper(StackOverflowClient target) {
		this.target = target;
	}

	@Override
	public String mostRecentQuestionAbout(String tag) {
		log.debug("Entering mostRecentQuestionAbout({})", tag);
		final String title = target.mostRecentQuestionAbout(tag);
		log.debug("Leaving mostRecentQuestionAbout({}): {}", tag, title);
		return title;
	}

	@Override
	public Document mostRecentQuestionsAbout(String tag) {
		log.debug("Entering mostRecentQuestionsAbout({})", tag);
		final Document document = target.mostRecentQuestionsAbout(tag);
		if (log.isTraceEnabled()) {
			log.trace("Leaving mostRecentQuestionsAbout({}): {}", tag, htmlExcerpt(document));
		}
		return document;
	}

	private String htmlExcerpt(Document document) {
		final String outerHtml = document.outerHtml();
		final Iterable<String> lines = Splitter.onPattern("\r?\n").split(outerHtml);
		final Iterable<String> firstLines = Iterables.limit(lines, 4);
		final String excerpt = Joiner.on(' ').join(firstLines);
		final int remainingBytes = Math.max(outerHtml.length() - excerpt.length(), 0);
		return excerpt + " [...and " + remainingBytes + " chars]";
	}
}
