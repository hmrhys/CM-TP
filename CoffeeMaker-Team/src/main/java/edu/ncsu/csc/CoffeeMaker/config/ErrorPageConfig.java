package edu.ncsu.csc.CoffeeMaker.config;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Configuration
public class ErrorPageConfig {

    @Bean
    public WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> containerCustomizer () {
        return container -> {
            container.addErrorPages( new ErrorPage( HttpStatus.NOT_FOUND, "/notfound" ) );
            container.addErrorPages( new ErrorPage( HttpStatus.FORBIDDEN, "/error" ) );
        };
    }

    @Controller
    public static class CustomErrorController implements ErrorController {
        @RequestMapping ( "/notfound" )
        public String handleNotFound () {
            return "notfound.html";
        }

        @RequestMapping ( "/error" )
        public String handleError () {
            return "error.html";
        }

        @Override
        public String getErrorPath () {
            return "/error";
        }
    }
}
