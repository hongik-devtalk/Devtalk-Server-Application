package com.hongik.devtalk.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Slf4j
@Component
public class S3MailTemplateLoader {
    public String loadTemplate(String templateUrl){
        try{
            URL url = new URL(templateUrl);
            try(BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream(), StandardCharsets.UTF_8))){
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e){
            log.error("S3에서 메일 템플릿을 불러오는데 실패했습니다. URL: {}", templateUrl, e);
            return null;
        }
    }
}
