package org.lessons.springlamiapizzeriacrud.Controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.Model.Pizza;
import org.lessons.springlamiapizzeriacrud.Repository.IngredientiRepository;
import org.lessons.springlamiapizzeriacrud.Repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizze")
public class PizzeriaController {
    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private IngredientiRepository ingredientiRepository;

    @GetMapping
    public String index(@RequestParam(name = "search", required = false) String searchKeyword, Model model) {
        List<Pizza> listaPizze;
        if (searchKeyword != null) {
            listaPizze = pizzaRepository.findByNameContaining(searchKeyword);
        } else {
            listaPizze = pizzaRepository.findAll();
        }
        model.addAttribute("listaPizze", listaPizze);
        model.addAttribute("preloadSearch", searchKeyword);
        return "pizze/lista";
    }


    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Pizza> result = pizzaRepository.findById(id);
        if (result.isPresent()) {
            Pizza pizza = result.get();
            model.addAttribute("pizza", pizza);
            return "pizze/show";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");

        }
    }

    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("pizza", new Pizza());
        model.addAttribute("listaIngredienti", ingredientiRepository.findAll());
        return "pizze/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaIngredienti", ingredientiRepository.findAll());
            return "pizze/create";
        }

        Pizza savePizza = pizzaRepository.save(formPizza);
        return "redirect:/pizze/show/" + savePizza.getId();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Pizza> result = pizzaRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("pizza", result.get());
            model.addAttribute("listaIngredienti", ingredientiRepository.findAll());
            return "pizze/edit";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");

        }

    }

    @PostMapping("/edit/{id}")
    String update(@PathVariable Integer id, @Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listaIngredienti", ingredientiRepository.findAll());
            return "pizze/edit";
        }
        Pizza savePizza = pizzaRepository.save(formPizza);
        return "redirect:/pizze/show/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Pizza> result = pizzaRepository.findById(id);
        if (result.isPresent()) {
            pizzaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Pizza " + result.get().getName() + " deleted!");
            return "redirect:/pizze";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pizza with id " + id + " not found");
        }
    }

  /*
    @GetMapping("/search")
    public String search(@RequestParam(name = "search") String searchKeyword, Model model) {
        List<Pizza> listaPizze = pizzaRepository.findByNameContaining(searchKeyword);
        model.addAttribute("listaPizze", listaPizze);
        return "pizze/lista";
    }
  */
}
