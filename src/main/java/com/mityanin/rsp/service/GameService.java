package com.mityanin.rsp.service;

import com.mityanin.rsp.domain.dto.GameOutcome;
import com.mityanin.rsp.domain.enums.GameEntity;

public interface GameService {

    GameOutcome play(String username, GameEntity entity);
}
