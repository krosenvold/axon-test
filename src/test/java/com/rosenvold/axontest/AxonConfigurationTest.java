package com.rosenvold.axontest;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.annotation.AggregateAnnotationCommandHandler;
import org.axonframework.commandhandling.annotation.AnnotationCommandTargetResolver;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.annotation.ClasspathParameterResolverFactory;
import org.axonframework.common.annotation.MultiParameterResolverFactory;
import org.axonframework.common.annotation.ParameterResolverFactory;
import org.axonframework.common.annotation.SpringBeanParameterResolverFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.EventSourcedAggregateRoot;
import org.axonframework.eventsourcing.EventSourcingRepository;
import org.axonframework.eventstore.EventStore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AxonConfigurationTest {

	@Test
	public void startContext() throws IOException {
		try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AxonConfiguration.class);
			 Closeable ignore = subscribeTo(TestAggregate.class, ctx)) {
			CommandGateway cg = ctx.getBean(CommandGateway.class);
			cg.sendAndWait(new TestCommand("1"));
		}
	}


	protected <T extends EventSourcedAggregateRoot<?>> Closeable subscribeTo(Class<T> clazz, ApplicationContext applicationContext) {
		EventBus eventBus = applicationContext.getBean(EventBus.class);
		EventStore eventStore = applicationContext.getBean(EventStore.class);
		CommandBus cBus = applicationContext.getBean(CommandBus.class);

		EventSourcingRepository<T> repository = new EventSourcingRepository<>(clazz, eventStore);
		repository.setEventBus(eventBus);

		AggregateAnnotationCommandHandler<T> handler = new AggregateAnnotationCommandHandler<>(
				clazz,
				repository,
				new AnnotationCommandTargetResolver(),
				springInjector(clazz, applicationContext));

		List<Runnable> unsubscriptions = new ArrayList<>();

		for (String cmd : handler.supportedCommands()) {
			cBus.subscribe(cmd, handler);
			unsubscriptions.add(() -> cBus.unsubscribe(cmd, handler));
		}

		return () -> unsubscriptions.forEach(Runnable::run);
	}

	public static ParameterResolverFactory springInjector(Class clazz, ApplicationContext applicationContext) {
		SpringBeanParameterResolverFactory factory = new SpringBeanParameterResolverFactory();
		factory.setApplicationContext(applicationContext);
		return MultiParameterResolverFactory.ordered(ClasspathParameterResolverFactory.forClass(clazz), factory);
	}

}