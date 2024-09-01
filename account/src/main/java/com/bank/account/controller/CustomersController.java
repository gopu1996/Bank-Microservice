package com.bank.account.controller;

import com.bank.account.constants.AccountConstatns;
import com.bank.account.dto.CustomerDetailsDto;
import com.bank.account.dto.CustomerDto;
import com.bank.account.dto.ErrorResponseDto;
import com.bank.account.dto.ResponseDto;
import com.bank.account.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api",produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@Tag(
        name = "Rest Api for Customer in Bank",
        description = "Rest Api for Customers details"
)

public class CustomersController {

    private static final Logger logger = LoggerFactory.getLogger(CustomersController.class);

    @Autowired
    private CustomerService customerService;

    @Operation(
            summary = "Fetch Customer Details REST API",
            description = "REST API to fetch Customer details based on a mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )
    @GetMapping("/fetchCustomerDetails")
    public ResponseEntity<CustomerDetailsDto> fetchCustomerDetails(@Valid
                                                                  @RequestHeader("bank-correlation-id") String correlationId,
                                                                       @RequestParam
                                                                   @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
                                                                   String mobileNumber){
       logger.debug("Bank-correlation-id found in RequestTraceFilter : {} ", correlationId);
       CustomerDetailsDto customerDetailsDto =  customerService.fetchCustomerDetails(mobileNumber,correlationId);

        return ResponseEntity.
                status(HttpStatus.OK)
                .body(customerDetailsDto);
    }

    @Operation(
            summary = "Create Customer Details",
            description = "Customer Details inside Bank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status Internal Server Error",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponseDto.class)
                    )
            )
    }
    )

    @PostMapping("/createCustomerDetails")
    public ResponseEntity<ResponseDto> createCustomerDetails(@Valid @RequestHeader("bank-correlation-id") String correlationId, @RequestBody CustomerDto customerDto){

        boolean isCreated =  customerService.createCustomerDetails(correlationId,customerDto);
        if (isCreated){
            return ResponseEntity.status(HttpStatus.CREATED).body(new
                    ResponseDto(AccountConstatns.STATUS_201,AccountConstatns.CUST_MESSAGE_201));
        } else {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new
                            ResponseDto(AccountConstatns.STATUS_500,AccountConstatns.MESSAGE_500));
        }
    }

}
