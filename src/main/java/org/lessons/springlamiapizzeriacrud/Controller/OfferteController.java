package org.lessons.springlamiapizzeriacrud.Controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.Model.Offerta;
import org.lessons.springlamiapizzeriacrud.Model.Pizza;
import org.lessons.springlamiapizzeriacrud.Repository.OfferteRepository;
import org.lessons.springlamiapizzeriacrud.Repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public String store(@Valid @ModelAttribute("offerta") Offerta formOfferta, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pizza", formOfferta.getPizza());
            return "offerte/create";
        }

        if (formOfferta.getEndDate() != null && formOfferta.getEndDate().isBefore(formOfferta.getStartDate())) {
            formOfferta.setEndDate(formOfferta.getStartDate().plusDays(30));
        }

        Offerta storedOfferta = offerteRepository.save(formOfferta);
        return "redirect:/pizze/show/" + storedOfferta.getPizza().getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Offerta> result = offerteRepository.findById(id);
        if (result.isPresent()) {
            Offerta formOfferta = result.get();
            model.addAttribute("offerta", formOfferta);
            return "offerte/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offerta with id " + id + " not found");
        }
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("offerta") Offerta formOfferta, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "offerte/edit";
        }
        Offerta updatedOfferta = offerteRepository.save(formOfferta);
        return "redirect:/pizze/show/" + updatedOfferta.getPizza().getId();
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id) {
        Optional<Offerta> result = offerteRepository.findById(id);
        if (result.isPresent()) {
            Offerta offertaToDelete = result.get();
            offerteRepository.delete(result.get());
            return "redirect:/pizze/show/" + offertaToDelete.getPizza().getId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Offerta with id " + id + " not found");
        }
    }
}
