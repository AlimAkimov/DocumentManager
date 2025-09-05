package com.spring.DocManager.controller;

import com.spring.DocManager.model.Group;
import com.spring.DocManager.service.GroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/groups")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping
    public String listGroups(Model model) {
        List<Group> groups = groupService.findAll()
                .stream()
                .sorted((g1, g2) -> g1.getId().compareTo(g2.getId()))
                .toList();
        model.addAttribute("groups", groups);
        return "groups/list";
    }


    @GetMapping("/new")
    public String newGroup(Model model) {
        model.addAttribute("group", new Group());
        return "groups/form";
    }

    @GetMapping("/edit/{id}")
    public String editGroup(@PathVariable Long id, Model model) {
        Group group = groupService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Группа не найдена с ID: " + id));
        model.addAttribute("group", group);
        return "groups/form";
    }

    @PostMapping
    public String saveGroup(@Valid @ModelAttribute Group group,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "groups/form";
        }
        try {
            if (group.getId() != null) {
                groupService.updateGroup(group);
                redirectAttributes.addFlashAttribute("successMessage", "Группа успешно обновлена");
            } else {
                groupService.save(group);
                redirectAttributes.addFlashAttribute("successMessage", "Группа успешно создана");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении группы: " + e.getMessage());
        }
        return "redirect:/groups";
    }

    @GetMapping("/delete/{id}")
    public String deleteGroup(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            groupService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Группа успешно удалена");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении группы: " + e.getMessage());
        }
        return "redirect:/groups";
    }
}