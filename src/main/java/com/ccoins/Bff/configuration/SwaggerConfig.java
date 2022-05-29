package com.ccoins.Bff.configuration;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

import static com.ccoins.Bff.configuration.security.JwtUtils.*;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket swaggerDocket() {

        return new Docket(SWAGGER_2)
                .apiInfo(apiInfo())         // infomacion adicional
                .securityContexts(Lists.newArrayList(securityContext()))
                .securitySchemes(Lists.newArrayList(apikey()))    // agregar autenticacion con jwt
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.any())     // package base donde va a encontrar los controladores
                .paths(PathSelectors.any())        // para cualquier path menos el de HomeController
                .build();

    }
    private ApiKey apikey() {

        return new ApiKey(TOKEN, AUTHORIZATION, HEADER);
    }

    private SecurityContext securityContext() {

        return SecurityContext.builder()
                .securityReferences(defaultAuth())
               // .forPaths(PathSelectors.regex("/api/.*"))
                .build();
    }

    private List<SecurityReference> defaultAuth() {

        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;

        return Lists.newArrayList(new SecurityReference(TOKEN, authorizationScopes));
    }

    private ApiInfo apiInfo(){

        return new ApiInfoBuilder()
                .title("Chopp Coins")
                .description("Backend API for Tesis, UTN MDP")
                //.termsOfServiceUrl("")
                .contact(new Contact("Manigima", "", "manigima.dev@gmail.com"))
                //.license("Apache License Version 2.0")
                //.licenseUrl("https://www.apache.org/licenses/LICENSE-2.0")
                .version("0.0.1")
                .build();
    }

}
