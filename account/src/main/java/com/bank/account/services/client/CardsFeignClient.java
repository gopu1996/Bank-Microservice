package com.bank.account.services.client;

import com.bank.account.dto.CardsDto;
import com.bank.account.dto.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "cards")
public interface CardsFeignClient {

    @GetMapping(value = "/api/fetchCards",consumes = "application/json")
    @CircuitBreaker(name = "cardCircuitBreaker", fallbackMethod = "getCardCircuitBreakerFallback")
    ResponseEntity<CardsDto> fetchCardsDetails(@RequestHeader("bank-correlation-id") String correlationId, @RequestParam String mobileNumber);

    @PostMapping("/api/createCards")
    @CircuitBreaker(name = "cardsCircuitBreaker", fallbackMethod = "postCardCircuitBreakerFallback")
    ResponseEntity<ResponseDto> createCards( @RequestParam String mobileNumber);

    @GetMapping("/api/healthCheck")
    @CircuitBreaker(name = "cardsCircuitBreaker", fallbackMethod = "getCardHealthCheckCircuitBreakerFallback")
    boolean getHealthCheck();

    default ResponseEntity<CardsDto> getCardCircuitBreakerFallback(Exception exception) {
        System.out.println("circuit breaker default method");
        return null;
    }

    default ResponseEntity<CardsDto> postCardCircuitBreakerFallback(Exception exception) {
        System.out.println("circuit breaker post method");
        return null;
    }

    default boolean getCardHealthCheckCircuitBreakerFallback(Exception exception) {
        System.out.println("circuit breaker Card health method");
        return false;
    }
}
