package com.hongik.devtalk.repository.liveFile;

import com.hongik.devtalk.domain.LiveFile;
import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LiveFileRepository extends JpaRepository<LiveFile, Long> {
    List<LiveFile> findBySeminar(Seminar seminar);
    Optional<LiveFile> findByFileUrl(String fileUrl);
    List<LiveFile> findBySeminarId(Long seminarId);
}
