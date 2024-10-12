package com.nttdata.TransactionMs.config;

import com.nttdata.TransactionMs.codec.OffsetDateTimeCodec;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.nttdata.TransactionMs.codec.StringToOffsetDateTimeConverter;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {



    @Bean
    public MongoClient mongoClient() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromCodecs(new OffsetDateTimeCodec())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .codecRegistry(codecRegistry)
                .build();

        return MongoClients.create(settings);
    }

    @Bean
    public MongoCustomConversions customConversions() {
        List<StringToOffsetDateTimeConverter> converters = new ArrayList<>();
        converters.add(new StringToOffsetDateTimeConverter());  // Tu convertidor personalizado
        return new MongoCustomConversions(converters);
    }
}
