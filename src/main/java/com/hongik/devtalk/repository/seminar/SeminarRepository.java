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

    boolean existsBySeminarNum(Integer seminarNum);

    boolean existsBySeminarNumAndIdNot(Integer seminarNum, Long id);

    @Query("select s.seminarNum from Seminar s order by s.seminarNum desc")
    List<Integer> findAllSeminarNums();

    // 활성화 기간이 겹치는 세미나 존재 여부 확인
    @Query("SELECT COUNT(s) > 0 FROM Seminar s " +
            "WHERE s.activeStartDate <= :activeEndDate " +
            "AND s.activeEndDate >= :activeStartDate")
    boolean overlapsSeminar(LocalDateTime activeStartDate, LocalDateTime activeEndDate);

    // 활성화 기간이 겹치는 세미나 존재 여부 확인 (자기 자신 제외 - 수정용)
    @Query("SELECT COUNT(s) > 0 FROM Seminar s " +
            "WHERE s.id <> :seminarId " +
            "AND s.activeStartDate <= :activeEndDate " +
            "AND s.activeEndDate >= :activeStartDate")
    boolean overlapsSeminarAndIdNot(Long seminarId, LocalDateTime activeStartDate, LocalDateTime activeEndDate);

}
