package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.Customer;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.repositories.CustomerRepository;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.GuestService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Customer.
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
public class APICustomerController extends APIController {

    /**
     * CustomerService object, to be autowired in by Spring to allow for
     * manipulating the Customer model
     */
    @Autowired
    private CustomerService    service;

    /**
     * GuestService object, to be autowired in by Spring to allow for
     * manipulating the Customer model
     */
    @Autowired
    private GuestService       guestService;

    /**
     * CustomerRepository
     */
    @Autowired
    private CustomerRepository repository;

    @Autowired
    private PasswordEncoder    passwordEncoder;

    /**
     * REST API method to provide GET access to all staff in the system
     *
     * @return JSON representation of all Customer
     */
    @GetMapping ( BASE_PATH + "/user/customer" )
    public List<Customer> getCustomers () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to all guests in the system
     *
     * @return JSON representation of all Guest
     */
    @GetMapping ( BASE_PATH + "/user/guest" )
    public List<Customer> getGuests () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific customer, as
     * indicated by the path variable provided (the name of the customer
     * desired)
     *
     * @param username
     *            customer username
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/user/customer/{username}" )
    public ResponseEntity getCustomer ( @PathVariable ( "username" ) final String username ) {
        final Customer customer = service.findByUsername( username );
        return null == customer
                ? new ResponseEntity( errorResponse( "No user found with name " + username ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( customer, HttpStatus.OK );
    }

    /**
     * REST API method to provide GET access to a specific guest, as
     * indicated by the path variable provided (the name of the guest
     * desired)
     *
     * @param username
     *            guest username
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/user/guest/{username}" )
    public ResponseEntity getGuest ( @PathVariable ( "username" ) final String username ) {
        final Customer guest = service.findByUsername( username );
        return null == guest
                ? new ResponseEntity( errorResponse( "No user found with name " + username ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( guest, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Customer model. This is
     * used to create a new Customer by automatically converting the JSON
     * RequestBody provided to a Customer object. Invalid JSON will fail.
     *
     * @param customer
     *            The valid Customer to be saved.
     * @return ResponseEntity indicating success if the Customer could be saved
     *         to the database, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/user/customer" )
    public ResponseEntity createCustomer ( @RequestBody final Customer customer ) {
        if ( null != service.findByUsername( customer.getUsername() ) ) {
            return new ResponseEntity(
                    errorResponse( "Customer with the username " + customer.getUsername() + " already exists." ),
                    HttpStatus.CONFLICT );
        }

        customer.addRole( Role.CUSTOMER );

        customer.setPassword( passwordEncoder.encode( customer.getPassword() ) );

        service.save( customer );
        return new ResponseEntity( successResponse( customer.getUsername() + " successfully created." ),
                HttpStatus.OK );

    }

    /**
     * REST API method to provide POST access to the Customer model. This is
     * used to create a new Customer by automatically converting the JSON
     * RequestBody provided to a Customer object. Invalid JSON will fail.
     *
     * @param username
     *            The username of the Guest
     * @param user
     *            The valid Customer to update the Guest to
     * @return ResponseEntity indicating success if the Customer could be saved
     *         to the database, or an error if it could not be
     */
    @PutMapping ( BASE_PATH + "/customer/{username}" )
    public ResponseEntity updateGuest ( @PathVariable ( "username" ) final String username,
            @RequestBody final Customer user ) {
        final Customer guest = guestService.findByUsername( username );

        if ( guest == null ) {
            return new ResponseEntity(
                    errorResponse( "Guest with the username " + username + " does not exist." ),
                    HttpStatus.NOT_FOUND );
        }

        guest.setUsername( user.getUsername() );

        guest.addRole( Role.CUSTOMER );
        // guest.removeRole( Role.GUEST );

        guest.setPassword( passwordEncoder.encode( user.getPassword() ) );

        service.save( guest );

        return new ResponseEntity(
                successResponse( guest.getUsername() + " successfully created from " + username + "." ),
                HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Customer model. This is
     * used to create a new Guest Customer by creating a Customer object with
     * the GUEST role (username and password are generated). Invalid
     * JSON will fail.
     *
     * @return ResponseEntity indicating success if the Guest could be saved
     *         to the database, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/user/guest" )
    public ResponseEntity createGuest () {
        final Customer guest = guestService.createGuestAccount();

        if ( guest != null ) {
            final String token = generateTokenForGuest(); // Generate random
                                                          // token
            return ResponseEntity.ok().header( "X-Auth-Token", token ).body( guest );
        }
        else {
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "Error creating guest" );
        }

        // final long number = repository.countByRolesContaining( Role.GUEST );
        // final String username = "guest" + String.valueOf( number );
        //
        // if ( null != service.findByUsername( username ) ) {
        // return new ResponseEntity(
        // errorResponse( "Guest with the username " + username + " already
        // exists." ),
        // HttpStatus.CONFLICT );
        // }
        //
        // final String password = generateRandomString( 8 );
        // final Customer guest = new Customer( username, password );
        //
        // guest.addRole( Role.GUEST );
        //
        // final Customer guestRet = new Customer( username, password );
        // guestRet.addRole( Role.GUEST );
        //
        // guest.setPassword( passwordEncoder.encode( guest.getPassword() ) );
        //
        // service.save( guest );
        // return new ResponseEntity( guestRet, HttpStatus.OK );
    }

    private String generateTokenForGuest () {
        // Generate a random token (UUID)
        return UUID.randomUUID().toString();
    }

    // @PostMapping ( "/create-and-login" )
    // public ResponseEntity createAndLogin ( final HttpServletRequest request,
    // final HttpServletResponse response ) {
    // // Automatically create user account (replace this with your logic)
    // final Customer newGuest = guestService.createGuestAccount();
    //
    // // Authenticate the newly created user
    // final Authentication authentication = new
    // UsernamePasswordAuthenticationToken( newGuest.getUsername(),
    // newGuest.getPassword() );
    //
    // // Set the authentication in the security context
    // SecurityContextHolder.getContext().setAuthentication( authentication );
    //
    // // Redirect or return success response as needed
    // return new ResponseEntity( successResponse( "User created and logged in
    // successfully" ), HttpStatus.OK );
    // }

    /**
     * REST API method to allow deleting a Customer from the CoffeeMaker's
     * Customers, by making a DELETE request to the API endpoint and indicating
     * the Customer to delete (as a path variable)
     *
     * @param username
     *            The username of the Customer to delete
     * @return Success if the Customer could be deleted; an error if the
     *         Customer does not exist
     */
    @DeleteMapping ( BASE_PATH + "/user/customer/{username}" )
    public ResponseEntity deleteCustomer ( @PathVariable final String username ) {
        final Customer customer = service.findByUsername( username );
        if ( null == customer ) {
            return new ResponseEntity( errorResponse( "No customer found for name " + username ),
                    HttpStatus.NOT_FOUND );
        }
        service.delete( customer );

        return new ResponseEntity( successResponse( username + " was deleted successfully" ), HttpStatus.OK );
    }

    /**
     * REST API method to allow deleting a Guest from the CoffeeMaker's
     * Guests, by making a DELETE request to the API endpoint and indicating
     * the Guest to delete (as a path variable)
     *
     * @param username
     *            The username of the Guest to delete
     * @return Success if the Customer could be deleted; an error if the
     *         Guest does not exist
     */
    @DeleteMapping ( BASE_PATH + "/user/guest/{username}" )
    public ResponseEntity deleteGuest ( @PathVariable final String username ) {
        final Customer guest = guestService.findByUsername( username );
        if ( null == guest ) {
            return new ResponseEntity( errorResponse( "No guest found for name " + username ),
                    HttpStatus.NOT_FOUND );
        }
        guestService.delete( guest );

        return new ResponseEntity( successResponse( username + " was deleted successfully" ), HttpStatus.OK );
    }
}
