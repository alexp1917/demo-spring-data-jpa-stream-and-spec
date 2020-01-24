package com.benjamintanone.springdatajpademojava.controllers;

import com.benjamintanone.springdatajpademojava.domain.Customer;
import com.benjamintanone.springdatajpademojava.repositories.CustomerRepository;
import com.benjamintanone.springdatajpademojava.specifications.CustomerSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Supplier;
import java.util.stream.Stream;

@RestController
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EntityManager entityManager;

    private String getCsvRowFromCustomer(Customer customer) {
        return String.format("%d,%s\n", customer.getId(), customer.getName());
    }

    private void writeCustomersToResponseAsCsv(Stream<Customer> customerStream,
                                final HttpServletResponse response) throws IOException {
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment;filename=customers.csv");
        PrintWriter printWriter = response.getWriter();
        printWriter.write("id,name\n"); // the CSV column header, not really important here
        customerStream.peek(customer -> printWriter.write(getCsvRowFromCustomer(customer)))
            .forEach(entityManager::detach); // optional, but objects _may_ not be GC'd if you don't detach them first.
        printWriter.flush();
        printWriter.close();
    }

    @GetMapping("/customers_fragment.csv")
    @Transactional(readOnly = true) // this is important, because Streams can only be opened in a transaction
    public void getCustomersCsv(
            final HttpServletResponse response,
            @RequestParam(required = false) final String name
    ) throws IOException {
        Specification<Customer> specification = CustomerSpecification.hasName(name);
        writeCustomersToResponseAsCsv(customerRepository.stream(specification, Customer.class), response);
    }
}