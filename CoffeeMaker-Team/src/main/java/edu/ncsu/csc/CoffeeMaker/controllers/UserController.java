package edu.ncsu.csc.CoffeeMaker.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class UserController extends APIController {

    @GetMapping ( "/currentusername" )
    public ResponseEntity getCurrentUsername ( final Authentication authentication ) {
        // Get the username
        final String username = authentication.getName();

        return new ResponseEntity<String>( successResponse( username ), HttpStatus.OK );
    }

    @GetMapping ( "/userdetails" )
    public String getUserDetails ( final Authentication authentication ) {
        // Get the username
        final String username = authentication.getName();

        // Get the authorities (roles)
        final StringBuilder roles = new StringBuilder();
        for ( final GrantedAuthority authority : authentication.getAuthorities() ) {
            roles.append( authority.getAuthority() ).append( ", " );
        }
        roles.delete( roles.length() - 2, roles.length() ); // Remove the last
                                                            // comma and space

        return "Username: " + username + "\n" + "Roles: " + roles.toString();
    }
}
