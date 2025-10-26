package com.hongik.devtalk.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/show-seminar")
@Tag(name="[Admin] ShowSeminar",description = "어드민 화면 노출 회차 관리 - by 박소연")
public class AdminShowSeminarController {

}
