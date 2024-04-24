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

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.Recipe;
import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.CoffeeOrderService;
import edu.ncsu.csc.CoffeeMaker.services.RecipeService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Testing the API Coffee Order Controller
 *
 * @author Natalie Meuser
 *
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APICoffeeOrderTest {

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
     * CoffeeOrderService, autowired, allows for manipulating the CoffeeOrder
     * model
     */
    @Autowired
    private CoffeeOrderService    service;

    /**
     * RecipeService, autowired, allows for manipulating the Recipe model
     */
    @Autowired
    private RecipeService         recipeService;

    /**
     * UserService, autowired, allows for manipulating the User model
     */
    @Autowired
    private UserService           userService;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
        recipeService.deleteAll();
        userService.deleteAll();

        final Recipe r2 = new Recipe();
        r2.setName( "CoffeeDrink" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 1 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username2", "pw2" );
        userService.save( c2 );
        final Staff s2 = new Staff( "staffUsername2", "pw2" );
        userService.save( s2 );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );
        service.save( valid );
    }

    /**
     * Tests getting orders
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testGetOrders () throws Exception {
        final String allOrders = mvc.perform( get( "/api/v1/ordersAll" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allOrders.contains( "username2" ) );
        Assertions.assertTrue( allOrders.contains( "CoffeeDrink" ) );
        Assertions.assertTrue( allOrders.contains( "\"status\":false" ) );
        Assertions.assertTrue( allOrders.contains( "\"pickUpStatus\":false" ) );
    }

    /**
     * Tests getting all orders as strings
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testGetOrdersStrings () throws Exception {
        final String allOrders = mvc.perform( get( "/api/v1/orderAllString" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allOrders.contains( "Order of CoffeeDrink, placed by username2. Not yet fulfilled." ) );
    }

    /**
     * Tests getting all of a customer's orders as strings
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testGetCustomerOrdersStrings () throws Exception {
        final String allOrders = mvc.perform( get( "/api/v1/orderAllString/{%s}", "username2" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allOrders.contains( "Order of CoffeeDrink. Not yet fulfilled." ) );
    }

    /**
     * Tests getting all of the uncompleted orders
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testGetOrdersNotCompleted () throws Exception {
        final Recipe r2 = new Recipe();
        r2.setName( "Latte" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 2 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c3 = new Customer( "username3", "pw3" );
        userService.save( c3 );
        final Staff s3 = new Staff( "staffUsername3", "pw3" );
        userService.save( s3 );

        final CoffeeOrder valid = new CoffeeOrder( r2, c3 );
        valid.fulfilledOrder( s3 );
        valid.pickUpOrder();
        service.save( valid );

        final String allOrders = mvc.perform( get( "/api/v1/ordersNotCompleted" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allOrders.contains( "CoffeeDrink" ) );
        Assertions.assertTrue( allOrders.contains( "\"status\":false" ) );
        Assertions.assertFalse( allOrders.contains( "Latte" ) );
    }

    /**
     * Test getting all of the orders not picked up by a customer
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testGetOrdersNotPickedUp () throws Exception {
        final Recipe r2 = new Recipe();
        r2.setName( "Latte" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 2 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username3", "pw3" );
        userService.save( c2 );
        final Staff s3 = new Staff( "staffUsername3", "pw3" );
        userService.save( s3 );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );
        valid.fulfilledOrder( s3 );
        service.save( valid );

        final String allOrders2 = mvc.perform( get( "/api/v1/ordersNotPickedUp/{%s}", "username2" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allOrders2.contains( "CoffeeDrink" ) );
        Assertions.assertTrue( allOrders2.contains( "\"status\":false" ) );
        Assertions.assertTrue( allOrders2.contains( "username2" ) );
        Assertions.assertFalse( allOrders2.contains( "Latte" ) );

        final String allOrders3 = mvc.perform( get( "/api/v1/ordersNotPickedUp/{%s}", "username3" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertFalse( allOrders3.contains( "CoffeeDrink" ) );
        Assertions.assertTrue( allOrders3.contains( "Latte" ) );
        Assertions.assertTrue( allOrders3.contains( "\"status\":true" ) );
        Assertions.assertTrue( allOrders3.contains( "\"pickUpStatus\":false" ) );
        Assertions.assertTrue( allOrders3.contains( "username3" ) );
    }

    /**
     * Tests adding an order to the database
     *
     * @throws Exception
     *             if issue occurs
     */
    @Test
    @Transactional
    public void testAddOrderToDatabase () throws Exception {
        final Recipe r2 = new Recipe();
        r2.setName( "Latte" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 2 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username3", "pw3" );
        userService.save( c2 );
        final Staff s3 = new Staff( "staffUsername3", "pw3" );
        userService.save( s3 );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );

        // mvc.perform( put( "/api/v1/orderAdd" ).contentType(
        // MediaType.APPLICATION_JSON )
        // .content( TestUtils.asJsonString( valid ) ) ).andExpect(
        // status().isOk() );
        service.save( valid );

        final String allOrders = mvc.perform( get( "/api/v1/orderAllString" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allOrders.contains( "Order of CoffeeDrink, placed by username2." ) );
        Assertions.assertTrue( allOrders.contains( "Order of Latte, placed by username3." ) );
    }

    /**
     * Tests getting a specific coffee order
     *
     * @throws Exception
     *             is issue occurs
     */
    @Test
    @Transactional
    public void testGetCoffeeOrder () throws Exception {
        final Recipe r2 = new Recipe();
        r2.setName( "Latte" );
        r2.setPrice( 1 );
        r2.addIngredient( "Coffee", 2 );
        r2.addIngredient( "Milk", 1 );
        recipeService.save( r2 );

        final Customer c2 = new Customer( "username3", "pw3" );
        userService.save( c2 );
        final Staff s3 = new Staff( "staffUsername3", "pw3" );
        userService.save( s3 );

        final CoffeeOrder valid = new CoffeeOrder( r2, c2 );

        final String getInvalidResult = mvc.perform( get( "/api/v1/order/{%s}", "25" ) ).andDo( print() )
                .andExpect( status().isNotFound() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( getInvalidResult.contains( "No order found." ) );

        valid.fulfilledOrder( s3 );
        service.save( valid );

        final String s = "" + valid.getId();

        final String getValidResult = mvc.perform( get( "/api/v1/order/{%s}", s ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( getValidResult.contains( "Latte" ) );

    }
}
