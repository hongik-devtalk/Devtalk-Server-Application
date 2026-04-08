package com.hongik.devtalk.service.seminar;

import com.hongik.devtalk.domain.Applicant;
import com.hongik.devtalk.domain.seminar.admin.dto.AdminStatsResponseDTO;
import com.hongik.devtalk.global.apiPayload.code.GeneralErrorCode;
import com.hongik.devtalk.global.apiPayload.exception.GeneralException;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminStatsService {

    private final SeminarRepository seminarRepository;
    private final ApplicantRepository applicantRepository;
    private final SeminarViewStatsService seminarViewStatsService;
    private final SearchStatsService searchStatsService;

    public AdminStatsResponseDTO.SeminarViewsResponseDTO getSeminarViews(Long seminarId, LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        validateSeminarExists(seminarId);

        List<AdminStatsResponseDTO.SeminarViewPointDTO> viewPoints = seminarViewStatsService
                .getDailyGraph(seminarId, from, to)
                .stream()
                .map(viewPoint -> AdminStatsResponseDTO.SeminarViewPointDTO.builder()
                        .date(viewPoint.date().atStartOfDay())
                        .viewCount(viewPoint.count())
                        .build())
                .toList();

        int totalViewCount = viewPoints.stream()
                .mapToInt(AdminStatsResponseDTO.SeminarViewPointDTO::getViewCount)
                .sum();

        return AdminStatsResponseDTO.SeminarViewsResponseDTO.builder()
                .seminarId(seminarId)
                .from(toStartOfDay(from))
                .to(toStartOfDay(to))
                .totalViewCount(totalViewCount)
                .viewPoints(viewPoints)
                .build();
    }

    public AdminStatsResponseDTO.SeminarInflowsResponseDTO getSeminarInflows(Long seminarId, LocalDate from, LocalDate to) {
        validateDateRange(from, to);
        validateSeminarExists(seminarId);

        LocalDateTime startDateTime = toStartOfDay(from);
        LocalDateTime endDateTime = toInclusiveEndOfDay(to);

        List<Applicant> applicants = applicantRepository.findBySeminar_IdAndCreatedAtBetween(
                seminarId,
                startDateTime,
                endDateTime
        );

        Map<String, Long> countsByInflow = applicants.stream()
                .collect(Collectors.groupingBy(
                        this::resolveInflowType,
                        Collectors.counting()
                ));

        int totalApplicantCount = applicants.size();

        List<AdminStatsResponseDTO.SeminarInflowDTO> inflows = countsByInflow.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .map(entry -> {
                    int applicantCount = entry.getValue().intValue();
                    return AdminStatsResponseDTO.SeminarInflowDTO.builder()
                            .inflowType(entry.getKey())
                            .applicantCount(applicantCount)
                            .percentage(AdminStatsResponseDTO.SeminarInflowDTO.calculatePercentage(
                                    applicantCount,
                                    totalApplicantCount
                            ))
                            .build();
                })
                .toList();

        return AdminStatsResponseDTO.SeminarInflowsResponseDTO.builder()
                .seminarId(seminarId)
                .from(startDateTime)
                .to(toStartOfDay(to))
                .totalApplicantCount(totalApplicantCount)
                .inflows(inflows)
                .build();
    }

    public AdminStatsResponseDTO.SearchKeywordStatsResponseDTO getSearchKeywordStats(
            LocalDate from,
            LocalDate to,
            String target
    ) {
        validateDateRange(from, to);

        String normalizedTarget = normalizeTarget(target);
        List<AdminStatsResponseDTO.TopKeywordDTO> keywords = searchStatsService.getTop5(normalizedTarget, from, to)
                .stream()
                .map(topKeyword -> AdminStatsResponseDTO.TopKeywordDTO.builder()
                        .keyword(topKeyword.keyword())
                        .searchCount(topKeyword.count())
                        .build())
                .toList();

        return AdminStatsResponseDTO.SearchKeywordStatsResponseDTO.builder()
                .target(normalizedTarget)
                .from(toStartOfDay(from))
                .to(toStartOfDay(to))
                .keywords(keywords)
                .build();
    }

    private void validateSeminarExists(Long seminarId) {
        seminarRepository.findById(seminarId)
                .orElseThrow(() -> new GeneralException(GeneralErrorCode.SEMINARINFO_NOT_FOUND));
    }

    private void validateDateRange(LocalDate from, LocalDate to) {
        if (from == null || to == null || from.isAfter(to)) {
            throw new GeneralException(GeneralErrorCode.INVALID_PARAMETER, "from/to 날짜 범위가 올바르지 않습니다.");
        }
    }

    private String normalizeTarget(String target) {
        if (target == null || target.isBlank()) {
            return SearchStatsService.TARGET_ALL;
        }

        String normalizedTarget = target.trim().toUpperCase(Locale.ROOT);
        if (!List.of(
                SearchStatsService.TARGET_ALL,
                SearchStatsService.TARGET_SEMINAR,
                SearchStatsService.TARGET_SPEAKER
        ).contains(normalizedTarget)) {
            throw new GeneralException(
                    GeneralErrorCode.INVALID_PARAMETER,
                    "target 값은 ALL, SEMINAR, SPEAKER 중 하나여야 합니다."
            );
        }
        return normalizedTarget;
    }

    private String resolveInflowType(Applicant applicant) {
        if (applicant.getInflowPathEtc() != null && !applicant.getInflowPathEtc().isBlank()) {
            return applicant.getInflowPathEtc().trim();
        }
        if (applicant.getInflowPath() != null) {
            return applicant.getInflowPath().name();
        }
        return "UNKNOWN";
    }

    private LocalDateTime toStartOfDay(LocalDate date) {
        return date.atStartOfDay();
    }

    private LocalDateTime toInclusiveEndOfDay(LocalDate date) {
        return date.plusDays(1).atStartOfDay().minusNanos(1);
    }
}
