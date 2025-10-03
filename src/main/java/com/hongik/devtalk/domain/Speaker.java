package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Speaker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speaker_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    // 소속
    @Column(length = 100)
    private String organization;

    // 이력
    @Lob
    private String history;

    @Column(length = 2048)
    private String profileUrl;

    @Column(length = 255)
    private String profileFileName;

    @Column(length = 20)
    private String profileFileExtension;

    private Long profileFileSize;

    @Column(unique = true)
    private String email;

    @Column(length = 20, unique = true)
    private String phone;

    @Lob
    private String etc;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();
}
