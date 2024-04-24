package edu.ncsu.csc.CoffeeMaker.api;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Ingredient;
import edu.ncsu.csc.CoffeeMaker.models.Inventory;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.InventoryService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Testing the API
 *
 * @author Natalie Meuser
 *
 */
@RunWith ( SpringRunner.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APITest {

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
     * RecipeService, autowired, allows for manipulating the Recipe model
     */
    @Autowired
    private RecipeService         service;

    /**
     * InventoryService, autowired, allows for manipulating the Inventory model
     */
    @Autowired
    private InventoryService      invService;

    /**
     * Sets up the tests.
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
        invService.deleteAll();
    }

    /**
     * Test ensuring that a recipe can be created
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        /* Figure out if the recipe we want is present */
        if ( !recipe.contains( "Mocha" ) ) {
            final Recipe r = new Recipe();
            r.addIngredient( "Coffee", 3 );
            r.addIngredient( "Milk", 2 );
            r.setPrice( 10 );
            r.setName( "Mocha" );

            mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                    .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

        }

        recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();

        assertTrue( recipe.contains(
                "Mocha" ) ); /* Make sure that now our recipe is there */

        final Inventory i = new Inventory( 0, 0 );

        mvc.perform( put( "/api/v1/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( i ) ) ).andExpect( status().isOk() );

        final String currentInventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( currentInventory.contains( "Coffee" ) );

        final String listInventory = mvc.perform( get( "/api/v1/inventoryList" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( listInventory.contains( "Coffee" ) );

        // mvc.perform( post( String.format( "/api/v1/makecoffee/%s", "Mocha" )
        // ).contentType( MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( 100 ) ) ).andExpect(
        // status().isOk() ).andDo( print() );
    }

    /**
     * Test adding an ingredient to inventory
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void addIngredientToInventory () throws Exception {
        final String currentInventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( currentInventory.contains( "[]" ) );

        final Ingredient newIngredient = new Ingredient();

        newIngredient.setAmount( 10 );
        newIngredient.setName( "Coffee" );

        final String addResponse = mvc
                .perform( put( "/api/v1/inventoryAdd" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( newIngredient ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( addResponse.contains( "Coffee successfully created" ) );

        final String updatedInventory = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( updatedInventory.contains( "Coffee" ) );

        // add another ingredient
        final Ingredient newIngredient2 = new Ingredient();

        newIngredient2.setAmount( 10 );
        newIngredient2.setName( "Mocha" );

        final String addResponse2 = mvc
                .perform( put( "/api/v1/inventoryAdd" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( newIngredient2 ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( addResponse2.contains( "Mocha successfully created" ) );

        final String updatedInventory2 = mvc.perform( get( "/api/v1/inventory" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( updatedInventory2.contains( "Mocha" ) );

        final String listInventory = mvc.perform( get( "/api/v1/inventoryList" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( listInventory.contains( "Coffee" ) );
        Assertions.assertTrue( listInventory.contains( "Mocha" ) );

        // add a duplicate ingredient
        final Ingredient newIngredient3 = new Ingredient();

        newIngredient3.setAmount( 15 );
        newIngredient3.setName( "Mocha" );

        final String addResponse3 = mvc
                .perform( put( "/api/v1/inventoryAdd" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( newIngredient3 ) ) )
                .andExpect( status().isConflict() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( addResponse3.contains( "already exists" ) );

        // final String updatedInventory3 = mvc.perform( get(
        // "/api/v1/inventory" ) ).andDo( print() )
        // .andExpect( status().isOk()
        // ).andReturn().getResponse().getContentAsString();
        //
        // Assertions.assertTrue( updatedInventory3.contains( "Mocha" ) );
    }

}
