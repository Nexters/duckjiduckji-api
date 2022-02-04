package com.nexters.duckji.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nexters.duckji.domain.Room;

@Repository
public interface RoomRepository extends ReactiveMongoRepository<Room, String> { }
