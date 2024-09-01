package com.bank.cards.controller;

import com.bank.cards.constant.CardsConstatns;
import com.bank.cards.dto.CardsContactInfoDto;
import com.bank.cards.dto.CardsDto;
import com.bank.cards.dto.ErrorResponseDto;
import com.bank.cards.dto.ResponseDto;
import com.bank.cards.service.CardsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "CRUD REST APIs for Cards in Bank",
        description = "CRUD REST APIs in Bank to CREATE, UPDATE, FETCH AND DELETE card details"
)
@RestController
@RequestMapping("/api")
@Validated
public class CardsController {

    private static final Logger logger = LoggerFactory.getLogger(CardsController.class);

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private CardsContactInfoDto cardsContactInfoDto;

    @Autowired
    private CardsServices cardsServices;

    @Operation(
            summary = "Create Card REST API",
            description = "REST API to create new Card inside Bank"
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
    @PostMapping("/createCards")
    public ResponseEntity<ResponseDto> createCards(@Valid @RequestParam
                                                       @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                       String mobileNumber){

        boolean isCreated =  cardsServices.createCards(mobileNumber);
         if (isCreated){
             return ResponseEntity.status(HttpStatus.CREATED).body(new
                     ResponseDto(CardsConstatns.STATUS_201,CardsConstatns.MESSAGE_201));
         } else {
             return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                     .body(new
                             ResponseDto(CardsConstatns.STATUS_500,CardsConstatns.MESSAGE_500));
         }
    }

    @Operation(
            summary = "Fetch Card Details REST API",
            description = "REST API to fetch card details based on a mobile number"
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
    })
    @GetMapping("/fetchCards")
    public ResponseEntity<CardsDto> fetchCardsDetils(@Valid
                                                         @RequestHeader("bank-correlation-id") String correlationId,
                                                         @RequestParam
                                                         @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
                                                         String mobileNumber){
        logger.debug("Bank-correlation-id found in RequestTraceFilter : {} ", correlationId);
        CardsDto cardsDto =  cardsServices.fetchCardsDetails(mobileNumber);

      return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
    }

    @Operation(
            summary = "Update Card Details REST API",
            description = "REST API to update card details based on a card number"
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
    })
    @PutMapping("/updateCardDetails")
    public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto, @RequestParam
    @Pattern(regexp="(^$|[0-9]{12})",message = "card number must be 12 digits")
    String cardNumber){
        boolean isUpdated = cardsServices.updateCards(cardsDto,cardNumber);
        if (isUpdated){
            return ResponseEntity.status(HttpStatus.OK).body(new
                    ResponseDto(CardsConstatns.STATUS_200,CardsConstatns.MESSAGE_200));
        } else {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new
                            ResponseDto(CardsConstatns.STATUS_500,CardsConstatns.MESSAGE_500));
        }
    }
    @Operation(
            summary = "Delete Card Details REST API",
            description = "REST API to delete Card details based on a mobile number"
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
    })

    @DeleteMapping("/deleteCard")
    public ResponseEntity<ResponseDto> deleteCardById(@Valid @RequestParam  @Pattern(regexp="(^$|[0-9]{10})",message = "Mobile number must be 10 digits")
    String mobileNumber){
        boolean isDeleted = cardsServices.deleteCard(mobileNumber);

        if (isDeleted){
            return ResponseEntity.status(HttpStatus.OK).body(new
                    ResponseDto(CardsConstatns.STATUS_200,CardsConstatns.MESSAGE_200));
        } else {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new
                            ResponseDto(CardsConstatns.STATUS_500,CardsConstatns.MESSAGE_500));
        }
    }

    @Operation(
            summary = "Get Build information",
            description = "Get Build information that is deployed into cards microservice"
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
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Get Java version",
            description = "Get Java versions details that is installed into cards microservice"
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
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Get Contact Info",
            description = "Contact Info details that can be reached out in case of any issues"
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
    @GetMapping("/contact-info")
    public ResponseEntity<CardsContactInfoDto> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cardsContactInfoDto);
    }

    @GetMapping("/healthCheck")
    public boolean getHealthCheck(){
        return  cardsServices.getHealthCheck();
    }
}
