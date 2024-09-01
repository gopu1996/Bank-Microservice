package com.bank.account.services.impl;

import com.bank.account.Entity.Accounts;
import com.bank.account.Entity.Customer;
import com.bank.account.dto.*;
import com.bank.account.exception.CustomerAlreadyExistExpection;
import com.bank.account.exception.ResourceNotFoundException;
import com.bank.account.mapper.AccountsMapper;
import com.bank.account.mapper.CustomerMapper;
import com.bank.account.repository.AccountsRepository;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.services.CustomerService;
import com.bank.account.services.client.CardsFallBack;
import com.bank.account.services.client.CardsFeignClient;
import com.bank.account.services.client.LoansFallBack;
import com.bank.account.services.client.LoansFeignClient;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {


    private AccountsRepository accountsRepository;
    private CustomerRepository customerRepository;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;


    @Override
    public CustomerDetailsDto fetchCustomerDetails(String mobileNumber, String correlationId) {

        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "MobileNumer", mobileNumber)
        );
        Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "CustomerId", customer.getCustomerId())
        );
        CustomerDetailsDto customerDetailsDto = CustomerMapper.mapToCustomerDetailsDto(customer, new CustomerDetailsDto());
        customerDetailsDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));
        ResponseEntity<LoansDto> loansDto = loansFeignClient.getLoansDetails(correlationId, mobileNumber);
        if (loansDto != null) {
            customerDetailsDto.setLoansDto(loansDto.getBody());
        }
        ResponseEntity<CardsDto> cardsDto = cardsFeignClient.fetchCardsDetails(correlationId, mobileNumber);
        if (cardsDto != null) {
            customerDetailsDto.setCardsDto(cardsDto.getBody());
        }
        System.out.println("cardResponse,loanResponse" + loansDto + " " + cardsDto + "===>");
        return customerDetailsDto;
    }

    @Override
    @Transactional
    public boolean createCustomerDetails(String correlationId, CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepository.findByMobileNumber(customerDto.getMobileNumber());
        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistExpection("Customer is already registered with given mobile number "
                    + customerDto.getMobileNumber());
        }

        if (!cardsFeignClient.getHealthCheck()) {
            System.out.println("Card Microservices " + cardsFeignClient.getHealthCheck());
            throw new RuntimeException("Card Microservices is Down");
        } else if (!loansFeignClient.getHealthCheck()) {
            System.out.println("Loan Microservices " + cardsFeignClient.getHealthCheck());
            throw new RuntimeException("Loan Microservices is Down");
        } else {
            Customer saveCustomer = customerRepository.save(customer);
            accountsRepository.save(AccountServiceImpl.createNewAccount(saveCustomer));
            cardsFeignClient.createCards(customer.getMobileNumber());
            loansFeignClient.createLoan(customer.getMobileNumber());
        }

        return true;
    }
}
