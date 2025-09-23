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

    @Column(length = 100)
    private String organization;

    @Lob
    private String history;

    @Column(length = 2048)
    private String profileUrl;

    @Column(unique = true)
    private String email;

    @Column(length = 20, unique = true)
    private String phone;

    @Lob
    private String etc;

    @OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL)
    private List<Lecture> lectures = new ArrayList<>();
}
