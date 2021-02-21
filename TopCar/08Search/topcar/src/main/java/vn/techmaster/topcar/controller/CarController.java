package vn.techmaster.topcar.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.techmaster.topcar.exception.StorageException;
import vn.techmaster.topcar.model.Car;
import vn.techmaster.topcar.repository.CRUD;
import vn.techmaster.topcar.request.SearchRequest;
import vn.techmaster.topcar.service.StorageService;

import java.util.Optional;

@Controller
@RequestMapping("/car")
public class CarController {
    @Autowired
    CRUD<Car> topcar;


    @GetMapping
    public String listAll(Model model){
        model.addAttribute("cars",topcar.getAll());
        return "allcars";
    }

    @GetMapping("/add")
    public String add(Model model){
        model.addAttribute("car",new Car());
        return "form";
    }

    @GetMapping("/edit/{id}")
    public String updateById(@PathVariable int id, Model model){
        Optional<Car> car=topcar.get(id);
        if(car.isPresent()){
            model.addAttribute("car",car.get());
        }
        return "form";
    }

    @GetMapping("/{id}")
    public String getElementById(@PathVariable int id,Model model){
        Optional<Car> car=topcar.get(id);
        if(car.isPresent()) {
            model.addAttribute("car", car.get());
        }
        return "Car";
    }

    @PostMapping(value = "/save",consumes = {"multipart/form-data"})
    public String save(@ModelAttribute Car car, BindingResult result, RedirectAttributes redirect){
        if(result.hasErrors()){
            return "form";
        }
        if(car.getId()>0){
            topcar.update(car);
        }else {
            topcar.add(car);
        }
        return "redirect:/car";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id,Model model){
        if(topcar.get(id).isPresent()){
            topcar.deleteById(id);
        }
        return listAll(model);
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("searchrequest",new SearchRequest());
        return "search";
    }

    @PostMapping("/search")
    public String searchByKey(Model model, @ModelAttribute SearchRequest request, BindingResult result){
        model.addAttribute("cars",topcar.searchByKeyword(request.getKeyword()));
        return "allcars";
    }

    @ExceptionHandler(StorageException.class)
    public String handleStorageFileNotFound(StorageException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "allcars";
    }
}
