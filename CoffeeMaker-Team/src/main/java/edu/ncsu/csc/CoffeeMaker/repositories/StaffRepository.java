package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.Staff;

/**
 * StaffRepository is used to provide CRUD operations for the Staff model.
 * Spring will generate appropriate code with JPA.
 *
 * @author Kai Presler-Marshall
 *
 */
public interface StaffRepository extends JpaRepository<Staff, Long> {

    /**
     * Finds a Staff object with the provided username. Spring will generate
     * code to make this happen.
     *
     * @param username
     *            Username of the Staff
     * @return Found staff, null if none.
     */
    Staff findByUsername ( String username );
}
