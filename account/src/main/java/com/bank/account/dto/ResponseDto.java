package com.bank.account.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(
        name = "Response",
        description = "Schema to hold successful response information"
)
public class ResponseDto {

    @Schema(
            description = "Status Code in response", example = "200"
    )
    private String statusCode;
    @Schema(
            description = "Status message in the response", example = "Request processed successfully"
    )
    private String statusMsg;
}
