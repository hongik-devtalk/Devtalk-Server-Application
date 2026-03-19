package com.hongik.devtalk.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name="search_keyword_daily")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchKeywordDaily {

    @EmbeddedId
    private SearchKeywordDailyId id;

    @Column(name="search_count", nullable=false)
    private int searchCount;

    @Builder
    private SearchKeywordDaily(SearchKeywordDailyId id, int searchCount) {
        this.id = id;
        this.searchCount = searchCount;
    }

    public static SearchKeywordDaily create(String targetType, String keywordNorm, LocalDate date) {
        return SearchKeywordDaily.builder()
                .id(new SearchKeywordDailyId(targetType, keywordNorm, date))
                .searchCount(1)
                .build();
    }

    public void increment() { this.searchCount++; }

    @Embeddable
    @Getter @NoArgsConstructor @AllArgsConstructor
    public static class SearchKeywordDailyId implements Serializable {
        @Column(name="target_type", length=16) private String targetType;
        @Column(name="keyword_norm", length=100) private String keywordNorm;
        @Column(name="search_date") private LocalDate searchDate;

        @Override public boolean equals(Object o){
            if(this==o) return true;
            if(!(o instanceof SearchKeywordDailyId that)) return false;
            return Objects.equals(targetType, that.targetType)
                    && Objects.equals(keywordNorm, that.keywordNorm)
                    && Objects.equals(searchDate, that.searchDate);
        }
        @Override public int hashCode(){ return Objects.hash(targetType, keywordNorm, searchDate); }
    }
}
