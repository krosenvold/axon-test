package com.rosenvold.axontest;

import org.axonframework.commandhandling.annotation.TargetAggregateIdentifier;

public class TestCommand {

	@TargetAggregateIdentifier
	final String id;

	public TestCommand(String id) {
		this.id = id;
	}
}
