package com.example.boilerplate.controller;

import com.example.boilerplate.domain.test.TestDocument;
import com.example.boilerplate.domain.test.TestDocumentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile({ "local", "dev" })
public class TestController {

    private final RedisTemplate<String, Object> redisTemplate;
    private final TestDocumentRepository testDocumentRepository;
    private final ObjectMapper objectMapper;

    @GetMapping("/test/log")
    public String generateLog() {
        log.info("log1");
        log.warn("log2");
        log.error("log3");
        return "log";
    }

    /**
     * API 1: 항공(FLIGHT) 1000개, 호텔(HOTEL) 1000개를 생성하여 Redis에 적재
     * Key 형식: flight:{uuid}, hotel:{uuid}
     */
    @GetMapping("/test/redis/load")
    public String loadRedisData() {
        log.info("Starting to load 1000 Flights and 1000 Hotels into Redis");

        Random random = new Random();
        long currentTime = System.currentTimeMillis();

        String[] airlines = { "Korean Air", "Asiana Airlines", "Jeju Air", "Jin Air", "Tway Air", "United Airlines",
                "Delta Airlines" };
        String[] flightRoutes = { "ICN-NRT", "ICN-JFK", "ICN-CDG", "GMP-CJU", "ICN-BKK", "ICN-SFO", "ICN-LHR" };

        String[] hotelBrands = { "Hilton", "Marriott", "Hyatt", "InterContinental", "Shilla", "Lotte", "Westin" };
        String[] hotelCities = { "Seoul", "Tokyo", "New York", "Paris", "Bangkok", "London", "Jeju" };

        // 항공 1000개 → Redis
        for (int i = 0; i < 1000; i++) {
            String id = UUID.randomUUID().toString();
            TestDocument doc = TestDocument.builder()
                    .id(id)
                    .type("FLIGHT")
                    .name(airlines[random.nextInt(airlines.length)])
                    .location(flightRoutes[random.nextInt(flightRoutes.length)])
                    .price(100000 + random.nextInt(1500000))
                    .productCode("KE" + (100 + random.nextInt(900)))
                    .timestamp(currentTime + i)
                    .build();
            redisTemplate.opsForValue().set("flight:" + id, doc);
        }

        // 호텔 1000개 → Redis
        for (int i = 0; i < 1000; i++) {
            String id = UUID.randomUUID().toString();
            String city = hotelCities[random.nextInt(hotelCities.length)];
            TestDocument doc = TestDocument.builder()
                    .id(id)
                    .type("HOTEL")
                    .name(hotelBrands[random.nextInt(hotelBrands.length)] + " " + city)
                    .location(city)
                    .price(50000 + random.nextInt(800000))
                    .productCode("RM-" + (100 + random.nextInt(900)))
                    .timestamp(currentTime + i + 1000)
                    .build();
            redisTemplate.opsForValue().set("hotel:" + id, doc);
        }

        log.info("Successfully loaded 1000 Flights and 1000 Hotels into Redis");
        return "Successfully loaded 2000 documents (1000 Flights, 1000 Hotels) to Redis";
    }

    /**
     * API 2: Redis에 적재된 flight:* / hotel:* 데이터를 읽어서 Elasticsearch에 bulk insert
     */
    @GetMapping("/test/es/load")
    public String loadEsFromRedis() {
        log.info("Starting to migrate data from Redis to Elasticsearch");

        List<TestDocument> docs = new ArrayList<>();

        // flight:* 키 스캔
        try (var cursor = redisTemplate.scan(ScanOptions.scanOptions().match("flight:*").count(200).build())) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                Object raw = redisTemplate.opsForValue().get(key);
                if (raw != null) {
                    TestDocument doc = objectMapper.convertValue(raw, TestDocument.class);
                    docs.add(doc);
                }
            }
        } catch (Exception e) {
            log.error("Error scanning flight keys from Redis", e);
        }

        // hotel:* 키 스캔
        try (var cursor = redisTemplate.scan(ScanOptions.scanOptions().match("hotel:*").count(200).build())) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                Object raw = redisTemplate.opsForValue().get(key);
                if (raw != null) {
                    TestDocument doc = objectMapper.convertValue(raw, TestDocument.class);
                    docs.add(doc);
                }
            }
        } catch (Exception e) {
            log.error("Error scanning hotel keys from Redis", e);
        }

        log.info("Migrating {} documents from Redis to Elasticsearch", docs.size());
        testDocumentRepository.saveAll(docs);
        log.info("Successfully migrated {} documents to Elasticsearch", docs.size());

        return "Successfully migrated " + docs.size() + " documents from Redis to Elasticsearch";
    }
}
