package com.hongik.devtalk.repository.live;

import com.hongik.devtalk.domain.Live;
import com.hongik.devtalk.domain.Seminar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LiveRepository extends JpaRepository<Live, Long> {
    Optional<Live> findBySeminar(Seminar seminar);
}
