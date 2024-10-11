package com.nttdata.TransactionMs.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeCodec implements Codec<OffsetDateTime> {

    @Override
    public void encode(BsonWriter writer, OffsetDateTime value, EncoderContext encoderContext) {
        writer.writeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    }

    @Override
    public OffsetDateTime decode(BsonReader reader, DecoderContext decoderContext) {
        return OffsetDateTime.parse(reader.readString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    @Override
    public Class<OffsetDateTime> getEncoderClass() {
        return OffsetDateTime.class;
    }
}
