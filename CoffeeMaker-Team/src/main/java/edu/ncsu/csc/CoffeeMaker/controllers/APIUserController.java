package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.User;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.UserService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for User.
 *
 * Spring will automatically convert all of the ResponseEntity and List results
 * to JSON
 *
 * @author Kai Presler-Marshall
 * @author Michelle Lemons
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APIUserController extends APIController {

    /**
     * UserService object, to be autowired in by Spring to allow for
     * manipulating the User model
     */
    @Autowired
    private UserService     service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * REST API method to provide GET access to all user in the system
     *
     * @return JSON representation of all User
     */
    @GetMapping ( BASE_PATH + "/user" )
    public List<User> getUsers () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific user, as indicated
     * by the path variable provided (the name of the user desired)
     *
     * @param username
     *            user username
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/user/{username}" )
    public ResponseEntity getUser ( @PathVariable ( "username" ) final String username ) {
        final User user = service.findByUsername( username );
        return null == user
                ? new ResponseEntity( errorResponse( "No user found with name " + username ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( user, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the User model. This is
     * used to create a new User by automatically converting the JSON
     * RequestBody provided to a User object. Invalid JSON will fail.
     *
     * @param user
     *            The valid User to be saved.
     * @return ResponseEntity indicating success if the Customer could be saved
     *         to the database, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/user" )
    public ResponseEntity createUser ( @RequestBody final User user ) {
        if ( null != service.findByUsername( user.getUsername() ) ) {
            return new ResponseEntity(
                    errorResponse( "User with the username " + user.getUsername() + " already exists." ),
                    HttpStatus.CONFLICT );
        }

        user.addRole( Role.USER );

        user.setPassword( passwordEncoder.encode( user.getPassword() ) );

        service.save( user );
        return new ResponseEntity( successResponse( user.getUsername() + " successfully created." ),
                HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a User from the CoffeeMaker's Users,
     * by making a DELETE request to the API endpoint and indicating the User
     * to delete (as a path variable)
     *
     * @param username
     *            The username of the User to delete
     * @return Success if the User could be deleted; an error if the User does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/user/{username}" )
    public ResponseEntity deleteUser ( @PathVariable final String username ) {
        final User user = service.findByUsername( username );
        if ( null == user ) {
            return new ResponseEntity( errorResponse( "No user found for name " + username ), HttpStatus.NOT_FOUND );
        }
        service.delete( user );

        return new ResponseEntity( successResponse( username + " was deleted successfully" ), HttpStatus.OK );
    }
}
