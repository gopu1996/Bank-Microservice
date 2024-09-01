package com.bank.account;

import com.bank.account.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts Microservice Rest Api Docu.0" +
						"mentation",
				description = "Bank Accounts Microservice Rest Api Documentation",
				version = "v1",
				contact = @Contact(
						name = "Gopal Pandey",
						email = "Gopal@gmail.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "www.bank.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Bank Accounts Microservice Rest Api Documentation",
				url = "www.bank.com"
		)
)
public class AccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountApplication.class, args);
	}

}
