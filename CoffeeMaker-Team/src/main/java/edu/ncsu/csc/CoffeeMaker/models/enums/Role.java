package edu.ncsu.csc.CoffeeMaker.models.enums;

/**
 * For keeping track of various types of users that are known to the system.
 * Different users have different functionality.
 *
 * Copied from iTrust2
 *
 * @author Kai Presler-Marshall
 * @author Jack MacDonald
 *
 */
public enum Role {

    /**
     * Customer
     */
    CUSTOMER ( 1, "customerindex" ),

    /**
     * Staff
     */
    STAFF ( 2, "staffindex" ),

    /**
     * User
     */
    USER ( 3, "" ),

    /**
     * Guest
     */
    GUEST ( 4, "guestindex" );

    /**
     * Numeric code of the Role
     */
    private final int    code;

    /**
     * Landing screen the User should be shown when logging in
     */
    private final String landingPage;

    /**
     * Create a Role from a code and landing screen.
     *
     * @param code
     *            Code of the Role.
     * @param landingPage
     *            Landing page (upon logging in) for the User
     */
    private Role ( final int code, final String landingPage ) {
        this.code = code;
        this.landingPage = landingPage;
    }

    /**
     * Gets the numeric code of the Role
     *
     * @return Code of this role
     */
    public int getCode () {
        return this.code;
    }

    /**
     * Gets the landing page for this user
     *
     * @return Landing page for the user
     */
    public String getLanding () {
        return this.landingPage;
    }

}
