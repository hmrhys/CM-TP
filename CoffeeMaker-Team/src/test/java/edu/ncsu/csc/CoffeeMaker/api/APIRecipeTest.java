package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.common.TestUtils;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;

/**
 * Tests the API Recipe Controller
 *
 * @author Natalie Meuser
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIRecipeTest {

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
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    /**
     * Testing creating an recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void ensureRecipe () throws Exception {
        service.deleteAll();

        final Recipe r = new Recipe();
        r.addIngredient( "Coffee", 3 );
        r.addIngredient( "Milk", 4 );
        r.setPrice( 10 );
        r.setName( "Mocha" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r ) ) ).andExpect( status().isOk() );

    }

    /**
     * Test the recipe api
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testRecipeAPI () throws Exception {

        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( "Coffee", 1 );
        recipe.addIngredient( "Milk", 20 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

    }

    /**
     * Test adding a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testAddRecipe2 () throws Exception {

        /* Tests a recipe with a duplicate name to make sure it's rejected */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
        final String name = "Coffee";
        final Recipe r1 = createRecipe( name, 50, 3, 1, 0 );

        service.save( r1 );

        final Recipe r2 = createRecipe( name, 50, 3, 1, 0 );
        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r2 ) ) ).andExpect( status().is4xxClientError() );

        Assertions.assertEquals( 1, service.findAll().size(), "There should only one recipe in the CoffeeMaker" );
    }

    /**
     * Test adding a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testAddRecipe15 () throws Exception {

        /* Tests to make sure that our cap of 3 recipes is enforced */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 2, 0 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 3, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = createRecipe( "Hot Milk", 75, 1, 2, 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );
    }

    /**
     * Test getting a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testGetRecipe () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( "Coffee", 1 );
        recipe.addIngredient( "Milk", 20 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        final String getRecipeResult = mvc.perform( get( "/api/v1/recipes/{%s}", "Delicious Not-Coffee" ) )
                .andDo( print() ).andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( getRecipeResult.contains( "Delicious Not-Coffee" ) );

        final String getInvalidResult = mvc.perform( get( "/api/v1/recipes/{%s}", "Nonexistant Recipe" ) )
                .andDo( print() ).andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( getInvalidResult.contains( "No recipe found with name" ) );

    }

    /**
     * Test deleting a recipe implemented by srschere
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testDeleteAddRecipeSS () throws Exception {

        /*
         * Tests to make sure that our cap of 3 recipes is enforced, but that
         * another recipe can be added if one is deleted
         */

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0 );
        service.save( r1 );
        final Recipe r2 = createRecipe( "Mocha", 50, 3, 2, 0 );
        service.save( r2 );
        final Recipe r3 = createRecipe( "Latte", 60, 3, 3, 0 );
        service.save( r3 );

        Assertions.assertEquals( 3, service.count(),
                "Creating three recipes should result in three recipes in the database" );

        final Recipe r4 = createRecipe( "Hot Chocolate", 75, 1, 2, 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) ).andExpect( status().isInsufficientStorage() );

        Assertions.assertEquals( 3, service.count(), "Creating a fourth recipe should not get saved" );

        service.delete( r3 );

        final String recipeDelete = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( recipeDelete.contains( "Latte" ) );

        Assertions.assertEquals( 2, service.count(), "There should now be only two recipes" );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( r4 ) ) );

        Assertions.assertEquals( 3, service.count(), "The new recipe can now be added, resulting in 3 total recipes" );

    }

    /**
     * Helper method for creating a recipe
     *
     * @param name
     *            of the recipe
     * @param price
     *            of the recipe
     * @param coffee
     *            amount of coffee
     * @param milk
     *            amount of milk
     * @param chocolate
     *            amount of chocolate
     * @return the created recipe
     */
    private Recipe createRecipe ( final String name, final Integer price, final Integer coffee, final Integer milk,
            final Integer chocolate ) {
        final Recipe recipe = new Recipe();
        recipe.setName( name );
        recipe.setPrice( price );
        recipe.addIngredient( "Coffee", coffee );
        recipe.addIngredient( "Milk", milk );
        if ( chocolate > 0 ) {
            recipe.addIngredient( "Chocolate", chocolate );
        }

        return recipe;
    }

    /**
     * Test deleting a valid recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testDeleteRecipeOk () throws Exception {

        service.deleteAll();
        /*
         * Tests to make sure the delete function is working properly
         */

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0 );
        service.save( r1 );

        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( recipe.contains( "Coffee" ) );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        service.delete( r1 );

        final String recipeDelete = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( recipeDelete.contains( "Coffee" ) );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    /**
     * Test deleting a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testDeleteRecipeOkCY () throws Exception {

        service.deleteAll();
        /*
         * Tests to make sure the delete function is working properly
         */

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0 );
        service.save( r1 );

        final String recipe = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( recipe.contains( "Coffee" ) );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final Recipe r2 = createRecipe( "Coffee2", 60, 5, 2, 0 );
        service.save( r2 );

        final String recipe2 = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( recipe2.contains( "Coffee2" ) );
        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        service.delete( r1 );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be One Recipe in the CoffeeMaker" );

        service.delete( r2 );

        final String recipeDelete = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( recipeDelete.contains( "Coffee" ) );
        Assertions.assertFalse( recipeDelete.contains( "Coffee2" ) );

        Assertions.assertEquals( 0, service.findAll().size(), "There should be no Recipes in the CoffeeMaker" );
    }

    /**
     * Test deleting a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testDeleteRecipeDeleted () throws Exception {
        service.deleteAll();

        /* Tests to check deleting a recipe that has already been deleted */

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0 );
        service.save( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final Recipe r2 = createRecipe( "Mocha", 50, 3, 1, 0 );
        service.save( r2 );
        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        // try deleting a recipe that a concurrent user already deleted
        service.delete( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final String recipeDelete = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( recipeDelete.contains( "\"name\":\"Coffee\",\"price\":50" ) );
        Assertions.assertTrue( recipeDelete.contains( "\"name\":\"Mocha\",\"price\":50" ) );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be one recipe in the database" );
    }

    /**
     * Test deleting a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testDeleteRecipeDeletedCY () throws Exception {
        service.deleteAll();

        /* Tests to check deleting a recipe that has already been deleted */

        final Recipe r1 = createRecipe( "Coffee", 50, 3, 1, 0 );
        service.save( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final Recipe r2 = createRecipe( "Mocha", 50, 3, 2, 0 );
        service.save( r2 );

        final Recipe r3 = createRecipe( "Latte", 10, 3, 3, 0 );
        service.save( r3 );
        Assertions.assertEquals( 3, service.count(), "There should be three recipes in the database" );

        service.delete( r2 );
        Assertions.assertEquals( 2, service.count(), "There should be two recipes in the database" );

        service.delete( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        // try deleting a recipe that a concurrent user already deleted
        service.delete( r1 );
        Assertions.assertEquals( 1, service.count(), "There should be one recipe in the database" );

        final String recipeDelete = mvc.perform( get( "/api/v1/recipes" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertFalse( recipeDelete.contains( "\"name\":\"Coffee\",\"price\":50" ) );
        Assertions.assertTrue( recipeDelete.contains( "\"name\":\"Latte\",\"price\":10" ) );

        Assertions.assertEquals( 1, service.findAll().size(), "There should be one recipe in the database" );
    }

    /**
     * Test deleting a recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testDeleteAPI () throws Exception {
        service.deleteAll();

        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( "Coffee", 1 );
        recipe.addIngredient( "Milk", 20 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        mvc.perform( delete( "/api/v1/recipes/{%s}", "Invalid Recipe" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 1, (int) service.count() );

        mvc.perform( delete( "/api/v1/recipes/{%s}", "Delicious Not-Coffee" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertEquals( 0, (int) service.count() );
    }

    /**
     * Testing edit recipe
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testEditRecipeAPI () throws Exception {
        final Recipe recipe = new Recipe();
        recipe.setName( "Delicious Not-Coffee" );
        recipe.addIngredient( "Coffee", 1 );
        recipe.addIngredient( "Milk", 20 );

        recipe.setPrice( 5 );

        mvc.perform( post( "/api/v1/recipes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( recipe ) ) );

        Assertions.assertEquals( 1, (int) service.count() );

        final Recipe recipeUpdated = new Recipe();

        recipeUpdated.setName( "Delicious Not-Coffee" );
        recipeUpdated.addIngredient( "Coffee", 1 );

        recipeUpdated.setPrice( 6 );

        final String response = mvc
                .perform( put( "/api/v1/recipeEdit" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( recipeUpdated ) ) )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( response.contains( "Delicious Not-Coffee successfully saved" ) );

        // update with a recipe that does not exist
        final Recipe recipeUpdatedInvalid = new Recipe();

        recipeUpdatedInvalid.setName( "NonExistant Not-Coffee" );
        recipeUpdatedInvalid.addIngredient( "Coffee", 1 );

        recipeUpdatedInvalid.setPrice( 6 );

        final String responseInvalid = mvc
                .perform( put( "/api/v1/recipeEdit" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( recipeUpdatedInvalid ) ) )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();

        Assertions.assertTrue( responseInvalid.contains( "could not be found in the system" ) );

    }

}
