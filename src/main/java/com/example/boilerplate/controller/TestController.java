package com.example.boilerplate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.boilerplate.domain.test.TestDocument;
import com.example.boilerplate.domain.test.TestDocumentRepository;

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

    @GetMapping("/test/log")
    public String generateLog() {
        log.info("log1");
        log.warn("log2");
        log.error("log3");
        return "log";
    }

    @GetMapping("/test/redis")
    public String testRedis(@RequestParam String key, @RequestParam String value) {
        log.info("Setting Redis key: {} with value: {}", key, value);
        redisTemplate.opsForValue().set(key, value);

        Object storedValue = redisTemplate.opsForValue().get(key);
        log.info("Retrieved from Redis - Key: {}, Value: {}", key, storedValue);

        return "Redis set success. Key: " + key + ", Value: " + storedValue;
    }

    @GetMapping("/est/tes/load")
    public String loadElasticsearchData() {
        log.info("Starting to load 1000 Flight and 1000 Hotel test documents into Elasticsearch");
        List<TestDocument> docs = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        Random random = new Random();

        String[] airlines = { "Korean Air", "Asiana Airlines", "Jeju Air", "Jin Air", "Tway Air", "United Airlines",
                "Delta Airlines" };
        String[] flightRoutes = { "ICN-NRT", "ICN-JFK", "ICN-CDG", "GMP-CJU", "ICN-BKK", "ICN-SFO", "ICN-LHR" };

        String[] hotelBrands = { "Hilton", "Marriott", "Hyatt", "InterContinental", "Shilla", "Lotte", "Westin" };
        String[] hotelCities = { "Seoul", "Tokyo", "New York", "Paris", "Bangkok", "London", "Jeju" };

        // Generate 1000 Flight Data
        for (int i = 0; i < 1000; i++) {
            String airline = airlines[random.nextInt(airlines.length)];
            String route = flightRoutes[random.nextInt(flightRoutes.length)];
            int price = 100000 + random.nextInt(1500000); // 100k ~ 1.6m KRW
            String flightNo = "KE" + (100 + random.nextInt(900));

            docs.add(TestDocument.builder()
                    .id(UUID.randomUUID().toString())
                    .type("FLIGHT")
                    .name(airline)
                    .location(route)
                    .price(price)
                    .productCode(flightNo)
                    .timestamp(currentTime + i)
                    .build());
        }

        // Generate 1000 Hotel Data
        for (int i = 0; i < 1000; i++) {
            String brand = hotelBrands[random.nextInt(hotelBrands.length)];
            String city = hotelCities[random.nextInt(hotelCities.length)];
            int price = 50000 + random.nextInt(800000); // 50k ~ 850k KRW
            String roomCode = "RM-" + (100 + random.nextInt(900));

            docs.add(TestDocument.builder()
                    .id(UUID.randomUUID().toString())
                    .type("HOTEL")
                    .name(brand + " " + city)
                    .location(city)
                    .price(price)
                    .productCode(roomCode)
                    .timestamp(currentTime + i + 1000)
                    .build());
        }

        testDocumentRepository.saveAll(docs);
        log.info("Successfully saved 2000 test documents (1000 Flights, 1000 Hotels) to Elasticsearch");

        return "Successfully loaded 2000 documents (1000 Flights, 1000 Hotels) to Elasticsearch";
    }
}
