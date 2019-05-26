package com.mityanin.rsp.repository;

import com.mityanin.rsp.domain.entity.Stats;
import com.mityanin.rsp.domain.enums.GameEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StatsRepository extends CrudRepository<Stats, Long> {

    Optional<Stats> findFirstByNameAndStartOrderByQuantityDesc(String name, GameEntity from);

    Optional<Stats> findFirstByNameAndStartAndTo(String name, GameEntity from, GameEntity to);
}
