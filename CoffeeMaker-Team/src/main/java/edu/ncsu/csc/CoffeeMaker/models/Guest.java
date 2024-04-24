package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

/**
 * Guest for the coffee maker. Guest is tied to the database using
 * Hibernate libraries. See GuestRepository and GuestService for the other
 * two pieces used for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Guest extends User {

    /**
     * Constructor for the guest class Empty class for hibernate
     */
    public Guest () {
        super();
    }

    /**
     * Constructor for a Guest object, references the super class
     *
     * @param username
     *            of the guest
     * @param password
     *            for the guest's account
     */
    public Guest ( final String username, final String password ) {
        super( username, password );
        super.setUserType( "guest" );
    }

}
