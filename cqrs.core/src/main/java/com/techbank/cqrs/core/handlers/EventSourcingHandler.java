package com.techbank.cqrs.core.handlers;

import com.techbank.cqrs.core.domain.AggregateRoot;

import java.util.UUID;

public interface EventSourcingHandler<T> {
    void save(AggregateRoot aggregateRoot);

    void republishEvents();
    T getById(String id);
}
