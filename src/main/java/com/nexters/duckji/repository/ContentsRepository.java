package com.nexters.duckji.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nexters.duckji.domain.Content;

@Repository
public interface ContentsRepository extends ReactiveMongoRepository<Content, String> {
}
