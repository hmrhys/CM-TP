package edu.ncsu.csc.CoffeeMaker.models;

import javax.persistence.Entity;

/**
 * Staff for the coffee maker. Staff is tied to the database using Hibernate
 * libraries. See StaffRepository and StaffService for the other two pieces used
 * for database support.
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class Staff extends User {

    /**
     * Constructor for the staff object Empty for hibernate
     */
    public Staff () {
        super();
    }

    /**
     * Constructor for the staff object
     *
     * @param username
     *            of the staff
     * @param password
     *            for the staff's account
     */
    public Staff ( final String username, final String password ) {
        super( username, password );
        super.setUserType( "staff" );
    }

}
