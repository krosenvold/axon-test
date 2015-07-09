package com.rosenvold.axontest;

import org.axonframework.commandhandling.annotation.CommandHandler;
import org.axonframework.eventhandling.annotation.EventHandler;
import org.axonframework.eventsourcing.annotation.AbstractAnnotatedAggregateRoot;
import org.axonframework.eventsourcing.annotation.AggregateIdentifier;

public class TestAggregate extends AbstractAnnotatedAggregateRoot<String> {
	private static final long serialVersionUID = 1L;

	@AggregateIdentifier
	private String id;

	protected TestAggregate() {
		// Needed by Axon
	}

	@CommandHandler
	public TestAggregate(TestCommand command) {
		apply(new TestEvent(command.id));
	}


	@CommandHandler
	public void handle(TestCommand cmd2) {
	}

	@EventHandler
	public void on(TestEvent event) {
		this.id = event.id;
	}
}
