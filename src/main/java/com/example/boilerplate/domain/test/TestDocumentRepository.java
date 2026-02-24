package com.example.boilerplate.domain.test;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TestDocumentRepository extends ElasticsearchRepository<TestDocument, String> {
}
