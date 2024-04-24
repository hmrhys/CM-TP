package edu.ncsu.csc.CoffeeMaker.models;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import edu.ncsu.csc.CoffeeMaker.models.enums.Role;

/**
 * User for the coffee maker. User is tied to the database using Hibernate
 * libraries. See Staff/Customer Repository and Staff/Customer Service for the
 * other two pieces used for database support.
 *
 * Roles from iTrust2
 *
 * @author Kai Presler-Marshall
 */
@Entity
public class User extends DomainObject {

    /** User id */
    @Id
    @GeneratedValue
    protected Long    id;

    /** Username of the user */
    private String    username;

    /** Password of the user */
    private String    password;

    /** Usertype of the user */
    private String    userType;

    /**
     * The Role of the User
     */
    @ElementCollection ( targetClass = Role.class, fetch = FetchType.EAGER )
    @Enumerated ( EnumType.STRING )
    private Set<Role> roles;

    /**
     * Empty constructor for the user class, used by hibernate
     */
    public User () {
        super();
    }

    /**
     * Constructor for the user class
     *
     * @param username
     *            of the user
     * @param password
     *            for the user's account
     */
    public User ( final String username, final String password ) {
        super();
        setUsername( username );
        setPassword( password );
    }

    /**
     * Returns the user's id
     *
     * @return the user's id
     */
    @Override
    public Long getId () {
        return id;
    }

    /**
     * Sets the id for the user
     *
     * @param id
     *            of the user
     */
    public void setId ( final Long id ) {
        this.id = id;
    }

    /**
     * Returns the username of the user
     *
     * @return the user's username
     */
    public String getUsername () {
        return username;
    }

    /**
     * Sets the user's username
     *
     * @param username
     *            to set as the user's
     */
    public void setUsername ( final String username ) {
        this.username = username;
    }

    /**
     * Returns the user's password
     *
     * @return the user's password
     */
    public String getPassword () {
        return password;
    }

    /**
     * Sets the user's password
     *
     * @param password
     *            the user's password
     */
    public void setPassword ( final String password ) {
        this.password = password;
    }

    /**
     * Gets the type of User
     *
     * @return the userType
     */
    public String getUserType () {
        return userType;
    }

    /**
     * Sets the type of User
     *
     * @param userType
     *            the userType to set
     */
    public void setUserType ( final String userType ) {
        this.userType = userType;
    }

    /**
     * Get the role of this user
     *
     * @return the role of this user
     */
    public Collection<Role> getRoles () {
        return roles;
    }

    /**
     * Set the roles of this user. Throws an exception if the Set is empty.
     *
     * @param roles
     *            the roles to set this user to
     */
    public void setRoles ( final Set<Role> roles ) {
        if ( roles.size() == 0 ) {
            throw new IllegalArgumentException(
                    "Tried to create a user with no roles. Users must have at least one role." );
        }

        this.roles = roles;
    }

    /**
     * Adds a new Role to this user. Throws an exception if trying to adding
     * additional roles to Customer or Staff.
     *
     * @param role
     *            the Role to add
     */
    public void addRole ( final Role role ) {
        if ( null == this.roles ) {
            this.roles = new HashSet<Role>();
        }

        if ( this.roles.contains( Role.STAFF ) || this.roles.contains( Role.CUSTOMER ) ) {
            throw new IllegalArgumentException( "Staff and Customers cannot have additional roles added" );
        }

        this.roles.add( role );
    }

    /**
     * Adds a new Role to this user. Throws an exception if trying to adding
     * additional roles to Customer or Staff.
     *
     * @param role
     *            the Role to add
     */
    public void removeRole ( final Role role ) {
        if ( null == this.roles ) {
            this.roles = new HashSet<Role>();
        }

        if ( this.roles.size() <= 1 ) {
            throw new IllegalArgumentException( "Users must have at least one role" );
        }

        this.roles.remove( role );
    }

    /** Checks if there is a user account that is equal to the passed in object
     *
     * @param o
     *            object that is being compared to existing user accounts
     *
     * @return true if a match is found, otherwise false
     */
    @Override
    public boolean equals ( final Object o ) {

        // check if object is null
        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        // test if a customer or staff object exists with the current object's
        // information
        if ( userType.equals( "customer" ) ) {

            // make customer equivalent of object
            final Customer c = (Customer) o;

            // check if customer user exists with object's username and password
            if ( Objects.equals( getUsername(), c.getUsername() )
                    && Objects.equals( getPassword(), c.getPassword() ) ) {
                return true;
            }

        }
        else if ( userType.equals( "staff" ) ) {

            // make staff equivalent of o
            final Staff s = (Staff) o;

            // check if staff user exists with object's username and password
            if ( Objects.equals( getUsername(), s.getUsername() )
                    && Objects.equals( getPassword(), s.getPassword() ) ) {
                return true;
            }
        }

        return false;

    }

    /**
     * Generates hashcode for user object given the object's username and
     * password
     *
     * [ code referenced from ChatGPT ~ hmreese2 ]
     *
     * @return int representation of object's hashed value - important for
     *         storing object in system memory.
     */
    @Override
    public int hashCode () {
        return Objects.hash( getUsername(), getPassword() );
    }

    @Override
    public String toString () {
        return userType + " : " + username;
        // Implementation of toString
    }
}
