package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long id;

    @Column(name = "tag_text", nullable = false, unique = true, length = 50)
    private String tagText;

    @Column(nullable = false)
    private Long searchCount;

    public void increaseSearchCount() {
        if (this.searchCount == null) {
            this.searchCount = 0L;
        }

        this.searchCount++;
    }
}