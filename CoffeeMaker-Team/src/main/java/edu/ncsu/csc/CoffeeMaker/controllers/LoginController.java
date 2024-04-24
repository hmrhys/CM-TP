package edu.ncsu.csc.CoffeeMaker.controllers;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.ncsu.csc.CoffeeMaker.config.TokenGenerator;
import edu.ncsu.csc.CoffeeMaker.services.CustomerService;
import edu.ncsu.csc.CoffeeMaker.services.GuestService;
import edu.ncsu.csc.CoffeeMaker.services.StaffService;

@Controller
public class LoginController {

    @Autowired
    private StaffService    staffService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private GuestService    guestService;

    @PostMapping ( "/perform_login" )
    public void performLogin ( @RequestParam ( "username" ) final String username,
            @RequestParam ( "password" ) final String password, final HttpServletRequest request,
            final HttpServletResponse response, final Authentication authentication ) throws IOException {

        final boolean isAuthenticated = authenticate( username, password, request );

        // Get the referrer URL
        final String referrer = request.getHeader( "Referer" );
        // Default error redirection URL
        String redirectUrl = "/login.html";

        if ( isAuthenticated ) {
            // Generate token
            final String token = TokenGenerator.generateToken();

            // -- COOKIE --

            // // Set the token as a cookie in the response
            // final Cookie cookie = new Cookie( "auth_token", token );
            // cookie.setMaxAge( 86400 ); // Set the cookie expiration time (in
            // // seconds)
            // cookie.setPath( "/" ); // Set the cookie path
            // response.addCookie( cookie );

            // -- COOKIE --

            // Get the user's authorities (roles)
            final Collection< ? extends GrantedAuthority> authorities = authentication.getAuthorities();
            // Check if the user has ROLE_STAFF
            for ( final GrantedAuthority authority : authorities ) {
                System.out.println( "User " + authentication.getName() + " has role: " + authority.getAuthority() );
            }

            if ( authorities.stream().anyMatch( auth -> auth.getAuthority().equals( "ROLE_STAFF" ) ) ) {
                response.sendRedirect( "/staffindex" ); // Redirect to
                // admin dashboard
            }
            else if ( authorities.stream().anyMatch( auth -> auth.getAuthority().equals( "ROLE_CUSTOMER" ) ) ) {
                response.sendRedirect( "/customerindex" ); // Redirect to
                                                           // customer dashboard
            }
            else if ( authorities.stream().anyMatch( auth -> auth.getAuthority().equals( "ROLE_GUEST" ) ) ) {
                // // Set the username as a cookie in the response
                // final Cookie usernameCookie = new Cookie( "username",
                // username );
                // usernameCookie.setMaxAge( 86400 ); // Set the cookie
                // expiration
                // // time (in seconds)
                // usernameCookie.setPath( "/" ); // Set the cookie path
                // final Cookie passwordCookie = new Cookie( "password",
                // password );
                // passwordCookie.setMaxAge( 86400 ); // Set the cookie
                // expiration
                // // time (in seconds)
                // passwordCookie.setPath( "/" ); // Set the cookie path
                //
                // response.addCookie( usernameCookie );
                // response.addCookie( passwordCookie );

                response.sendRedirect( "/guestindex" );
            }

            else {
                response.sendRedirect( "/debug" ); // Redirect to
                                                   // user
                // dashboard (or any
                // other page)
            }
        }
        else {
            if ( referrer != null && referrer.contains( "stafflogin" ) ) {
                redirectUrl = "/stafflogin.html";
            }
            else if ( referrer != null && referrer.contains( "customerlogin" ) ) {
                redirectUrl = "/customerlogin.html";
            }

            // Append error parameter
            if ( redirectUrl.contains( "?" ) ) {
                redirectUrl += "&error";
            }
            else {
                redirectUrl += "?error";
            }

            // Redirect to the updated URL
            response.sendRedirect( redirectUrl );
        }
    }

    // @PostMapping ( "/login" )
    // public void login ( @RequestParam ( "username" ) final String username,
    // @RequestParam ( "password" ) final String password, final
    // HttpServletRequest request,
    // final HttpServletResponse response, final Authentication authentication )
    // throws IOException {
    //
    // // Default error redirection URL
    // final String redirectUrl = "/login.html";
    //
    // final boolean isAuthenticated = authenticate( username, password, request
    // );
    //
    // if ( isAuthenticated ) {
    // final String token = TokenGenerator.generateToken();
    //
    // // Set the token as a cookie in the response
    // final Cookie tokenCookie = new Cookie( "auth_token", token );
    // tokenCookie.setMaxAge( 86400 ); // Set the cookie expiration time
    // // (in
    // // seconds)
    // tokenCookie.setPath( "/" ); // Set the cookie path
    // response.addCookie( tokenCookie );
    //
    // // Set the username as a cookie in the response (if needed)
    // final Cookie usernameCookie = new Cookie( "username", username );
    // usernameCookie.setMaxAge( 86400 ); // Set the cookie expiration time
    // // (in seconds)
    // usernameCookie.setPath( "/" ); // Set the cookie path
    // response.addCookie( usernameCookie );
    //
    // // Set the password as a cookie in the response (if needed)
    // final Cookie passwordCookie = new Cookie( "password", password );
    // passwordCookie.setMaxAge( 86400 ); // Set the cookie expiration time
    // // (in seconds)
    // passwordCookie.setPath( "/" ); // Set the cookie path
    // response.addCookie( passwordCookie );
    //
    // // // Create a response body containing the username
    // // final Map<String, String> responseBody = new HashMap<>();
    // // responseBody.put( "token", token );
    // // responseBody.put( "username", username );
    // // responseBody.put( "password", password );
    //
    // // Return the response with a success status and the response
    //
    // // return ResponseEntity.ok( responseBody );
    // }
    // else {
    // // Handle authentication failure
    // // return ResponseEntity.status( HttpStatus.UNAUTHORIZED ).build();
    // }
    // }

    // private boolean authenticate ( final String username, final String
    // password ) {
    // final User user = userService.findByUsername( username );
    // return ( user != null && passwordEncoder.matches( password,
    // user.getPassword() ) );
    //
    // // return ( "worker13".equals( username ) || "customer".equals( username
    // // ) ) && "password".equals( password );
    // }

    private boolean authenticate ( final String username, final String password, final HttpServletRequest request ) {
        // Get the referrer URL
        final String referrer = request.getHeader( "Referer" );

        if ( referrer != null && referrer.contains( "stafflogin" ) ) {
            return staffService.authenticateUser( username, password );
        }
        else if ( referrer != null && referrer.contains( "customerlogin" ) ) {
            return customerService.authenticateUser( username, password );
        }
        else if ( referrer != null && referrer.contains( "guestindex" ) ) {
            return guestService.authenticateUser( username, password );
        }
        else {
            return false;
        }
        // return ( userService.findByUsername( username ) != null &&
        // "password".equals( password ) );

        // return ( "worker13".equals( username ) || "customer".equals( username
        // ) ) && "password".equals( password );
    }
}
