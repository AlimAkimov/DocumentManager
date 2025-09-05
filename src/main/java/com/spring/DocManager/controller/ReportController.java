package com.spring.DocManager.controller;

import com.spring.DocManager.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private DocumentService documentService;

    @GetMapping
    public String getExpiringDocumentsReport(Model model) {
        model.addAttribute("expiringDocuments", documentService.getExpiringDocuments());
        return "report/expiring";
    }
}
