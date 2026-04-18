package com.example.parts.controller;

import com.example.parts.config.PartsProperties;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    private final PartsProperties partsProperties;

    public GlobalModelAttributes(PartsProperties partsProperties) {
        this.partsProperties = partsProperties;
    }

    @ModelAttribute("marketplaceName")
    public String marketplaceName() {
        return partsProperties.getMarketplaceName();
    }

    @ModelAttribute("contactPhone")
    public String contactPhone() {
        return partsProperties.getContactPhone();
    }

    @ModelAttribute("contactEmail")
    public String contactEmail() {
        return partsProperties.getContactEmail();
    }

    @ModelAttribute("officeAddress")
    public String officeAddress() {
        return partsProperties.getOfficeAddress();
    }

    @ModelAttribute("workingHours")
    public String workingHours() {
        return partsProperties.getWorkingHours();
    }

    @ModelAttribute("isAuthenticated")
    public boolean isAuthenticated(Authentication authentication) {
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @ModelAttribute("currentUserEmail")
    public String currentUserEmail(Authentication authentication) {
        return isAuthenticated(authentication) ? authentication.getName() : null;
    }
}
