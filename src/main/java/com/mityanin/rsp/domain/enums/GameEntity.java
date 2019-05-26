package com.mityanin.rsp.domain.enums;

import java.util.Random;

public enum GameEntity {
    ROCK, PAPER, SCISSORS;

    private static Random random = new Random();

    public static GameEntity getRandom(){
        return GameEntity.values()[random.nextInt(3)];
    }
}
