package com.bank.loans.services.impl;


import com.bank.loans.constants.LoansConstants;
import com.bank.loans.dto.LoansDto;
import com.bank.loans.entity.Loans;
import com.bank.loans.exception.LoansAlreadyExistsException;
import com.bank.loans.exception.ResourceNotFoundException;
import com.bank.loans.mapper.LoansMapper;
import com.bank.loans.repository.LoansRepository;
import com.bank.loans.services.LoanServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class LoansServiceImpl implements LoanServices {

    @Autowired
    private LoansRepository loanRepository;

    @Autowired
    private HealthEndpoint healthEndpoint;

    @Override
    public boolean createLoan(String mobileNumber) {

        Optional<Loans> loans= loanRepository.findByMobileNumber(mobileNumber);
        if (loans.isPresent())
               throw  new LoansAlreadyExistsException("Loan is already exist with given number "+mobileNumber);

        loanRepository.save(createNewLoan(mobileNumber));
        return true;
    }

    @Override
    public LoansDto getLoansDetails(String mobileNumber) {

      Loans loans =  loanRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Loan","Mobile Number", mobileNumber));

        return LoansMapper.mapToLoansDto(loans, new LoansDto());
    }

    @Override
    public boolean updateLoans(LoansDto loansDto, String loanNumber) {

      Loans loans =  loanRepository.findByLoanNumber(loanNumber).orElseThrow(() ->
                new ResourceNotFoundException("Loan","Loan Number", loanNumber));

      LoansMapper.mapToLoans(loansDto,loans);
      loans.setLoanNumber(loanNumber);
      loans.setOutstandingAmount(loansDto.getTotalLoan() - loansDto.getAmountPaid());
      loanRepository.save(loans);
        return true;
    }

    @Override
    public boolean deleteLoan(String mobileNumber) {
        Loans loans =  loanRepository.findByMobileNumber(mobileNumber).orElseThrow(() ->
                new ResourceNotFoundException("Loan","Mobile Number", mobileNumber));
        loanRepository.deleteById(loans.getLoanId());
        return true;
    }

    @Override
    public boolean getHealthCheck() {
        String isUp = String.valueOf(healthEndpoint.health().getStatus());
        if (isUp.equalsIgnoreCase("UP")){
            return true;
        } else {
            return false;
        }
    }


    private Loans createNewLoan(String mobileNumber) {
        Loans newLoan = new Loans();
        long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
        newLoan.setLoanNumber(Long.toString(randomLoanNumber));
        newLoan.setMobileNumber(mobileNumber);
        newLoan.setLoanType(LoansConstants.HOME_LOAN);
        newLoan.setTotalLoan(LoansConstants.NEW_LOAN_LIMIT);
        newLoan.setAmountPaid(0);
        newLoan.setOutstandingAmount(LoansConstants.NEW_LOAN_LIMIT);
        return newLoan;
    }
}
