package com.nexters.duckji.config;

import java.util.concurrent.TimeUnit;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.mongodb.MongoClientSettings;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;

@Configuration
@EnableReactiveMongoAuditing
@EnableReactiveMongoRepositories(basePackages = "com.nexters.duckji.repository")
public class ReaciveMongoDBConfiguration extends AbstractReactiveMongoConfiguration {

	@Value("${spring.data.mongodb.database}")
	String databaseName;

	@Override
	protected String getDatabaseName() {
		return databaseName;
	}

	@Bean
	@Override
	public MongoClient reactiveMongoClient() {
		MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
				.retryWrites(true)
				.readPreference(ReadPreference.secondaryPreferred())
				.readConcern(ReadConcern.LOCAL)
				.writeConcern(WriteConcern.W1.withJournal(true))
				.applyToSocketSettings(builder -> {
					builder.connectTimeout(1, TimeUnit.SECONDS);
					builder.readTimeout(1, TimeUnit.SECONDS);
				})
				.applyToConnectionPoolSettings(builder -> {
					builder.minSize(10);
					builder.maxSize(500);
				})
				.codecRegistry(CodecRegistries.fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
						CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build()))
				)
				.build();

		return MongoClients.create(mongoClientSettings);
	}

	@Bean
	public ReactiveMongoTransactionManager transactionManager() {
		TransactionOptions transactionOptions = TransactionOptions.builder()
				.readPreference(ReadPreference.primary())
				.readConcern(ReadConcern.LOCAL)
				.writeConcern(WriteConcern.W1.withJournal(true))
				.maxCommitTime(3L, TimeUnit.SECONDS)
				.build();

		return new ReactiveMongoTransactionManager(reactiveMongoDbFactory(), transactionOptions);
	}

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate() {
		return new ReactiveMongoTemplate(reactiveMongoClient(), getDatabaseName());
	}
}
