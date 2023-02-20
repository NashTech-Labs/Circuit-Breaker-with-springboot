package io.knoldus.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.knoldus.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/orderRecords")
public class Controller {
    @Autowired
    @Lazy
    private RestTemplate restTemplate;

    public static final String USER_SERVICE="customerService";
    private static final String BASEURL = "http://localhost:9191";

    @GetMapping("/displayCustomers")
    @CircuitBreaker(name =USER_SERVICE,fallbackMethod = "getAllAvailableCustomersData")
    public List<CustomerDTO> displayOrders(@RequestParam("customers") String customers) {
        String uri = customers == null ? BASEURL : BASEURL + "/" + customers;
        return restTemplate.getForObject(uri, ArrayList.class);
    }

    public List<CustomerDTO> getAllAvailableCustomersData(Exception e){
        return Stream.of(
                new CustomerDTO(119, "Rohan", "Alexa", 5000),
                new CustomerDTO(532, "Rohit", "IPhone 12", 60000)
        ).collect(Collectors.toList());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
