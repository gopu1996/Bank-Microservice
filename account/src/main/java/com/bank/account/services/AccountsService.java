package com.bank.account.services;


import com.bank.account.dto.CustomerDto;
import jakarta.transaction.Transactional;

public interface
AccountsService {

    /**
     *
     * @param customerDto - CustomerDto Object
     */

    void createAccount(CustomerDto customerDto);

    CustomerDto fetchAccount(String mobileNumber);

    boolean updateAccount(CustomerDto customerDto, long accountNumber);
    @Transactional
    boolean deleteAccount(String mobileNumber);
}
