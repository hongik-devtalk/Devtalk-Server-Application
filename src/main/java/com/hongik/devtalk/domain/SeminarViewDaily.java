package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="seminar_view_daily")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SeminarViewDaily {

    @EmbeddedId
    private SeminarViewDailyId id;

    @Column(name="view_count", nullable=false)
    private int viewCount;

    @Builder
    private SeminarViewDaily(SeminarViewDailyId id, int viewCount) {
        this.id = id;
        this.viewCount = viewCount;
    }

    public static SeminarViewDaily create(Long seminarId, LocalDate date) {
        return SeminarViewDaily.builder()
                .id(new SeminarViewDailyId(seminarId, date))
                .viewCount(1)
                .build();
    }

    public void increment() { this.viewCount++; }

    @Embeddable
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class SeminarViewDailyId implements Serializable {
        @Column(name="seminar_id") private Long seminarId;
        @Column(name="view_date") private LocalDate viewDate;

        @Override public boolean equals(Object o){
            if(this==o) return true;
            if(!(o instanceof SeminarViewDailyId that)) return false;
            return Objects.equals(seminarId, that.seminarId) && Objects.equals(viewDate, that.viewDate);
        }
        @Override public int hashCode(){ return Objects.hash(seminarId, viewDate); }
    }
}