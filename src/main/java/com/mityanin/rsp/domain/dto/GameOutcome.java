package com.mityanin.rsp.domain.dto;

import com.mityanin.rsp.domain.entity.PlayerScore;
import com.mityanin.rsp.domain.enums.GameEntity;
import com.mityanin.rsp.domain.enums.Result;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GameOutcome {

    private Result result;

    private GameEntity serverPick;

    private GameEntity yourPick;

    private PlayerScore statistics;
}
