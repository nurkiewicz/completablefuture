package com.nurkiewicz.reactive.stackoverflow;

import com.google.common.base.Throwables;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class HttpStackOverflowClient implements StackOverflowClient {

	@Override
	public String mostRecentQuestionAbout(String tag) {
		return fetchTitleOnline(tag);
	}

	@Override
	public Document mostRecentQuestionsAbout(String tag) {
		try {
			return Jsoup.
					connect("http://stackoverflow.com/questions/tagged/" + tag).
					get();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private String fetchTitleOnline(String tag) {
		return mostRecentQuestionsAbout(tag).select("a.question-hyperlink").get(0).text();
	}

}
