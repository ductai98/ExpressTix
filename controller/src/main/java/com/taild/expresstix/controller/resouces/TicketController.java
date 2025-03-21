package com.taild.expresstix.controller.resouces;

import com.taild.expresstix.application.service.event.EventAppService;
import com.taild.expresstix.application.service.ticket.TicketDetailAppService;
import com.taild.expresstix.domain.model.dto.TicketDetailDTO;
import com.taild.expresstix.domain.repository.ticket.TicketDetailRepository;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
@Slf4j
public class TicketController {

    @Autowired
    private EventAppService eventAppService;

    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    private final RestTemplate restTemplate = new RestTemplate()  ;
    @Autowired
    private TicketDetailRepository ticketDetailRepository;


    @GetMapping("/hello")
    @RateLimiter(name = "ticket", fallbackMethod = "fallbackHello")
    public ResponseEntity<String> hello() {
        ResponseEntity response = new ResponseEntity<>(eventAppService.sayHi("taild"), HttpStatus.OK);
        return response;
    }

    public ResponseEntity<String> fallbackHello(Throwable throwable) {
        log.info("Too many requests, fallback method called. Error: {}", throwable.getMessage());
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
        return responseEntity;
    }


    @GetMapping("/ticket/{id}")
    @RateLimiter(name = "ticketdetail", fallbackMethod = "fallbackGetTicketDetail")
    public ResponseEntity<TicketDetailDTO> getTicketDetail(
            @PathVariable(name = "id") Long ticketId,
            @RequestParam(name = "version", required = false, defaultValue = "0") Long version
    ) {
        TicketDetailDTO ticketDetail = ticketDetailAppService.getTicketDetailById(ticketId, version);
        ResponseEntity<TicketDetailDTO> response = new ResponseEntity<>(ticketDetail, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<TicketDetailDTO> fallbackGetTicketDetail(Throwable throwable) {
        log.info("Too many requests, fallback method called. Error: {}", throwable.getMessage());
        ResponseEntity<TicketDetailDTO> responseEntity = new ResponseEntity<>(null, HttpStatus.TOO_MANY_REQUESTS);
        return responseEntity;
    }


    @GetMapping("/order/{id}")
    @RateLimiter(name = "ticketorder", fallbackMethod = "fallbackOrderTicket")
    public ResponseEntity<String> orderTickets(@RequestParam(name = "quantity") int quantity,
                                          @PathVariable(name = "id") Long ticketId
    ) {

        if (quantity <= 0) {
            return ResponseEntity.badRequest().body("Quantity must be greater than 0");
        }

        boolean isSuccess = ticketDetailAppService.decreaseTicketStock(ticketId, quantity);
        ResponseEntity<String> responseEntity;

        if (isSuccess) {
            log.info("Order placed successfully for ticket: {}, quantity: {}", ticketId, quantity);
            responseEntity = new ResponseEntity<>("Order placed successfully", HttpStatus.OK);
        } else {
            Long stockCache = ticketDetailAppService.getAvailableStock(ticketId);
            Long stockDB = ticketDetailRepository.getAvailableStock(ticketId);
            log.info("Fail to order, stock available in cache: {}, stock available in DB: {}", stockCache, stockDB);
            responseEntity = new ResponseEntity<>("Insufficient stock", HttpStatus.BAD_REQUEST);
        }

        return responseEntity;
    }

    public ResponseEntity<String> fallbackOrderTicket(Throwable throwable) {
        log.info("Too many requests, fallback method called. Error: {}", throwable.getMessage());
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Too many requests", HttpStatus.TOO_MANY_REQUESTS);
        return responseEntity;
    }
}
