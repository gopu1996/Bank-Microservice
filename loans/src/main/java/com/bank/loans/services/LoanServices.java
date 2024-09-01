package com.bank.loans.services;

import com.bank.loans.dto.LoansDto;
import jakarta.transaction.Transactional;

public interface LoanServices {

    boolean createLoan(String mobileNumber);

    LoansDto getLoansDetails(String mobileNumber);

    boolean updateLoans(LoansDto loansDto, String loanNumber);
    @Transactional
    boolean deleteLoan(String mobileNumber);

    boolean getHealthCheck();
}
