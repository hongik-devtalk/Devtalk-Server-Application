package com.hongik.devtalk.controller.attend;

import com.hongik.devtalk.domain.live.dto.SeminarLiveAttendDTO;
import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.live.SeminarLiveAttendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "[Admin] Seminar Live", description = "어드민 - 세미나 라이브 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/live")
public class SeminarLiveAttendController {

    private final SeminarLiveAttendService seminarLiveAttendService;

    @GetMapping("/{seminarId}/attends")
    @Operation(summary = "수동 출석명단 조회 API -by 남성현", description = "세미나 출석 명단을 조회합니다. (오프라인 수동 출석)")
    public ApiResponse<SeminarLiveAttendDTO.AttendStudentDTOList> attendList(
            @PathVariable("seminarId") Long seminarId) {

        return ApiResponse.onSuccess("오프라인 출석 명단 리스트",seminarLiveAttendService.attendanceList(seminarId));
    }
}
