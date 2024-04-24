package edu.ncsu.csc.CoffeeMaker.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;

/**
 * CoffeeOrderRepository is used to provide CRUD operations for the CoffeeOrder
 * model. Spring will generate appropriate code with JPA.
 *
 * @author Natalie Meuser
 *
 */
public interface CoffeeOrderRepository extends JpaRepository<CoffeeOrder, Long> {

}
