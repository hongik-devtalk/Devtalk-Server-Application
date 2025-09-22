package com.hongik.devtalk.global;




import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode.MISSING_AUTH_INFO;

@RequiredArgsConstructor
@RestController
public class HomeController {

    @GetMapping("/")
    @Operation(summary = "스웨거테스트 -by 남성현")
    public String home() {
        return "데브톡 프로젝트 서버입니다.";
    }
}
