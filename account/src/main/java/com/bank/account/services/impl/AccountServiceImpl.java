package com.bank.account.services.impl;

import com.bank.account.Entity.Accounts;
import com.bank.account.Entity.Customer;
import com.bank.account.constants.AccountConstatns;
import com.bank.account.dto.AccountsDto;
import com.bank.account.dto.CustomerDto;
import com.bank.account.exception.CustomerAlreadyExistExpection;
import com.bank.account.exception.ResourceNotFoundException;
import com.bank.account.mapper.AccountsMapper;
import com.bank.account.mapper.CustomerMapper;
import com.bank.account.repository.AccountsRepository;
import com.bank.account.repository.CustomerRepository;
import com.bank.account.services.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountsService {

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private CustomerRepository customerRepository;


    @Override
    public void createAccount(CustomerDto customerDto) {

        Customer customer = CustomerMapper.mapToCustomer(customerDto,new Customer());
         Optional<Customer> optionalCustomer =  customerRepository.findByMobileNumber(customerDto.getMobileNumber());
         if (optionalCustomer.isPresent()){
             throw new CustomerAlreadyExistExpection("Customer is already registered with given mobile number "
                     +customerDto.getMobileNumber() );
         }
        Customer saveCustomer = customerRepository.save(customer);
        accountsRepository.save(createNewAccount(saveCustomer));

    }

    @Override
    public CustomerDto fetchAccount(String mobileNumber) {

      Customer customer =   customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                 ()-> new ResourceNotFoundException("Customer","MobileNumer",mobileNumber)
         );

        Accounts accounts =   accountsRepository.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()-> new ResourceNotFoundException("Account","CustomerId",customer.getCustomerId())
        );

      CustomerDto customerDto =  CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
      customerDto.setAccountsDto(AccountsMapper.mapToAccountsDto(accounts, new AccountsDto()));

      return customerDto;

    }

    @Override
    public boolean updateAccount(CustomerDto customerDto, long accountNumber) {
        boolean isUpdated = false;

        AccountsDto accountsDto = customerDto.getAccountsDto();
        if (accountsDto != null){
           Accounts accounts = accountsRepository.findById(accountNumber).orElseThrow(
                    ()-> new ResourceNotFoundException("Account","AccountNumber",accountsDto.getAccountNumber()));
            accountsDto.setAccountNumber(accountNumber);
            accounts = accountsRepository.save(AccountsMapper.mapToAccounts(accountsDto,accounts));

            long customerId = accounts.getCustomerId();
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    ()-> new ResourceNotFoundException("Customer","CustomerID",customerId));

            customerRepository.save(CustomerMapper.mapToCustomer(customerDto,customer));

            isUpdated = true;

        }

        return isUpdated;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {


        Customer customer = customerRepository.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFoundException("Customer","MobileNumber",mobileNumber));

       accountsRepository.deleteByCustomerId(customer.getCustomerId());
       customerRepository.deleteById(customer.getCustomerId());

        return true;
    }


    public static Accounts createNewAccount(Customer customer){

        Accounts newAccounts = new Accounts();
        newAccounts.setCustomerId(customer.getCustomerId());
        long randomAccountNumber =  1000000000L + new Random().nextInt(900000000);
        newAccounts.setAccountNumber(randomAccountNumber);
        newAccounts.setAccountType(AccountConstatns.SAVINGS);
        newAccounts.setBranchAddress(AccountConstatns.ADDRESS);

        return newAccounts;

    }
}
