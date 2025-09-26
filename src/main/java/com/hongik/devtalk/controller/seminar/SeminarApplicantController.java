package com.hongik.devtalk.controller.seminar;

import com.hongik.devtalk.service.seminar.SeminarApplicantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Applicant", description = "세미나 신청 API")
public class SeminarApplicantController {
    private SeminarApplicantService seminarApplicantService;

}
