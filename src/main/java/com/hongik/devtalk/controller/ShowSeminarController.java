package com.hongik.devtalk.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/show-seminar")
@Tag(name="[User] ShowSeminar",description = "유저 홈화면에서 노출할 세미나 회차 응답 api - by 박소연")
public class ShowSeminarController {
}
