package com.hongik.devtalk.controller.internal;

import com.hongik.devtalk.service.mail.D1ReminderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "INTERNAL (Debug Only)", description = "로컬/개발 환경에서만 활성화되는 내부 테스트용 API 컨트롤러")
@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
@Profile({"local","dev"}) // 로컬/개발에서만 활성화
public class ReminderDebugController {

    private final D1ReminderService svc;

    @Operation(
            summary = "D-1 리마인더 즉시 발송 (테스트용)",
            description = "세미나 하루 전 오전 10시에 발송되는 리마인더 로직을 **수동으로 즉시 트리거**하는 테스트용 API입니다.<br>" +
                    "**별도의 API 연동이 필요 없는** 내부 디버깅 기능입니다."
    )
    // 내일 세미나 대상자에게 D-1 리마인더 즉시 발송
    @PostMapping("/reminders/d1/run")
    public Map<String,Object> runOnce() {
        int sent = svc.sendD1Reminders();
        return Map.of("sent", sent);
    }
}