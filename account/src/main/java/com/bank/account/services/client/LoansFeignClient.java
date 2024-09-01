package com.bank.account.services.client;

import com.bank.account.dto.CardsDto;
import com.bank.account.dto.LoansDto;
import com.bank.account.dto.ResponseDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans")
public interface LoansFeignClient {

    @GetMapping(value = "/api/getLoanDetails",consumes = "application/json")
    @CircuitBreaker(name = "loansCircuitBreaker", fallbackMethod = "getLoansCircuitBreakerFallback")
    ResponseEntity<LoansDto> getLoansDetails(@RequestHeader("bank-correlation-id") String correlationId, @RequestParam String mobileNumber);

    @PostMapping("/api/createLoan")
    @CircuitBreaker(name = "loansCircuitBreaker", fallbackMethod = "postLoansCircuitBreakerFallback")
    ResponseEntity<ResponseDto> createLoan(@RequestParam String mobileNumber);

    @GetMapping("/api/healthCheck")
    @CircuitBreaker(name = "loansCircuitBreaker", fallbackMethod = "getLoansHealthCheckCircuitBreakerFallback")
    boolean getHealthCheck();

    default ResponseEntity<CardsDto> getLoansCircuitBreakerFallback(Exception exception) {
        System.out.println("loan circuit breaker default method");
        return null;
    }

    default ResponseEntity<CardsDto> postLoansCircuitBreakerFallback(Exception exception) {
        System.out.println("loan circuit breaker post method");
        return null;
    }

    default boolean getLoansHealthCheckCircuitBreakerFallback(Exception exception) {
        System.out.println("loan circuit breaker get method");
        return false;
    }
}
