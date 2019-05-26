package com.mityanin.rsp.domain.entity;

import com.mityanin.rsp.domain.enums.GameEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stats {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private GameEntity start;

    @Column
    @Enumerated(EnumType.STRING)
    private GameEntity to;

    @Column
    private long quantity;
}
