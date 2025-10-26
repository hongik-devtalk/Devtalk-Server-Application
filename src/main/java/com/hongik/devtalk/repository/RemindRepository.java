package com.hongik.devtalk.repository;

import com.hongik.devtalk.domain.Remind;
import com.hongik.devtalk.domain.enums.RemindStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;

public interface RemindRepository extends JpaRepository<Remind, Long> {

    boolean existsBySeminar_IdAndApplicant_IdAndScheduledAt(
            Long seminarId, Long applicantId, LocalDateTime scheduledAt);

    @Modifying
    @Query("""
    update Remind r set r.status = :status, r.sentAt = :sentAt
     where r.seminar.id = :seminarId
       and r.applicant.id = :applicantId
       and r.scheduledAt = :scheduledAt
  """)
    int updateStatus(@Param("seminarId") Long seminarId,
                     @Param("applicantId") Long applicantId,
                     @Param("scheduledAt") LocalDateTime scheduledAt,
                     @Param("status") RemindStatus status,
                     @Param("sentAt") LocalDateTime sentAt);
}
