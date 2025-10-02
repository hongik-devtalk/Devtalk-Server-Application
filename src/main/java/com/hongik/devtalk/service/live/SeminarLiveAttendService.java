package com.hongik.devtalk.service.live;

import com.hongik.devtalk.domain.Attendance;
import com.hongik.devtalk.domain.Student;
import com.hongik.devtalk.domain.enums.ParticipationType;
import com.hongik.devtalk.domain.live.dto.SeminarLiveAttendDTO;
import com.hongik.devtalk.repository.AttendanceRepository;
import com.hongik.devtalk.repository.seminar.SeminarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeminarLiveAttendService {

    private final AttendanceRepository attendanceRepository;
    private final SeminarRepository seminarRepository;

    public SeminarLiveAttendDTO.AttendStudentDTOList attendanceList(Long seminarId) {

        List<Attendance> attendanceList = attendanceRepository.findBySeminarAndApplicant_ParticipationType(
                seminarRepository.findById(seminarId).get(), ParticipationType.OFFLINE);

        List<SeminarLiveAttendDTO.AttendStudentDTO> students = attendanceList.stream()
                .map(Attendance::getApplicant)
                .map(app -> {
                    Student s = app.getStudent();

                    List<String> departments = s.getDepartments().stream()
                            .map(Enum::name)
                            .distinct()
                            .toList();

                    return SeminarLiveAttendDTO.AttendStudentDTO.builder()
                            .studentId(s.getId())
                            .studentNum(s.getStudentNum())
                            .grade(s.getGrade())
                            .studentName(s.getName())
                            .department(departments)
                            .build();
                })
                .toList();

        return SeminarLiveAttendDTO.AttendStudentDTOList.builder()
                .students(students)
                .build();
    }
}
