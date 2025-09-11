package com.spring.DocManager.controller;

import com.spring.DocManager.model.DocumentType;
import com.spring.DocManager.service.DocumentTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/document-types")
public class DocumentTypeController {
    @Autowired
    private DocumentTypeService documentTypeService;

    @GetMapping
    public String listTypes(Model model) {
        model.addAttribute("types", documentTypeService.findAll());
        return "documentTypes/list";
    }

    @GetMapping("/new")
    public String createType(Model model) {
        model.addAttribute("documentType", new DocumentType());
        return "documentTypes/form";
    }

    @GetMapping("/edit/{id}")
    public String editType(@PathVariable Long id, Model model) {
        DocumentType documentType = documentTypeService.findById(id);
        model.addAttribute("documentType", documentType);
        return "documentTypes/form";
    }

    @PostMapping
    public String saveType(@Valid @ModelAttribute("documentType") DocumentType documentType,
                           BindingResult result,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "documentTypes/form";
        }
        documentTypeService.save(documentType);
        redirectAttributes.addFlashAttribute("successMessage", "Тип документа успешно сохранён");
        return "redirect:/document-types";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            documentTypeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Тип документа успешно удалён");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при удалении типа: " + e.getMessage());
        }
        return "redirect:/document-types";
    }
}
