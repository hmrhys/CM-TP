package edu.ncsu.csc.CoffeeMaker.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.CoffeeMaker.models.CoffeeOrder;
import edu.ncsu.csc.CoffeeMaker.repositories.CoffeeOrderRepository;

/**
 * The CoffeeOrderService is used to handle CRUD operations on the CoffeeOrder
 * model. In addition to all functionality in `Service`, we also manage the
 * Ingredient singleton.
 *
 * @author Natalie Meuser
 *
 */
@Component
@Transactional
@RestController
public class CoffeeOrderService extends Service<CoffeeOrder, Long> {

    /**
     * CoffeeOrderRepository, to be autowired in by Spring and provide CRUD
     * operations on CoffeeOrder model.
     */
    @Autowired
    private CoffeeOrderRepository coffeeOrderRepository;

    /**
     * Returns the repository
     */
    @Override
    protected JpaRepository<CoffeeOrder, Long> getRepository () {
        return coffeeOrderRepository;
    }

}
