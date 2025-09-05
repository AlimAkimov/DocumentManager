package com.spring.DocManager.controller;

import com.spring.DocManager.model.Document;
import com.spring.DocManager.model.DocumentType;
import com.spring.DocManager.service.DocumentService;
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
@RequestMapping("/documents")
public class DocumentController {
    @Autowired
    private DocumentService documentService;
    @Autowired
    private GroupService groupService;

    @GetMapping
    public String listDocuments(Model model) {
        List<Document> documents = documentService.findAll()
                .stream()
                .sorted((d1, d2) -> d1.getId().compareTo(d2.getId()))
                .toList();
        model.addAttribute("documents", documents);
        return "documents/list";
    }


    @GetMapping("/new")
    public String newDocument(Model model) {
        model.addAttribute("document", new Document());
        model.addAttribute("types", DocumentType.values());
        model.addAttribute("groups", groupService.findAll());
        return "documents/form";
    }

    @GetMapping("/edit/{id}")
    public String editDocument(@PathVariable Long id, Model model) {
        Document document = documentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Документ не найден с ID: " + id));
        model.addAttribute("document", document);
        model.addAttribute("types", DocumentType.values());
        model.addAttribute("groups", groupService.findAll());
        return "documents/form";
    }

    @PostMapping
    public String saveDocument(@Valid @ModelAttribute Document document,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("types", DocumentType.values());
            model.addAttribute("groups", groupService.findAll());
            return "documents/form";
        }
        try {
            if (document.getId() != null) {
                documentService.updateDocument(document);
                redirectAttributes.addFlashAttribute("successMessage", "Документ успешно обновлен");
            } else {
                documentService.save(document);
                redirectAttributes.addFlashAttribute("successMessage", "Документ успешно создан");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении документа: " + e.getMessage());
        }
        return "redirect:/documents";
    }

    @GetMapping("/delete/{id}")
    public String deleteDocument(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            documentService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Документ успешно удален");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении документа: " + e.getMessage());
        }
        return "redirect:/documents";
    }
}