package com.ccoins.bff.decoder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public class PageDeserializer<T> extends JsonDeserializer<Page<?>> {

    @Override
    public Page<T> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectCodec codec = p.getCodec();
        JsonNode node = codec.readTree(p);

        List<T> content = codec.readValue(node.get("content").traverse(), new TypeReference<>(){});
        Pageable pageable = codec.readValue(node.get("pageable").traverse(), Pageable.class);
        long total = node.get("totalElements").asLong();

        return new PageImpl<>(content, pageable, total);
    }
}