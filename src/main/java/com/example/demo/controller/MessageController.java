package com.example.demo.controller;

import com.example.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping("/")
    public String index(Locale locale) {
        return messageService.getMessage("hello.message", null, locale);
    }
}
