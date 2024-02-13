package com.private_lbs.taskmaster.favorite.domain.repository;

import com.private_lbs.taskmaster.bat.domain.Bat;
import com.private_lbs.taskmaster.favorite.domain.Favorite;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteQueryRepository extends JpaRepository<Favorite, Long> {

    @Query("SELECT DISTINCT b " +
            "FROM Bat b " +
            "JOIN FETCH b.hitter h " +
            "JOIN FETCH b.pitcher p " +
            "JOIN FETCH h.team ht " +
            "JOIN FETCH p.team pt " +
            "JOIN FETCH b.favorite f " +
            "WHERE f.member.id = :memberId " +
            "ORDER BY b.createDateTime")
    List<Bat> getFavoriteProcessedVideo(@Param("memberId") long memberId);

}