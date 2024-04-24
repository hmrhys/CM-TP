package edu.ncsu.csc.CoffeeMaker.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import edu.ncsu.csc.CoffeeMaker.controllers.LoginController;

/**
 * Configuration file for Spring Security. Responsible for setting up Spring
 * Security imports (i.e. BCrypt), and setting up the initial page for
 * authorization purposes.
 *
 * @author hmreese2 (Hanna Reese)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder    passwordEncoder;

    @Autowired
    private LoginController    loginController;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public DaoAuthenticationProvider authenticationProvider () {
        final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService( userDetailsService );
        authProvider.setPasswordEncoder( passwordEncoder );
        return authProvider;
    }

    @Override
    protected void configure ( final AuthenticationManagerBuilder auth ) throws Exception {
        auth.userDetailsService( userDetailsService ).passwordEncoder( passwordEncoder );
        // auth.inMemoryAuthentication().withUser( "customer" ).password(
        // passwordEncoder.encode( "password" ) )
        // .roles( "CUSTOMER" )
        // .and()
        // .withUser( "worker13" ).password( passwordEncoder.encode( "password"
        // ) ).roles( "STAFF" );
        // You can add more users with different roles here if needed
    }

    /*
     * [ Source code referenced from Generative AI (ChatGPT) ] Configures Spring
     * to work with our current code. Includes setting the default page to the
     * login page, and setting up authentication with Spring BCrypt in this way.
     * @param http instance of HTTP Security used for
     * authentication/authorization purposes
     * @throws
     */
    @Override
    protected void configure ( final HttpSecurity http ) throws Exception {

        /*
         * According to
         * https://docs.spring.io/spring-security/site/docs/current/apidocs/org/
         * springframework/security/config/annotation/web/builders/HttpSecurity.
         * html#addFilter-javax.servlet.Filter- ChannelProcessingFIlter is the
         * first filter processed, so this means the IP block will be the
         * absolute first Filter.
         */
        // http.addFilterBefore( ipBlockFilter(), ChannelProcessingFilter.class
        // );

        // WILL TAKE TO index.html
        // http.authorizeRequests()
        // .antMatchers( "/public/**", "/css/**", "/js/**", "/#/**",
        // "/login.html", "/customerlogin.html",
        // "/stafflogin.html", "/createaccount.html" )
        // .permitAll()
        // .antMatchers( HttpMethod.POST, "/users/staff" ).permitAll()
        // .antMatchers( "/api/**" ).permitAll()
        // .anyRequest().authenticated()
        // .and()
        // .formLogin()
        // .loginPage( "/login.html" )
        // .loginProcessingUrl( "/perform_login" )
        // .defaultSuccessUrl( "/index.html" )
        // .permitAll()
        // .and()
        // .logout()
        // .permitAll()
        // .and()
        // .csrf().disable();

        // TAKES TO THE RIGHT PAGE
        // http.authorizeRequests()
        // .antMatchers( "/public/**", "/css/**", "/js/**", "/#/**",
        // "/login.html", "/customerlogin.html",
        // "/stafflogin.html", "/createaccount.html", "/debug.html" )
        // .permitAll()
        // .antMatchers( HttpMethod.POST, "/users/staff" ).permitAll()
        // .antMatchers( "/api/**" ).permitAll()
        // .anyRequest().authenticated()
        // .and()
        // .formLogin()
        // .loginPage( "/login.html" )
        // .loginProcessingUrl( "/perform_login" )
        // .permitAll()
        // .and()
        // .logout()
        // .permitAll()
        // .and()
        // .csrf().disable()
        // .formLogin().defaultSuccessUrl( "/", true ) // Redirect to root
        // // by default
        // .successHandler( ( request, response, authentication ) -> {
        // // Get the user's authorities (roles)
        // final Collection< ? extends GrantedAuthority> authorities =
        // authentication.getAuthorities();
        // // Check if the user has ROLE_ADMIN
        // if ( authorities.stream().anyMatch( auth ->
        // auth.getAuthority().equals( "ROLE_STAFF" ) ) ) {
        // response.sendRedirect( "/staffindex" ); // Redirect to
        // // staff index
        // }
        // else {
        // response.sendRedirect( "/customerindex" ); // Redirect
        // // to
        // // customer
        // // index (or
        // // any other
        // // page)
        // }
        // } );

        // THIS WORKS FOR LOGGING IN AND HANDLING ERRORS
        // http.authorizeRequests()
        // .antMatchers( "/public/**", "/css/**", "/js/**", "/#/**",
        // "/login.html", "/customerlogin.html",
        // "/stafflogin.html", "/createaccount.html", "/debug.html" )
        // .permitAll()
        // .antMatchers( HttpMethod.POST, "/users/staff" ).permitAll()
        // .antMatchers( "/api/**" ).permitAll()
        // .anyRequest().authenticated()
        // .and()
        // .formLogin()
        // .loginPage( "/login.html" )
        // .loginProcessingUrl( "/perform_login" )
        // .successHandler( successHandler() ) // Use custom success
        // // handler
        // .failureHandler( failureHandler() ) // Set custom failure
        // // handler
        // .permitAll()
        // .and()
        // .logout()
        // .permitAll()
        // .and()
        // .csrf().disable();

        // HAVING ISSUES WITH AUTHORIZATION
        // http.authorizeRequests()
        // .antMatchers( "/public/**", "/css/**", "/js/**", "/#/**",
        // "/login.html", "/customerlogin.html",
        // "/stafflogin.html", "/createaccount.html", "/notfound.html",
        // "/error.html",
        // "/privacypolicyhome.html", "/guestindex.html" )
        // .permitAll()
        // .antMatchers( "/staff/**" ).hasRole( "STAFF" )
        // .antMatchers( "/staffindex.html", "/staffindex",
        // "/staff-addingredients.html",
        // "/staff-addinventory.html",
        // "/staff-addrecipe.html", "/staff-allorders.html", "/staff-addrecipe"
        // )
        // .hasRole( "STAFF" )
        // .antMatchers( "/guestindex.html", "/guest-placeorder.html",
        // "/guest-orderstatus.html",
        // "/guest-vieworders.html" )
        // .not()
        // .hasAnyRole( "USER", "CUSTOMER" )
        // .antMatchers( HttpMethod.POST, "/users/staff"
        // ).permitAll().antMatchers( "/api/**" )
        // .permitAll()
        // .anyRequest().authenticated().and().formLogin().loginPage(
        // "/login.html" )
        // .loginProcessingUrl( "/perform_login" ).successHandler(
        // successHandler() ) // Use
        // // custom
        // // success
        // // handler
        // .failureHandler( failureHandler() ) // Set custom failure
        // // handler
        // .permitAll().and().logout().logoutUrl( "/logout" ) // Specify
        // // the logout
        // // URL
        // .logoutSuccessUrl( "/login?logout" ) // Redirect to this URL
        // // after successful logout
        // .deleteCookies( "auth_token", "JESSOINID" ) // Delete cookies
        // // upon logout
        // .invalidateHttpSession( true ) // Invalidate HTTP session
        // .permitAll() // Allow all users to access the logout URL
        // .and().csrf().disable();

        http.authorizeRequests()
                .antMatchers( "/public/**", "/css/**", "/js/**", "/#/**", "/api/**", "/login", "/stafflogin.html",
                        "/customerlogin.html", "/guestindex.html", "/createaccount.html", "/notfound.html",
                        "/error.html", "/privacypolicyhome.html", "/guestindex.html" )
                .permitAll()
                .antMatchers( "/staffindex.html", "/staffindex", "/staff-addingredients.html", "/staff-addingredients",
                        "/staff-addinventory.html", "/staff-addinventory", "/staff-addrecipe.html", "/staff-addrecipe",
                        "/staff-allorders.html", "/staff-allorders", "/staff-deleterecipe.html", "/staff-deleterecipe",
                        "/staff-editrecipe.html", "/staff-editrecipe", "/staff-fulfillorders.html",
                        "/staff-fulfillorders" )
                .hasRole( "STAFF" )
                .antMatchers( "/customerindex.html", "/customerindex", "/customer-orderstatus.html",
                        "/customer-orderstatus", "/customer-placeorder.html", "/customer-placeorder",
                        "/customer-vieworders.html", "/customer-vieworders" )
                .hasRole( "CUSTOMER" )
                .antMatchers( "/guestindex.html", "/guestindex", "/guest-orderstatus.html", "/guest-orderstatus",
                        "/guest-placeorder.html", "/guest-placeorder", "/guest-vieworders.html", "/guest-vieworders",
                        "/privacypolicy-guest.html" )
                .not().hasAnyRole( "STAFF", "CUSTOMER", "USER" ).anyRequest().authenticated().and().formLogin()
                .loginPage( "/login" ).loginProcessingUrl( "/perform_login" ).successHandler( successHandler() ) // Use
                                                                                                                 // custom
                                                                                                                 // success
                                                                                                                 // handler
                .failureHandler( failureHandler() ) // Set custom failure
                                                    // handler
                .and().logout().logoutUrl( "/logout" ) // Specify the logout URL
                .logoutSuccessUrl( "/login?logout" ) // Redirect to this URL
                                                     // after successful logout
                .deleteCookies( "auth_token", "JESSOINID" ) // Delete cookies
                                                            // upon logout
                .invalidateHttpSession( true ) // Invalidate HTTP session
                .permitAll() // Allow all users to access the logout URL
                .and().csrf().disable();

        // .csrf().csrfTokenRepository(
        // CookieCsrfTokenRepository.withHttpOnlyFalse() );

        // http.authorizeRequests().antMatchers( patterns
        // ).anonymous().anyRequest().authenticated().and().formLogin().loginPage(
        // null )

        /*
         * * Credit to https://medium.com/spektrakel
         * -blog/angular2-and-spring-a- friend-in- security-need-is-a-friend-
         * against-csrf-indeed- 9f83eaa9ca2e and http://docs.spring.io/spring-
         * security/site/docs/current/ reference/ html/csrf.html#csrf-cookie for
         * information on how to make Angular work properly with CSRF protection
         */

        // .csrfTokenRepository( CookieCsrfTokenRepository.withHttpOnlyFalse()
        // );
    }

    /**
     * Bean for Spring Security Bcrypt password encoder, sets up for future use
     * in application.
     *
     * @return instance of PasswordEncoder useed for BCrypt based authentication
     */
    @Bean
    public PasswordEncoder encoder () {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler () {
        return ( request, response, authentication ) -> {
            // Delegate to the performLogin method in the LoginController
            loginController.performLogin( request.getParameter( "username" ), request.getParameter( "password" ),
                    request, response, authentication );
        };
    }

    @Bean
    public AuthenticationFailureHandler failureHandler () {
        return ( request, response, exception ) -> {
            // Delegate to the performLogin method in the LoginController
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            loginController.performLogin( request.getParameter( "username" ), request.getParameter( "password" ),
                    request, response, authentication );
        };
    }

    // THIS WORKS
    // @Bean
    // public AuthenticationFailureHandler failureHandler () {
    // return ( request, response, exception ) -> {
    // // Get the referrer URL
    // final String referrer = request.getHeader( "Referer" );
    // // Default error redirection URL
    // String redirectUrl = "/login.html";
    //
    // // Check if referrer is stafflogin or customerlogin
    // if ( referrer != null && referrer.contains( "stafflogin" ) ) {
    // redirectUrl = "/stafflogin.html";
    // }
    // else if ( referrer != null && referrer.contains( "customerlogin" ) ) {
    // redirectUrl = "/customerlogin.html";
    // }
    //
    // // Append error parameter
    // if ( redirectUrl.contains( "?" ) ) {
    // redirectUrl += "&error";
    // }
    // else {
    // redirectUrl += "?error";
    // }
    //
    // // Redirect to the updated URL
    // response.sendRedirect( redirectUrl );
    // };
    // }

}
