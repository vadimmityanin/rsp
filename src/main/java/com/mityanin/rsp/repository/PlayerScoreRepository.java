package com.mityanin.rsp.repository;

import com.mityanin.rsp.domain.entity.PlayerScore;
import com.mityanin.rsp.domain.entity.Stats;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PlayerScoreRepository extends CrudRepository<PlayerScore, Long> {

    Optional<PlayerScore> findByName(String name);
}
