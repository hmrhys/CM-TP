package edu.ncsu.csc.CoffeeMaker.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

@ExtendWith ( SpringExtension.class )
@SpringBootTest
@AutoConfigureMockMvc
public class APIUserTest {

    @Autowired
    private MockMvc               mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService           service;

    private final String          userJson = "{\"username\": \"testUser\", \"password\": \"password123\"}";

    @BeforeEach
    public void setup () {
        mockMvc = MockMvcBuilders.webAppContextSetup( context ).build();

        service.deleteAll();
    }

    // // Example test for getting a user by username
    // @Transactional
    // @Test
    // public void testGetUserByUsername () throws Exception {
    // final String username = "testUser";
    // final Customer c1 = new Customer( "customer111", "123456Pw" );
    //
    // // Assuming User class has setUsername method and constructor as needed
    //
    // Mockito.when( service.findByUsername( username ) ).thenReturn( c1 );
    //
    // mockMvc.perform( get( "/api/v1/user/" + username ) ).andExpect(
    // status().isOk() )
    // .andExpect( jsonPath( "$.username" ).value( username ) );
    // }

    // Example test for creating a user
    @Test
    @Transactional
    public void testGetAllUsers () throws Exception {
        mockMvc.perform( get( "/api/v1/user" ) ).andExpect( status().isOk() ).andExpect( jsonPath( "$" ).isArray() );
    }

    @Test
    @Transactional
    public void testGetUserByUsername () throws Exception {
        final User user = new User( "testUser", "password123" );
        service.save( user );

        // This test was corrected to use the username path variable, assuming
        // "testUser" exists
        mockMvc.perform( get( "/api/v1/user/{username}", "testUser" ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.username" ).value( "testUser" ) );
    }

    @Test
    @Transactional
    public void testCreateUser () throws Exception {
        final User user = new User( "testUser", "password123" );

        mockMvc.perform( post( "/api/v1/user" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( user ) ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 1, (int) service.count() );

        // mockMvc.perform( post( "/api/v1/user" ).contentType(
        // MediaType.APPLICATION_JSON ).content( userJson ) )
        // .andExpect( status().isOk() );
    }

    @Test
    @Transactional
    public void testDeleteUserByUsername () throws Exception {
        final User user = new User( "testUser", "password123" );
        service.save( user );

        Assertions.assertEquals( 1, (int) service.count() );

        mockMvc.perform( delete( "/api/v1/user/{username}", "testUser" ) ).andExpect( status().isOk() );

        Assertions.assertEquals( 0, (int) service.count() );
    }

    @Test
    @Transactional
    public void testGetAllUsersWhenNoneExist () throws Exception {
        mockMvc.perform( get( "/api/v1/user" ) ).andExpect( status().isOk() ).andExpect( jsonPath( "$" ).isEmpty() );
    }

    @Test
    @Transactional
    public void testGetNonExistentUser () throws Exception {
        final String nonExistentUsername = "nonExistentUser";
        mockMvc.perform( get( "/api/v1/user/{username}", nonExistentUsername ) ).andExpect( status().isNotFound() );
    }

    @Test
    @Transactional
    public void testCreateDuplicateUser () throws Exception {
        final User user = new User( "duplicateUser", "password123" );
        service.save( user ); // Save the user first time

        mockMvc.perform( post( "/api/v1/user" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( user ) ) ).andExpect( status().isConflict() );
    }

    @Test
    @Transactional
    public void testDeleteNonExistentUser () throws Exception {
        final String nonExistentUsername = "nonExistentUser";
        mockMvc.perform( delete( "/api/v1/user/{username}", nonExistentUsername ) ).andExpect( status().isNotFound() );
    }
}
