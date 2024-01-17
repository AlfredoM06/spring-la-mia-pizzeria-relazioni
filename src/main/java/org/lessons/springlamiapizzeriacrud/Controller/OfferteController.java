package org.lessons.springlamiapizzeriacrud.Controller;

import org.lessons.springlamiapizzeriacrud.Model.Offerta;
import org.lessons.springlamiapizzeriacrud.Model.Pizza;
import org.lessons.springlamiapizzeriacrud.Repository.OfferteRepository;
import org.lessons.springlamiapizzeriacrud.Repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/offerte")
public class OfferteController {
    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private OfferteRepository offerteRepository;


    @GetMapping("/create")
    public String create(@RequestParam(name = "pizzaId", required = true) Integer pizzaId, Model model) {
        Optional<Pizza> result = pizzaRepository.findById(pizzaId);
        if (result.isPresent()) {
            Pizza offertaPizza = result.get();
            model.addAttribute("pizza", offertaPizza);

            Offerta newOfferta = new Offerta();

            newOfferta.setPizza(offertaPizza);
            newOfferta.setStartDate(LocalDate.now());
            newOfferta.setEndDate(LocalDate.now().plusDays(30));
            model.addAttribute("offerta", newOfferta);
            return "offerte/create";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + pizzaId + " not found");
        }

    }


    @PostMapping("/create")
    public String store(Offerta formOfferta) {
        Offerta storedOfferta = offerteRepository.save(formOfferta);
        return "redirect:/pizze/show/" + storedOfferta.getPizza().getId();
    }

}
