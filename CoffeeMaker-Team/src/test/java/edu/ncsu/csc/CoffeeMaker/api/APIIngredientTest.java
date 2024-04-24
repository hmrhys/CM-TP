package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.services.IngredientService;

/**
 * Tests the API Ingredient Controller
 *
 * @author Natalie Meuser
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIIngredientTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    private MockMvc               mvc;

    /**
     * Context of the web application
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * IngredientService, autowired, allows for manipulating the Ingredient
     * model
     */
    @Autowired
    private IngredientService     service;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Test getting an ingredient
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void getIngredient () throws Exception {
        service.deleteAll();

        final Ingredient i = new Ingredient( "Coffee", 5 );

        service.save( i );

        final String getIngredientResult = mvc.perform( get( "/api/v1//ingredients/{%s}", "Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( getIngredientResult.contains( "5" ) );

        final String getInvalid = mvc.perform( get( "/api/v1//ingredients/{%s}", "Invalid Ingredient" ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( getInvalid.contains( "No ingredient found with name" ) );

    }

}
