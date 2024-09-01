package com.bank.account.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Accounts",
        description = "Schema to hold Customer and Account Information"
)
public class AccountsDto {

    @Schema(
            description = "Account number of customer"
    )
    @Digits(integer = 10, message = "Account number must be 10 digit", fraction = 0)
    private long accountNumber;

    @Schema(
            description = "Account Type of customer want", example = "Saving"
    )
    @NotEmpty(message = "Account Type Cannot be null and empty")
    private String accountType;

    @Schema(
            description = "Branch Address of Bank"
    )
    @NotEmpty(message = "Branch Address Cannot be null and empty")
    private String branchAddress;

}
