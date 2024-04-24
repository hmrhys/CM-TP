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

import edu.ncsu.csc.CoffeeMaker.models.Staff;
import edu.ncsu.csc.CoffeeMaker.models.enums.Role;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

/**
 * This is the controller that holds the REST endpoints that handle CRUD
 * operations for Staff.
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
public class APIStaffController extends APIController {

    /**
     * StaffService object, to be autowired in by Spring to allow for
     * manipulating the Staff model
     */
    @Autowired
    private StaffService    service;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * REST API method to provide GET access to all staff in the system
     *
     * @return JSON representation of all Staff
     */
    @GetMapping ( BASE_PATH + "/user/staff" )
    public List<Staff> getStaff () {
        return service.findAll();
    }

    /**
     * REST API method to provide GET access to a specific staff, as indicated
     * by the path variable provided (the name of the staff desired)
     *
     * @param username
     *            staff username
     * @return response to the request
     */
    @GetMapping ( BASE_PATH + "/user/staff/{username}" )
    public ResponseEntity getStaffMember ( @PathVariable ( "username" ) final String username ) {
        final Staff staff = service.findByUsername( username );
        return null == staff
                ? new ResponseEntity( errorResponse( "No user found with name " + username ), HttpStatus.NOT_FOUND )
                : new ResponseEntity( staff, HttpStatus.OK );
    }

    /**
     * REST API method to provide POST access to the Staff model. This is used
     * to create a new Staff by automatically converting the JSON RequestBody
     * provided to a Staff object. Invalid JSON will fail.
     *
     * @param staffMember
     *            The valid Staff to be saved.
     * @return ResponseEntity indicating success if the Staff could be saved to
     *         the database, or an error if it could not be
     */
    @PostMapping ( BASE_PATH + "/user/staff" )
    public ResponseEntity createStaffMember ( @RequestBody final Staff staffMember ) {
        if ( null != service.findByUsername( staffMember.getUsername() ) ) {
            return new ResponseEntity(
                    errorResponse( "Staff with the username " + staffMember.getUsername() + " already exists." ),
                    HttpStatus.CONFLICT );
        }

        staffMember.addRole( Role.STAFF );

        staffMember.setPassword( passwordEncoder.encode( staffMember.getPassword() ) );

        service.save( staffMember );
        return new ResponseEntity( successResponse( staffMember.getUsername() + " successfully created." ),
                HttpStatus.OK );

    }

    /**
     * REST API method to allow deleting a Staff from the CoffeeMaker's Staff,
     * by making a DELETE request to the API endpoint and indicating the Staff
     * to delete (as a path variable)
     *
     * @param username
     *            The username of the Staff to delete
     * @return Success if the Staff could be deleted; an error if the Staff does
     *         not exist
     */
    @DeleteMapping ( BASE_PATH + "/user/staff/{username}" )
    public ResponseEntity deleteStaff ( @PathVariable final String username ) {
        final Staff staffMember = service.findByUsername( username );
        if ( null == staffMember ) {
            return new ResponseEntity( errorResponse( "No staff found for name " + username ), HttpStatus.NOT_FOUND );
        }
        service.delete( staffMember );

        return new ResponseEntity( successResponse( username + " was deleted successfully" ), HttpStatus.OK );
    }

}
