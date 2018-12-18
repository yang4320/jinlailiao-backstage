package com.jinlailiao.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SensitiveWord {
    @Value("${sensitive.words}")
    private String sensitiveWords;
}
