package com.rosenvold.axontest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.commandhandling.gateway.DefaultCommandGateway;
import org.axonframework.domain.IdentifierFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.eventstore.EventStore;
import org.axonframework.eventstore.fs.FileSystemEventStore;
import org.axonframework.eventstore.fs.SimpleEventFileResolver;
import org.axonframework.serializer.Serializer;
import org.axonframework.serializer.xml.CompactDriver;
import org.axonframework.serializer.xml.XStreamSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thoughtworks.xstream.XStream;

@Configuration
public class AxonConfiguration {

	@Bean
	public Serializer serializer(){
		return new XStreamSerializer(new XStream(new CompactDriver()));
	}

	@Bean
	public EventStore eventStore(Serializer serializer) throws IOException {
		final Path events = Files.createTempDirectory("events");
		return new FileSystemEventStore(serializer, new SimpleEventFileResolver(events.toFile()));
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
