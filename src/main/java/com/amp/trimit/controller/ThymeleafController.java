package com.amp.trimit.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.amp.trimit.service.UrlTrimService;

@Controller
public class ThymeleafController {
    private final UrlTrimService urlTrimService;

    public ThymeleafController(UrlTrimService urlTrimService){
        this.urlTrimService = urlTrimService;
    }

     @GetMapping("/")
    public String home(Model model) {
        return "home";
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String urlString, Model model){
        try {
        String shortUrl = urlTrimService.trimUrl(urlString);
        model.addAttribute("shortUrl", shortUrl);
        model.addAttribute("longUrl", urlString);
    } catch (IllegalArgumentException e) {
        model.addAttribute("error", e.getMessage());
    }
        return "home";
    }

    
    @GetMapping("/{code}")
    public ResponseEntity<Void> redirectByCode(@PathVariable String code) {
    String longUrl = urlTrimService.findUrlByCode(code);
    if (longUrl == null) {
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.status(302)
            .location(URI.create(longUrl))
            .build();
}

    @GetMapping("/redirect")
    public ResponseEntity<Void> redirect(@RequestParam String code) {
        String longUrl = urlTrimService.findUrlByCode(code);
        if (longUrl == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(302)
            .location(URI.create(longUrl))
            .build();
}

}
