package org.lessons.springlamiapizzeriacrud.Repository;

import org.lessons.springlamiapizzeriacrud.Model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PizzaRepository extends JpaRepository<Pizza, Integer> {
}
