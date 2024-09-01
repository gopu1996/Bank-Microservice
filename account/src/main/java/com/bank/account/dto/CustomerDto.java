package com.bank.account.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Customer",
        description = "Schema to hold Customer and Account Information"
)
public class CustomerDto {

    @Schema(
            description = "Name of Customer" , example = "Gopal Pandey"
    )
    @NotEmpty(message = "Name Cannot be null and empty")
    @Size(min = 5, max = 30 , message = "The length of the customer name should be between 5 to 30")
    private  String name;

    @Schema(
            description = "Email of Customer" , example = "Gopal@gmail.com"
    )
    @NotEmpty(message = "Email Address Cannot be null and empty")
    @Email(message = "Email address should be valid")
    private String email;

    @Schema(
            description = "Mobile Number of Customer" , example = "8467834598"
    )
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Mobile number must be 10 digit")
    @NotEmpty(message = "Mobile Number Cannot be null and empty")
    private String mobileNumber;

    @Schema(
            description = "Account Details of Customer"
    )
    @Valid
    private AccountsDto accountsDto;
}
