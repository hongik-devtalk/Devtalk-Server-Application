package com.hongik.devtalk.service.mail;

import com.hongik.devtalk.domain.*;
import com.hongik.devtalk.domain.enums.ParticipationType;
import com.hongik.devtalk.domain.enums.RemindStatus;
import com.hongik.devtalk.repository.ApplicantRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import com.hongik.devtalk.repository.RemindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class D1ReminderService {

    private final SeminarRepository seminarRepo;
    private final ApplicantRepository applicantRepo;
    private final RemindRepository remindRepo;
    private final MailSendService mailSendService;

    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public int sendD1Reminders() {
        ZonedDateTime now = ZonedDateTime.now(KST);
        LocalDate tomorrow = now.plusDays(1).toLocalDate();
        LocalDateTime from = tomorrow.atStartOfDay();
        LocalDateTime to   = tomorrow.plusDays(1).atStartOfDay().minusNanos(1);

        // 이번 실행의 “의도된 스케줄 시각” — D-1 10:00 KST
        LocalDateTime scheduledAt = LocalDateTime.of(now.toLocalDate(), LocalTime.of(10, 0));

        List<Seminar> seminars = seminarRepo.findStartingBetween(from, to);
        int sent = 0;
        for (Seminar s : seminars) {
            sent += fanoutAndSend(s, ParticipationType.ONLINE,  scheduledAt);
            sent += fanoutAndSend(s, ParticipationType.OFFLINE, scheduledAt);
        }
        return sent;
    }

    private int fanoutAndSend(Seminar seminar, ParticipationType type, LocalDateTime scheduledAt) {
        List<Applicant> targets = applicantRepo.findBySeminarIdAndTypeFetchStudent(seminar.getId(), type);
        int cnt = 0;
        for (Applicant a : targets) {
            // 중복 방지: 동일 (seminar, applicant, scheduledAt) 있으면 skip
            if (remindRepo.existsBySeminar_IdAndApplicant_IdAndScheduledAt(seminar.getId(), a.getId(), scheduledAt)) continue;

            // 선점 — 유니크키로 동시성 보호
            remindRepo.save(Remind.builder()
                    .seminar(seminar)
                    .applicant(a)
                    .status(RemindStatus.SCHEDULED)
                    .scheduledAt(scheduledAt)
                    .build());

            // 메일 발송
            Student st = a.getStudent();
            String email = st != null ? st.getEmail() : null;
            String name  = st != null ? st.getName()  : null;

            boolean ok = mailSendService.sendReminderMail(email, type, name, seminar);

            // 결과 반영
            remindRepo.updateStatus(seminar.getId(), a.getId(), scheduledAt,
                    ok ? RemindStatus.SUCCESS : RemindStatus.FAILED,
                    ok ? LocalDateTime.now() : null);

            if (ok) cnt++;
        }
        return cnt;
    }
}
