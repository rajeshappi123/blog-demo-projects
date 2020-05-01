
package com.mogikanensoftware.greetings.mvc.web;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HelloController {

    @GetMapping("/")
    public String index() {        
        return "index";
    }

    @GetMapping("/home")
    public String index(Principal principal, Model model) {
        log.info("Principal name ->{}", principal.getName());

        OAuth2AuthenticationToken  userDetails = (OAuth2AuthenticationToken ) principal;
        log.info("Principal username ->{}", userDetails.getPrincipal());
        log.info("Principal authorities ->{}", userDetails.getAuthorities());
        
        model.addAttribute("name", principal.getName());

        return "index";
    }

    @Autowired
    @GetMapping("/group")
    @PreAuthorize("hasRole('demo-mvc-users')")
    public String group() {
        return "group";
    }

    @Autowired
    @GetMapping("/roleUser")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String roleUser() {
        log.info("Has ROLE_USER - inside roleUser");
        return "group";
    }
}