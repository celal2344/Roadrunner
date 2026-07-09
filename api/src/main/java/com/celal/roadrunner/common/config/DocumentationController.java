package com.celal.roadrunner.common.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DocumentationController {

    @GetMapping("/scalar/")
    public String redirectScalarTrailingSlash() {
        return "redirect:/scalar";
    }
}
