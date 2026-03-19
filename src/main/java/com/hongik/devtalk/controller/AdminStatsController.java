package com.hongik.devtalk.controller;

import com.hongik.devtalk.global.apiPayload.ApiResponse;
import com.hongik.devtalk.service.seminar.SearchStatsService;
import com.hongik.devtalk.service.seminar.SeminarViewStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/stats")
public class AdminStatsController {

    private final SeminarViewStatsService seminarViewStatsService;
    private final SearchStatsService searchStatsService;

    //세미나 카드별 조회수 (일 단위 그래프)
    @GetMapping("/seminars/{seminarId}/views")
    public ApiResponse<List<SeminarViewStatsService.ViewPoint>> seminarViews(
            @PathVariable Long seminarId,
            @RequestParam String from,
            @RequestParam String to
    ) {
        var points = seminarViewStatsService.getDailyGraph(seminarId, LocalDate.parse(from), LocalDate.parse(to));
        return ApiResponse.onSuccess("세미나 조회수 그래프 조회 성공", points);
    }

    //인기 검색어 Top5 (막대그래프용)
    // target=ALL | SEMINAR | SPEAKER
    @GetMapping("/search/top5")
    public ApiResponse<List<SearchStatsService.TopKeyword>> top5(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(defaultValue = SearchStatsService.TARGET_ALL) String target
    ) {
        var res = searchStatsService.getTop5(target, LocalDate.parse(from), LocalDate.parse(to));
        return ApiResponse.onSuccess("인기 검색어 TOP5 조회 성공", res);
    }
}