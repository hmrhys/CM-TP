package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
import edu.ncsu.csc.CoffeeMaker.services.CustomUserDetailsService;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * Tests LoginController functionality, ensures that login pages are directing
 * as they are supposed to (for valid and invalid input).
 *
 * @author hmreese2
 */
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith ( SpringExtension.class )
public class APILoginTest {

    /**
     * MockMvc uses Spring's testing framework to handle requests to the REST
     * API
     */
    @Autowired
    private MockMvc                  mvc;

    /**
     * Instance of WebApplicationContext used to perform web requests from the
     * application with details from the request
     */
    @Autowired
    private WebApplicationContext    context;

    @Autowired
    private UserService              userService;

    @Autowired
    private CustomerService          customerService;

    @Autowired
    private StaffService             staffService;

    @Autowired
    private UserDetailsService       userDetailsService;

    @Autowired
    private CustomUserDetailsService cuds;

    @Autowired
    private CustomerRepository       cr;

    @Autowired
    private PasswordEncoder          passwordEncoder;

    /**
     * Sets up the tests.
     */
    @BeforeEach
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();

        customerService.deleteAll();

        final String pwd = passwordEncoder.encode( "password" );

        final Customer c = new Customer( "boblee", pwd );
        customerService.save( c );
    }

    /**
     * Tests logging in with invalid credentials. User should be redirected to
     * error page.
     *
     * @throws Exception
     *             this is thrown when user logs in with invalid credentials
     *             that are not accepted by and do not match with any of the
     *             logged credentials in the database.
     */
    @Test
    @Transactional
    public void testLoginErrorRedirection () throws Exception {

        // attempt to log in
        mvc.perform( post( "/perform_login" ).param( "username", "davielocket" ).param( "password", "password" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect( status().is3xxRedirection() )
                .andExpect( redirectedUrl( "/login.html?error" ) );

        userService.deleteAll();
    }

    @Test
    @Transactional
    public void testCustomerErrorRedirection () throws Exception {
        // attempt logging into customer portal
        mvc.perform( post( "/perform_login" ).param( "username", "davielocket" ).param( "password", "password" )
                .header( "Referer", "http://localhost:8080/customerlogin.html" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect( status().is3xxRedirection() )
                .andExpect( redirectedUrl( "/customerlogin.html?error" ) );

        userService.deleteAll();
    }

    @Test
    @Transactional
    public void testStaffErrorRedirection () throws Exception {
        // attempt logging into staff portal
        mvc.perform( post( "/perform_login" ).param( "username", "davielocket" ).param( "password", "password" )
                .header( "Referer", "http://localhost:8080/stafflogin.html" )
                .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect( status().is3xxRedirection() )
                .andExpect( redirectedUrl( "/stafflogin.html?error" ) );

        userService.deleteAll();
    }

    // @Test
    // @Transactional
    // public void testCustomerSuccess () throws Exception {
    //
    // final String hashedPwd = passwordEncoder.encode( "password" );
    // final Customer c1 = new Customer( "terrijoe", hashedPwd );
    // customerService.save( c1 );
    //
    // Assertions.assertNotNull( mvc );
    //
    // mvc.perform( post( "/perform_login" ).param( "username", "terrijoe"
    // ).param( "password", "password" )
    // .header( "Referer", "http://localhost:8080/customerlogin.html" )
    // .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect(
    // status().is3xxRedirection() )
    // .andExpect( redirectedUrl( "/customerindex.html" ) );
    //
    // userService.deleteAll();
    // }

    @Test
    @Transactional
    public void testing () throws Exception {

        final String hashedPwd = passwordEncoder.encode( "password" );
        final Customer c1 = new Customer( "terrijoe", hashedPwd );
        customerService.save( c1 );

        // testing out roles information with User
        final User u = new User( "mayleejay", "password" );
        c1.addRole( Role.CUSTOMER );
        Assertions.assertEquals( 1, c1.getRoles().size() );

        // Simulate authentication with a user having ROLE_CUSTOMER
        // Assertions.assertNotNull( cuds.loadUserByUsername( "terrijoe" ) );
        // final UserDetails ud = cuds.loadUserByUsername( "terrijoe" );
        // final Authentication authentication = new
        // UsernamePasswordAuthenticationToken( ud, null );

        final UserDetails userDetails = userDetailsService.loadUserByUsername( "terrijoe" );
        final Authentication authentication = new UsernamePasswordAuthenticationToken( userDetails, null );

        Assertions.assertNotNull( authentication.getAuthorities() );

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication( authentication );
        SecurityContextHolder.setContext( securityContext );

        // SecurityContextHolder.getContext().setAuthentication( authentication
        // );

        // mvc.perform( post( "/perform_login" ).param( "username", "terrijoe"
        // ).param( "password", "password" )
        // .header( "Referer", "http://localhost:8080/customerlogin.html" )
        // .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect(
        // status().is3xxRedirection() )
        // .andExpect( redirectedUrl( "/customerindex.html" ) );

        userService.deleteAll();
    }

    // @Test
    // @Transactional
    // public void testCustomerRedirection () throws Exception {
    // // check that user is saved in database correctly
    // Assertions.assertEquals( 1, userService.count() );
    // Assertions.assertEquals( 1, customerService.count() );
    //
    // // hash password
    // final String hashedPwd = passwordEncoder.encode( "password" );
    //
    // // testing
    // final Customer c2 = new Customer( "davielocket", hashedPwd );
    // customerService.save( c2 );
    // Assertions.assertEquals( 2, customerService.count() );
    // Assertions.assertEquals( c2, customerService.findByUsername(
    // "davielocket" ) );
    // Assertions.assertEquals( customerService.findAll().get( 0 ),
    // customerService.findByUsername( "boblee" ) );
    // Assertions.assertEquals( c2, customerService.findAll().get( 1 ) );
    // Assertions.assertNotNull( cr.findByUsername( "davielocket" ) );
    //
    // // check that user is in authenticated (invalid)
    // Assertions.assertFalse( customerService.authenticateUser( "idontexist",
    // "password" ) );
    // // check that user is in authenticated (valid)
    // Assertions.assertTrue( customerService.authenticateUser( "davielocket",
    // "password" ) );
    // Assertions.assertTrue( customerService.authenticateUser( "boblee",
    // "password" ) );
    //
    // mvc.perform( post( "/perform_login" ).param( "username", "davielocket"
    // ).param( "password", "password" )
    // .header( "Referer", "http://localhost:8080/customerlogin.html" )
    // .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect(
    // status().is3xxRedirection() )
    // .andExpect( redirectedUrl( "/customerindex" ) );
    //
    // userService.deleteAll();
    // }

    // @Test
    // @Transactional
    // public void test () throws Exception {
    // // check that user is saved in database correctly
    // Assertions.assertEquals( 1, userService.count() );
    // Assertions.assertEquals( 1, customerService.count() );
    //
    // // hash password
    // final String hashedPwd = passwordEncoder.encode( "password" );
    //
    // // testing
    // final Customer c2 = new Customer( "davielocket", hashedPwd );
    // customerService.save( c2 );
    // Assertions.assertEquals( 2, customerService.count() );
    // Assertions.assertEquals( c2, customerService.findByUsername(
    // "davielocket" ) );
    // Assertions.assertEquals( customerService.findAll().get( 0 ),
    // customerService.findByUsername( "boblee" ) );
    // Assertions.assertEquals( c2, customerService.findAll().get( 1 ) );
    // Assertions.assertNotNull( cr.findByUsername( "davielocket" ) );
    //
    // // check that user is in authenticated (invalid)
    // Assertions.assertFalse( customerService.authenticateUser( "idontexist",
    // "password" ) );
    // // check that user is in authenticated (valid)
    // Assertions.assertTrue( customerService.authenticateUser( "davielocket",
    // "password" ) );
    // Assertions.assertTrue( customerService.authenticateUser( "boblee",
    // "password" ) );
    //
    // // MockHttpServletRequest
    // final MockHttpServletRequest request = new MockHttpServletRequest();
    // request.setMethod( "POST" );
    // request.setRequestURI( "/perform_login" );
    // request.addHeader( "Referer", "http://localhost:8080/customerlogin.html"
    // );
    //
    // mvc.perform( post( "/perform_login" ).param( "username", "davielocket"
    // ).param( "password", "password" )
    // .header( "Referer", "http://localhost:8080/customerlogin.html" )
    // .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect(
    // status().is3xxRedirection() )
    // .andExpect( redirectedUrl( "/customerindex" ) );
    //
    // userService.deleteAll();
    // }

    // @Test
    // @Transactional
    // public void testCustomerRedirectionSuccess () throws Exception {
    // // check that user is saved in database correctly
    // Assertions.assertEquals( 1, userService.count() );
    // Assertions.assertEquals( 1, customerService.count() );
    //
    // // hash password
    // final String hashedPwd = passwordEncoder.encode( "password" );
    //
    // // testing
    // final Customer c2 = new Customer( "davielocket", hashedPwd );
    // customerService.save( c2 );
    // Assertions.assertEquals( 2, customerService.count() );
    // Assertions.assertEquals( c2, customerService.findByUsername(
    // "davielocket" ) );
    // Assertions.assertEquals( customerService.findAll().get( 0 ),
    // customerService.findByUsername( "boblee" ) );
    // Assertions.assertEquals( c2, customerService.findAll().get( 1 ) );
    // Assertions.assertNotNull( cr.findByUsername( "davielocket" ) );
    //
    // // check that user is in authenticated (invalid)
    // Assertions.assertFalse( customerService.authenticateUser( "idontexist",
    // "password" ) );
    // // check that user is in authenticated (valid)
    // Assertions.assertTrue( customerService.authenticateUser( "davielocket",
    // "password" ) );
    // Assertions.assertTrue( customerService.authenticateUser( "boblee",
    // "password" ) );
    //
    // // Perform login request
    // final MockHttpServletRequestBuilder requestBuilder = post(
    // "/perform_login" ).param( "username", "davielocket" )
    // .param( "password", "password" ).header( "Referer",
    // "http://localhost:8080/customerlogin.html" )
    // .contentType( MediaType.APPLICATION_FORM_URLENCODED );
    //
    // mvc.perform( requestBuilder ).andExpect( status().is3xxRedirection() )
    // .andExpect( redirectedUrl( "/customerindex" ) );
    //
    // userService.deleteAll();
    // }

    // @Test
    // @Transactional
    // public void testStaffLoginRedirect () throws Exception {
    // // Create a staff user for testing
    // final Staff staffUser = new Staff( "worker12", "password" );
    // staffService.save( staffUser );
    //
    // // Assert that the staff user is authenticated
    // Assertions.assertTrue( staffService.authenticateUser( "worker12",
    // "password" ) );
    //
    // mvc.perform( post( "/perform_login" ).param( "username", "worker12"
    // ).param( "password", "password" )
    // .contentType( MediaType.APPLICATION_FORM_URLENCODED ) ).andExpect(
    // status().is3xxRedirection() )
    // .andExpect( redirectedUrl( "/staffindex" ) );
    // }

}
