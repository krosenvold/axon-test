package com.rosenvold.axontest;

import java.io.File;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.domain.IdentifierFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfiguration {

	private final File events = new File("target/events");

	@Bean
	public EventStore eventStore() {
		events.mkdirs();
		return new FileSystemEventStore(new SimpleEventFileResolver(events));
	}

	@Bean
	public SimpleCommandBus commandBus() {
		return new SimpleCommandBus();
	}

	@Bean
	public EventBus eventBus() {
		return new SimpleEventBus();
	}


	@Bean
	public CommandGateway commandGateway(SimpleCommandBus commandBus) {
		return new DefaultCommandGateway(commandBus);
	}

	@Bean
	public IdentifierFactory identifierFactory() {
		return IdentifierFactory.getInstance();
	}
}
