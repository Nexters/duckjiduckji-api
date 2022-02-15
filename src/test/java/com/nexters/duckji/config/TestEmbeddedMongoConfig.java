package com.nexters.duckji.config;

import java.io.IOException;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.util.SocketUtils;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@TestConfiguration
@EnableReactiveMongoRepositories(basePackages = "com.nexters.duckji",
		reactiveMongoTemplateRef = "testMongoTemplate")
public class TestEmbeddedMongoConfig {
	private MongodExecutable executable;
	private static final String MONGO_DB_URL = "mongodb://%s:%d";
	private static final String MONGO_DB_NAME = "embeded_duckji";

	@Bean(name = "testMongoTemplate")
	@Primary
	public ReactiveMongoTemplate mongodExecutable() throws IOException {
		return new ReactiveMongoTemplate(testReactiveMongoClient(), MONGO_DB_NAME);
	}

	@Bean(name = "testReactiveMongoClient")
	@Primary
	public MongoClient testReactiveMongoClient() throws IOException {
		String ip = "localhost";
		int randomPort = SocketUtils.findAvailableTcpPort();

		ImmutableMongodConfig mongoDConfig = MongodConfig.builder()
				.version(Version.Main.V3_4)
				.net(new Net(ip, randomPort, Network.localhostIsIPv6()))
				.build();

		MongodStarter starter = MongodStarter.getDefaultInstance();
		executable = starter.prepare(mongoDConfig);
		executable.start();

		return MongoClients.create(String.format(MONGO_DB_URL, ip, randomPort));
	}

}
