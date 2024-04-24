package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Guest;

/**
 * GuestRepository is used to provide CRUD operations for the Guest model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface GuestRepository extends JpaRepository<Guest, Long> {

    /**
     * Finds a Guest object with the provided username. Spring will generate
     * code to make this happen.
     *
     * @param username
     *            Username of the Guest
     * @return Found customer, null if none.
     */
    Guest findByUsername ( String username );
}
