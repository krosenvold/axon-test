package com.rosenvold.axontest;

import java.io.Serializable;

public class TestEvent  implements Serializable {

	final String id;

	public TestEvent(String id) {
		this.id = id;
	}
}
