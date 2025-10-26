package com.hongik.devtalk.domain;

import com.hongik.devtalk.domain.enums.SeminarStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    @Column(length = 2048)
    private String thumbnailUrl;

    @Column(length = 255)
    private String thumbnailFileName;

    @Column(length = 20)
    private String thumbnailFileExtension;

    private Long thumbnailFileSize;

    @Column(length = 50, nullable = false, unique = true)
    private int seminarNum; // 몇회차

    private String topic;

    //세미나가 진행되는 시간
    private LocalDateTime seminarDate;

    //신청 시작 시간
    private LocalDateTime startDate;

    //신청 마감 시간
    private LocalDateTime endDate;

    private String place;

    @Enumerated(EnumType.STRING)
    private SeminarStatus status;

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Attendance> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Review> reviews = new ArrayList<>();

    @OneToOne(mappedBy = "seminar", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Live live;

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<LiveFile> liveFiles = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Applicant> applicants = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @OneToMany(mappedBy = "seminar", cascade = CascadeType.ALL)
    private List<Remind> reminds = new ArrayList<>();

    public void updateInfo(Integer seminarNum, LocalDateTime seminarDate, String place, String topic,
                           LocalDateTime applyStartDate, LocalDateTime applyEndDate) {
        this.seminarNum = seminarNum;
        this.seminarDate = seminarDate;
        this.place = place;
        this.topic = topic;
        this.startDate = applyStartDate;
        this.endDate = applyEndDate;
    }

    public void updateLive(Live live) {
        this.live = live;
    }

    public void updateThumbnail(String url, String name, String ext, Long size) {
        this.thumbnailUrl = url;
        this.thumbnailFileName = name;
        this.thumbnailFileExtension = ext;
        this.thumbnailFileSize = size;
    }
}
