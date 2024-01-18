package org.lessons.springlamiapizzeriacrud.Repository;

import org.lessons.springlamiapizzeriacrud.Model.Ingrediente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientiRepository extends JpaRepository<Ingrediente, Integer> {
}
