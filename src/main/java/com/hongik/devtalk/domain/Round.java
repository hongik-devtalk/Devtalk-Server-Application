package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_id")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String roundNum;

    private String topic;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Applicant> applicants = new ArrayList<>();

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<Lecture> lectures = new ArrayList<>();
}
