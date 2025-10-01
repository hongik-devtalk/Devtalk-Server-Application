package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SeminarRepository extends JpaRepository<Seminar,Long> {
    /**
     * 현재 시간을 기준으로 신청 가능한 세미나를 조회합니다.
     * @param now 현재 시간 (LocalDateTime)
     * @return 신청 기간에 해당하는 세미나
     */
    @Query("SELECT s FROM Seminar s WHERE s.startDate <= :now AND s.endDate >= :now")
    Seminar findSeminarInApplicationPeriod(@Param("now") LocalDateTime now);

    List<Seminar> findAllByOrderBySeminarNumDesc();
}
