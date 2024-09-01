package com.bank.account.services;

import com.bank.account.dto.CustomerDetailsDto;
import com.bank.account.dto.CustomerDto;

public interface CustomerService {

    CustomerDetailsDto fetchCustomerDetails(String mobileNumber,String correlationId);

    boolean createCustomerDetails(String correlationId , CustomerDto customerDto);
}


