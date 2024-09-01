package com.bank.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(name = "Cards",
        description = "Schema to hold Card information"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardsDto {

    @NotEmpty(message = "Mobile Number can not be a null or empty")
    @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile Number must be 10 digits")
    @Schema(
            description = "Mobile Number of Customer", example = "4354437687"
    )
    private String mobileNumber;

    @Schema(
            description = "Card Number of the customer", example = "100646930341"
    )
    private String cardNumber;

    @Schema(
            description = "Type of the card", example = "Credit Card"
    )
    @NotEmpty(message = "CardType can not be a null or empty")
    private String cardType;

    @Schema(
            description = "Total amount limit available against a card", example = "100000"
    )
    @Positive(message = "Total card limit should be greater than zero")
    private long totalLimit;

    @Schema(
            description = "Total amount used by a Customer", example = "1000"
    )
    @PositiveOrZero(message = "Total amount used should be equal or greater than zero")
    private long amountUsed;
    @Schema(
            description = "Total available amount against a card", example = "90000"
    )
    @PositiveOrZero(message = "Total available amount should be equal or greater than zero")
    private long availableAmount;
}
