package edu.ncsu.csc.CoffeeMaker.controllers;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * Default controller that handles redirecting the logged-in user to one of the
 * appropriate landing screens based on their user roles. If a new role is added
 * to the system, add to the edu.ncsu.csc.itrust.roles.Role class.
 *
 * Other functionality should (generally) not be added to this class and instead
 * go in an appropriate controller for the user type. See the sub-packages for
 * location of each controller type.
 *
 * @author Kai Presler-Marshall
 *
 */

@Controller
public class DefaultController {

    // /**
    // * Returns the login page
    // *
    // * @param model
    // * Data from the frontend
    // * @return Page to be displayed
    // */
    // @GetMapping ( { "login", "login.html" } )
    // public String login ( final Model model ) {
    // return "login";
    // }

    /**
     * This controller is used to redirect the authenticated user to the
     * appropriate landing screen based on their role.
     *
     * @param model
     *            The data from the front end
     * @return The page to be displayed to the user
     */
    @RequestMapping ( value = "/" )
    public RedirectView index ( final Model model ) {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final List< ? extends GrantedAuthority> auths = (List< ? extends GrantedAuthority>) auth.getAuthorities();
        final Role role = auths.stream().map( e -> e.toString() ).map( Role::valueOf ).filter( e -> null != e )
                .findAny().get();
        return new RedirectView( role.getLanding() );
    }
}
