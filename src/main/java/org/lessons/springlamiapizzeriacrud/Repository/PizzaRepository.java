package org.lessons.springlamiapizzeriacrud.Repository;

import org.lessons.springlamiapizzeriacrud.Model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
    List<Pizza> findByNameContaining(String searchName);
}
