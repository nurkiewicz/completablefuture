package com.blogspot.nurkiewicz.reactive.util;


import rx.Observable;

public class HeartBeat {

	public static Observable<HeartBeat> monitorServer(String name) {
		return Observable.never();
	}

}
