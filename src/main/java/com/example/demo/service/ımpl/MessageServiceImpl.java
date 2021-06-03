package com.example.demo.service.ımpl;

import com.example.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageSource messageSource;

    public String getMessage(String input, Object... objects){
        return messageSource.getMessage(input, objects, LocaleContextHolder.getLocale());
    }

    public String getMessage(String input) {
        return messageSource.getMessage(input, null, LocaleContextHolder.getLocale());
    }
}
