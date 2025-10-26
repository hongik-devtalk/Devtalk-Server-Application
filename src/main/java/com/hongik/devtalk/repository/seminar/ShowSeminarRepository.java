package com.hongik.devtalk.repository.seminar;

import com.hongik.devtalk.domain.ShowSeminar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowSeminarRepository extends JpaRepository<ShowSeminar,Long> {
    Optional<ShowSeminar> findFirstByApplicantActivateTrue();

    Optional<ShowSeminar> findFirstByLiveActivateTrue();
}
