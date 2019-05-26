package com.mityanin.rsp.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class PlayerScore {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Column
    private String name;

    @Column
    private long wins;

    @Column
    private long losses;

    @Column
    private long draws;

    @Column
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private GameEntity lastPicked;
}
