package com.taild.expresstix.controller.resouces;

import com.taild.expresstix.application.service.event.EventAppService;
import com.taild.expresstix.application.service.ticket.TicketDetailAppService;
import com.taild.expresstix.domain.model.entity.TicketDetailEntity;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;

@RestController
@RequestMapping("/api")
public class HiController {

    private  static final SecureRandom secureRandom = new SecureRandom();

    @Autowired
    private EventAppService eventAppService;

    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    private final RestTemplate restTemplate = new RestTemplate()  ;

    @GetMapping("/hello")
    @RateLimiter(name = "backendA", fallbackMethod = "fallbackHello")
    public String hello() {
        return eventAppService.sayHi("taild");
    }

    public String fallbackHello(Throwable throwable) {
        return "Too many requests";
    }

    @GetMapping("/circuit/breaker")
    @CircuitBreaker(name = "checkRandom", fallbackMethod = "fallbackCircuitBreaker")
    public String circuitBreaker() {
        int productId = secureRandom.nextInt(20) + 1; // Product ID between 1 and 20
        String url = "https://fakestoreapi.com/products/" + productId;

        return restTemplate.getForObject(url, String.class);
    }

    public String fallbackCircuitBreaker(Throwable throwable) {
        return "Service fakestoreapi is currently unavailable";
    }

    @GetMapping("/ticket/{id}")
    public TicketDetailEntity getTickerDetail(@PathVariable(name = "id") Long ticketId) {
        return ticketDetailAppService.getTicketDetailById(ticketId);
    }
}
