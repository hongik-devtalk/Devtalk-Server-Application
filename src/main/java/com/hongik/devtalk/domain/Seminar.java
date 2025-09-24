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
public class Seminar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seminar_id")
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String seminarNum; // 몇회차

    private String topic;

    private LocalDate startDate;

    private LocalDate endDate;

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "seminar", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Live live;

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Applicant> applicants = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Remind> reminds = new ArrayList<>();
}
