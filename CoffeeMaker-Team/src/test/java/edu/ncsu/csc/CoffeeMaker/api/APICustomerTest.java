package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;

/**
 * Tests APICustomerController, and CustomerController functionalities, ensures
 * that Customer API Endpoints are functioning as expected and performing their
 * necessary tasks.
 *
 * @author hmreese2
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APICustomerTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    @Autowired
    private MockMvc               mvc;

    /**
     * Instance of WebApplicationContext used to perform web requests from the
     * application with details from the request
     */
    @Autowired
    private WebApplicationContext context;

    /**
     * Instance of Customer Service to use to check and manage the current
     * Customer users in the customer database
     */
    @Autowired
    private CustomerService       customerService;

    /**
     * Reinitializes mvc and clears customerService before the start of every
     * test.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        customerService.deleteAll();
    }

    /////////////////////////////////////
    // TESTS FOR APICUSTOMERCONTROLLER //
    /////////////////////////////////////

    /**
     * Tests endpoint getting all customers from the database when there are no
     * customers and when there already are some customers.
     *
     * @throws Exception
     *             if there is an issue with the request when retrieving all
     *             customers
     */
    @Test
    @Transactional
    public void testGetAllCustomers () throws Exception {
        /////////////
        // EMPTY TEST
        /////////////

        // check there are no customers
        Assertions.assertEquals( 0, customerService.count() );
        // test when there are no customers
        mvc.perform( get( "/api/v1/user/customer" ) ).andExpect( status().isOk() )
                .andExpect( content().string( "[]" ) );

        // add some customers
        customerService.save( new Customer( "bobbielee", "password" ) );
        customerService.save( new Customer( "davielocket", "password" ) );
        customerService.save( new Customer( "terriejoe", "password" ) );

        Assertions.assertEquals( 3, customerService.count() );

        /////////////////
        // NON-EMPTY TEST
        /////////////////

        // test with existing customers
        final String allCustomers = mvc.perform( get( "/api/v1/user/customer" ) ).andDo( print() )
                .andExpect( status().isOk() ).andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allCustomers.contains( "bobbielee" ) );
        Assertions.assertTrue( allCustomers.contains( "davielocket" ) );
        Assertions.assertTrue( allCustomers.contains( "terriejoe" ) );

        // more in-depth testing (for specific details)
        mvc.perform( get( "/api/v1/user/customer" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 3 ) )
                .andExpect( jsonPath( "$[0].username" ).value( "bobbielee" ) )
                .andExpect( jsonPath( "$[1].username" ).value( "davielocket" ) )
                .andExpect( jsonPath( "$[2].username" ).value( "terriejoe" ) );

    }

    /**
     * Tests endpoint getting a single customer by their username when that user
     * does not exist and when they do exist.
     *
     * @throws Exception
     *             if there is an issue with the request when retrieving a
     *             customer by their username
     */
    @Test
    @Transactional
    public void testGetSingleCustomer () throws Exception {
        ///////////////
        // INVALID TEST
        ///////////////

        // check there are no customers
        Assertions.assertEquals( 0, customerService.count() );

        // test with non-existant user
        mvc.perform( get( "/api/v1/user/customer/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "No user found with name bobbielee" ) );

        /////////////
        // VALID TEST
        /////////////

        // add customer
        customerService.save( new Customer( "bobbielee", "password" ) );
        customerService.save( new Customer( "davielocket", "password" ) );
        customerService.save( new Customer( "terriejoe", "password" ) );

        Assertions.assertEquals( 3, customerService.count() );

        // test getting certain customers
        mvc.perform( get( "/api/v1/user/customer/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "bobbielee" ) );

        mvc.perform( get( "/api/v1/user/customer/{%s}", "davielocket" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "davielocket" ) );

        mvc.perform( get( "/api/v1/user/customer/{%s}", "terriejoe" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "terriejoe" ) );

    }

    /**
     * Tests endpoint creating a customer account when there is already an
     * existing customer with the attempted username, and where there is no
     * issue creating the customer user. Ensures that the customer is properly
     * stored in the database upon creating.
     *
     * @throws Exception
     *             if there is an issue with the request when posting/creating
     *             new customer
     */
    @Test
    @Transactional
    public void testCreateCustomer () throws Exception {
        // initial tests check there are no customers
        Assertions.assertEquals( 0, customerService.count() );

        ///////////////
        // INVALID TEST
        ///////////////

        // make user to test duplicate with
        customerService.save( new Customer( "bobbielee", "password" ) );
        Assertions.assertEquals( 1, customerService.count() );
        final String jsonReqDupe = "{\"username\":\"bobbielee\", \"password\":\"password\"}";
        // test with non-existant user
        mvc.perform( post( "/api/v1/user/customer" ).contentType( MediaType.APPLICATION_JSON ).content( jsonReqDupe ) )
                .andExpect( status().isConflict() )
                .andExpect( jsonPath( "$.message" ).value( "Customer with the username bobbielee already exists." ) );

        /////////////
        // VALID TEST
        /////////////

        // test creating non-existing user
        final Customer c = new Customer( "davielocket", "password" );
        final String jsonReq = "{\"username\":\"davielocket\", \"password\":\"password\"}";

        mvc.perform( post( "/api/v1/user/customer" ).contentType( MediaType.APPLICATION_JSON ).content( jsonReq ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( "davielocket successfully created." ) );

        // check that customer is in database now
        final Customer checkingCustomer = customerService.findByUsername( "davielocket" );
        Assertions.assertNotNull( checkingCustomer );
        Assertions.assertEquals( "davielocket", checkingCustomer.getUsername() );
        Assertions.assertEquals( c.getUsername(), checkingCustomer.getUsername() );
        Assertions.assertEquals( 2, customerService.count() );
        Assertions.assertEquals( "bobbielee", customerService.findAll().get( 0 ).getUsername() );
        Assertions.assertEquals( "davielocket", customerService.findAll().get( 1 ).getUsername() );
    }

    /**
     * Tests endpoint deleting a customer when that customer does not exist, and
     * when they do exist. Ensures that the deletion is properly recorded in the
     * database (the database is updated as expected).
     *
     * @throws Exception
     *             if there is an issue with the request when deleting customer
     */
    @Test
    @Transactional
    public void testDeleteCustomer () throws Exception {
        ///////////////
        // INVALID TEST
        ///////////////

        // check there are no customers
        Assertions.assertEquals( 0, customerService.count() );

        // test with non-existant user
        mvc.perform( delete( "/api/v1/user/customer/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "No customer found for name bobbielee" ) );

        /////////////
        // VALID TEST
        /////////////

        // add customer
        customerService.save( new Customer( "bobbielee", "password" ) );
        customerService.save( new Customer( "davielocket", "password" ) );
        customerService.save( new Customer( "terriejoe", "password" ) );

        Assertions.assertEquals( 3, customerService.count() );

        // test getting certain customers
        mvc.perform( delete( "/api/v1/user/customer/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( "bobbielee was deleted successfully" ) );

        Assertions.assertEquals( 2, customerService.count() );

    }

    //////////////////////////////////
    // TESTS FOR CUSTOMERCONTROLLER //
    //////////////////////////////////

    // /**
    // * Tests endpoint accessing customerindex.html page
    // *
    // * @throws Exception
    // * if there is an issue with the request when retrieving
    // * customerindex page
    // */
    // @Test
    // @WithMockUser ( username = "davielocket", roles = { "CUSTOMER" } )
    // public void testCustomerIndex () throws Exception {
    // mvc.perform( get( "/customerindex" ) ).andExpect( status().isOk() )
    // .andExpect( view().name( "customerindex" ) );
    // }

    /**
     * Tests endpoint accessing the orderstatus html page as a customer
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             customer-orderstatus page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "CUSTOMER" } )
    public void testViewOrderStatus () throws Exception {
        mvc.perform( get( "/customer-orderstatus.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "customer-orderstatus" ) );
    }

    /**
     * Tests endpoint accessing the vieworders html page as a customer
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             customer-vieworders page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "CUSTOMER" } )
    public void testViewOrders () throws Exception {
        mvc.perform( get( "/customer-vieworders.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "customer-vieworders" ) );
    }

    /**
     * Tests endpoint accessing the placeorder html page as a customer
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             customer-placeorder page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "CUSTOMER" } )
    public void testPlaceOrder () throws Exception {
        mvc.perform( get( "/customer-placeorder.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "customer-placeorder" ) );
    }

}
