package com.example.boilerplate.domain.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "test_data")
public class TestDocument {
    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String type; // "FLIGHT" or "HOTEL"

    @Field(type = FieldType.Text)
    private String name; // Airline or Hotel Name

    @Field(type = FieldType.Keyword)
    private String location; // Route (e.g., ICN-NRT) or City (e.g., Tokyo, Paris)

    @Field(type = FieldType.Integer)
    private Integer price;

    @Field(type = FieldType.Text)
    private String productCode; // Flight Number or Hotel Room Code

    @Field(type = FieldType.Long)
    private Long timestamp;
}
