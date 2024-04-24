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

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

/**
 * Tests APIStaffController, and StaffController functionalities, ensures that
 * Staff API Endpoints are functioning as expected and performing their
 * necessary tasks.
 *
 * @author hmreese2
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APIStaffTest {

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
     * Instance of Staff Service to use to check and manage the current Staff
     * users in the Staff database
     */
    @Autowired
    private StaffService          staffService;

    /**
     * Reinitializes mvc and clears staffService before the start of every test.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        staffService.deleteAll();
    }

    //////////////////////////////////
    // TESTS FOR APISTAFFCONTROLLER //
    //////////////////////////////////

    /**
     * Tests endpoint getting all staff members from the database when there are
     * no staff members and when there already are some staff.
     *
     * @throws Exception
     *             if there is an issue with the request when retrieving all
     *             staff members
     */
    @Test
    @Transactional
    public void testGetAllStaff () throws Exception {
        /////////////
        // EMPTY TEST
        /////////////

        // check there are no customers
        Assertions.assertEquals( 0, staffService.count() );
        // test when there are no customers
        mvc.perform( get( "/api/v1/user/staff" ) ).andExpect( status().isOk() ).andExpect( content().string( "[]" ) );

        // add some customers
        staffService.save( new Staff( "bobbielee", "password" ) );
        staffService.save( new Staff( "davielocket", "password" ) );
        staffService.save( new Staff( "terriejoe", "password" ) );

        Assertions.assertEquals( 3, staffService.count() );

        /////////////////
        // NON-EMPTY TEST
        /////////////////

        // test with existing customers
        final String allStaff = mvc.perform( get( "/api/v1/user/staff" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        Assertions.assertTrue( allStaff.contains( "bobbielee" ) );
        Assertions.assertTrue( allStaff.contains( "davielocket" ) );
        Assertions.assertTrue( allStaff.contains( "terriejoe" ) );

        // more in-depth testing (for specific details)
        mvc.perform( get( "/api/v1/user/staff" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.length()" ).value( 3 ) )
                .andExpect( jsonPath( "$[0].username" ).value( "bobbielee" ) )
                .andExpect( jsonPath( "$[1].username" ).value( "davielocket" ) )
                .andExpect( jsonPath( "$[2].username" ).value( "terriejoe" ) );

    }

    /**
     * Tests endpoint getting a single staff by their username when that user
     * does not exist and when they do exist.
     *
     * @throws Exception
     *             if there is an issue with the request when retrieving a staff
     *             by their username
     */
    @Test
    @Transactional
    public void testGetSingleStaff () throws Exception {
        ///////////////
        // INVALID TEST
        ///////////////

        // check there are no customers
        Assertions.assertEquals( 0, staffService.count() );

        // test with non-existant user
        mvc.perform( get( "/api/v1/user/staff/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "No user found with name bobbielee" ) );

        /////////////
        // VALID TEST
        /////////////

        // add customer
        staffService.save( new Staff( "bobbielee", "password" ) );
        staffService.save( new Staff( "davielocket", "password" ) );
        staffService.save( new Staff( "terriejoe", "password" ) );

        Assertions.assertEquals( 3, staffService.count() );

        // test getting certain customers
        mvc.perform( get( "/api/v1/user/staff/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "bobbielee" ) );

        mvc.perform( get( "/api/v1/user/staff/{%s}", "davielocket" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "davielocket" ) );

        mvc.perform( get( "/api/v1/user/staff/{%s}", "terriejoe" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "terriejoe" ) );

    }

    /**
     * Tests endpoint creating a staff account when there is already an existing
     * staff with the attempted username, and where there is no issue creating
     * the staff user. Ensures that the staff is properly stored in the database
     * upon creating.
     *
     * @throws Exception
     *             if there is an issue with the request when posting/creating
     *             new staff
     */
    @Test
    @Transactional
    public void testCreateStaffMember () throws Exception {
        // initial tests check there are no customers
        Assertions.assertEquals( 0, staffService.count() );

        ///////////////
        // INVALID TEST
        ///////////////

        // make user to test duplicate with
        staffService.save( new Staff( "bobbielee", "password" ) );
        Assertions.assertEquals( 1, staffService.count() );
        final String jsonReqDupe = "{\"username\":\"bobbielee\", \"password\":\"password\"}";
        // test with non-existant user
        mvc.perform( post( "/api/v1/user/staff" ).contentType( MediaType.APPLICATION_JSON ).content( jsonReqDupe ) )
                .andExpect( status().isConflict() )
                .andExpect( jsonPath( "$.message" ).value( "Staff with the username bobbielee already exists." ) );

        /////////////
        // VALID TEST
        /////////////

        // test creating non-existing user
        final Staff s = new Staff( "davielocket", "password" );
        final String jsonReq = "{\"username\":\"davielocket\", \"password\":\"password\"}";

        mvc.perform( post( "/api/v1/user/staff" ).contentType( MediaType.APPLICATION_JSON ).content( jsonReq ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( "davielocket successfully created." ) );

        // check that customer is in database now
        final Staff checkingStaff = staffService.findByUsername( "davielocket" );
        Assertions.assertNotNull( checkingStaff );
        Assertions.assertEquals( "davielocket", checkingStaff.getUsername() );
        Assertions.assertEquals( s.getUsername(), checkingStaff.getUsername() );
        Assertions.assertEquals( 2, staffService.count() );
        Assertions.assertEquals( "bobbielee", staffService.findAll().get( 0 ).getUsername() );
        Assertions.assertEquals( "davielocket", staffService.findAll().get( 1 ).getUsername() );
    }

    /**
     * Tests endpoint deleting a staff when that staff does not exist, and when
     * they do exist. Ensures that the deletion is properly recorded in the
     * database (the database is updated as expected).
     *
     * @throws Exception
     *             if there is an issue with the request when deleting staff
     */
    @Test
    @Transactional
    public void testDeleteStaff () throws Exception {
        ///////////////
        // INVALID TEST
        ///////////////

        // check there are no customers
        Assertions.assertEquals( 0, staffService.count() );

        // test with non-existant user
        mvc.perform( delete( "/api/v1/user/staff/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() )
                .andExpect( jsonPath( "$.message" ).value( "No staff found for name bobbielee" ) );

        /////////////
        // VALID TEST
        /////////////

        // add customer
        staffService.save( new Staff( "bobbielee", "password" ) );
        staffService.save( new Staff( "davielocket", "password" ) );
        staffService.save( new Staff( "terriejoe", "password" ) );

        Assertions.assertEquals( 3, staffService.count() );

        // test getting certain customers
        mvc.perform( delete( "/api/v1/user/staff/{%s}", "bobbielee" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( jsonPath( "$.message" ).value( "bobbielee was deleted successfully" ) );

        Assertions.assertEquals( 2, staffService.count() );

    }

    ///////////////////////////////
    // TESTS FOR STAFFCONTROLLER //
    ///////////////////////////////

    /**
     * Tests endpoint accessing the index html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-index page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testStaffIndex () throws Exception {
        mvc.perform( get( "/staff-index.html" ) ).andExpect( status().isOk() ).andExpect( view().name( "staffindex" ) );
    }

    /**
     * Tests endpoint accessing the addingredients html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-addingredients page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testAddIngredients () throws Exception {
        mvc.perform( get( "/staff-addingredients.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-addingredients" ) );
    }

    /**
     * Tests endpoint accessing the addinventory html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-addinventory page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testAddInventory () throws Exception {
        mvc.perform( get( "/staff-addinventory.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-addinventory" ) );
    }

    /**
     * Tests endpoint accessing the addrecipe html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-addrecipe page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testAddRecipe () throws Exception {
        mvc.perform( get( "/staff-addrecipe.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-addrecipe" ) );
    }

    /**
     * Tests endpoint accessing the editrecipe html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-editrecipe page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testEditRecipe () throws Exception {
        mvc.perform( get( "/staff-editrecipe.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-editrecipe" ) );
    }

    /**
     * Tests endpoint accessing the deleterecipe html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-deleterecipe page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testDeleteRecipe () throws Exception {
        mvc.perform( get( "/staff-deleterecipe.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-deleterecipe" ) );
    }

    /**
     * Tests endpoint accessing the fulfillorders html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-fulfillorders page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testFulfillOrder () throws Exception {
        mvc.perform( get( "/staff-fulfillorders.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-fulfillorders" ) );
    }

    /**
     * Tests endpoint accessing the allorders html page as a staff member
     *
     * @throws Exception
     *             if there is an issue with the request when accessing the
     *             staff-allorders page
     */
    @Test
    @WithMockUser ( username = "davielocket", roles = { "STAFF" } )
    public void testAllOrders () throws Exception {
        mvc.perform( get( "/staff-allorders.html" ) ).andExpect( status().isOk() )
                .andExpect( view().name( "staff-allorders" ) );
    }

}
