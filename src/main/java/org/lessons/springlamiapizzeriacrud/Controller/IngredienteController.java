package org.lessons.springlamiapizzeriacrud.Controller;

import jakarta.validation.Valid;
import org.lessons.springlamiapizzeriacrud.Model.Ingrediente;
import org.lessons.springlamiapizzeriacrud.Repository.IngredientiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/ingredienti")
public class IngredienteController {

    @Autowired
    private IngredientiRepository ingredientiRepository;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("listaIngredienti", ingredientiRepository.findAll());
        return "ingredienti/index";
    }


    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute("formIngrediente", new Ingrediente());
        return "ingredienti/create";
    }

    @PostMapping("/create")
    public String store(@Valid @ModelAttribute("formIngrediente") Ingrediente formIngrediente, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ingredienti/create";
        }
        ingredientiRepository.save(formIngrediente);
        return "redirect:/ingredienti";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        Optional<Ingrediente> result = ingredientiRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("formIngrediente", result.get());
            return "ingredienti/create";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "l'ingrediente con id " + id + "non è stato trovato");
        }

    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Integer id, @Valid @ModelAttribute("ingrediente") Ingrediente formIngrediente, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "ingredienti/create";
        }
        formIngrediente.setId(id);
        Ingrediente updatedIngrediente = ingredientiRepository.save(formIngrediente);
        return "redirect:/ingredienti";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Optional<Ingrediente> result = ingredientiRepository.findById(id);
        if (result.isPresent()) {
            ingredientiRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "L'ingrediente " + result.get().getName() + " è stato eliminato");
            return "redirect:/ingredienti";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "l'ingrediente con id " + id + " non è stato trovato");
        }
    }

}
