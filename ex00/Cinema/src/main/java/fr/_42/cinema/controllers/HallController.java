package fr._42.cinema.controllers;

import fr._42.cinema.models.Hall;
import fr._42.cinema.services.HallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/panel/halls")
public class HallController {

    private final HallService hallService;

    @Autowired
    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public String listHalls(Model model) {
        model.addAttribute("halls", hallService.findAll());
        return "/admin/halls";
    }

    @PostMapping
    public String createHall(
            @RequestParam("serialNumber") String serialNumber,
            @RequestParam("seatsNumber") Integer seatsNumber,
            RedirectAttributes redirectAttributes
    ) {
        Hall hall = new Hall(serialNumber, seatsNumber);
        hallService.save(hall);
        redirectAttributes.addFlashAttribute("success", "Hall created successfully");
        return "redirect:/admin/panel/halls";
    }
}
