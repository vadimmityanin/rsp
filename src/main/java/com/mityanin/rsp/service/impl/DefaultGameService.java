package com.mityanin.rsp.service.impl;

import com.mityanin.rsp.domain.dto.GameOutcome;
import com.mityanin.rsp.domain.entity.PlayerScore;
import com.mityanin.rsp.domain.entity.Stats;
import com.mityanin.rsp.domain.enums.GameEntity;
import com.mityanin.rsp.domain.enums.Result;
import com.mityanin.rsp.repository.PlayerScoreRepository;
import com.mityanin.rsp.repository.StatsRepository;
import com.mityanin.rsp.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.mityanin.rsp.domain.enums.Result.*;

@Service
@AllArgsConstructor
@Slf4j
public class DefaultGameService implements GameService {

    private final StatsRepository statsRepository;

    private final PlayerScoreRepository scoreRepository;

    private final Map<GameEntity, GameEntity> winMapping = Map.of(
            GameEntity.ROCK, GameEntity.SCISSORS,
            GameEntity.PAPER, GameEntity.ROCK,
            GameEntity.SCISSORS, GameEntity.PAPER
    );

    @Override
    @Transactional
    public GameOutcome play(String username, GameEntity userPick) {
        Optional<PlayerScore> maybeScore = scoreRepository.findByName(username);
        if (maybeScore.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("Processing new user: {}", username);
            }
            return handleNewUser(username, userPick);
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Processing existing user : {}", username);
            }
            PlayerScore playerScore = maybeScore.get();
            GameEntity lastPicked = playerScore.getLastPicked();
            Optional<Stats> maybeMostProbableTransition = getMostProbableStateTransition(username, lastPicked);

            if (maybeMostProbableTransition.isEmpty()) {
                GameEntity serverPick = GameEntity.getRandom();
                if (log.isDebugEnabled()) {
                    log.debug("Stats for user {} are empty, making random pick {}", username, serverPick);
                }
                return checkResultAndUpdateStatistics(username, userPick, playerScore, lastPicked, serverPick);
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("Processing existing user : {}", username);
                }
                GameEntity prediction = maybeMostProbableTransition.get().getTo();
                if (log.isDebugEnabled()) {
                    log.debug("Most probable state transition for user {} is {}", username, maybeMostProbableTransition);
                }
                GameEntity serverPick = getCounter(prediction);
                return checkResultAndUpdateStatistics(username, userPick, playerScore, lastPicked, serverPick);
            }

        }

    }

    private GameOutcome checkResultAndUpdateStatistics(String username, GameEntity userPick, PlayerScore playerScore, GameEntity lastPicked, GameEntity serverPick) {
        Result result = check(userPick, serverPick);
        updateUserStateTransitions(username, userPick, playerScore, lastPicked, result);
        return GameOutcome.builder().yourPick(userPick).result(result).statistics(playerScore).serverPick(serverPick).build();
    }

    private void updateUserStateTransitions(String username, GameEntity userPick, PlayerScore playerScore, GameEntity lastPicked, Result result) {
        statsRepository.findFirstByNameAndStartAndTo(username, lastPicked, userPick)
                .ifPresentOrElse(st -> st.setQuantity(st.getQuantity() + 1),
                        () -> statsRepository.save(Stats.builder().start(lastPicked).to(userPick).name(username).quantity(1L).build()));
        updatePlayerStats(result, userPick, playerScore);
    }

    private Optional<Stats> getMostProbableStateTransition(String username, GameEntity lastPicked) {
        return statsRepository.findFirstByNameAndStartOrderByQuantityDesc(username, lastPicked);
    }

    private Result check(GameEntity verifier, GameEntity verifiable) {
        return verifier.equals(verifiable)
                ? DRAW
                : winMapping.get(verifier).equals(verifiable)
                ? WIN
                : LOSS;
    }

    private GameEntity getCounter(GameEntity entity) {
        return winMapping.entrySet().stream().filter(entry -> entry.getValue().equals(entity))
                .findAny().map(Map.Entry::getKey).get();
    }

    private void updatePlayerStats(Result result, GameEntity userPick, PlayerScore playerScore) {
        if (result.equals(WIN)) {
            playerScore.setWins(playerScore.getWins() + 1);
        } else if (result.equals(LOSS)) {
            playerScore.setLosses(playerScore.getLosses() + 1);
        } else playerScore.setDraws(playerScore.getDraws() + 1);

        playerScore.setLastPicked(userPick);
    }

    private GameOutcome handleNewUser(String username, GameEntity userPick) {
        GameEntity serverPick = GameEntity.getRandom();
        Result result = check(userPick, serverPick);
        PlayerScore newUserScore = PlayerScore.builder().name(username).build();
        updatePlayerStats(result, userPick, newUserScore);
        scoreRepository.save(newUserScore);
        if (log.isDebugEnabled()) {
            log.debug("New user {} processing finished: {}, {}", username, result, newUserScore);
        }
        return GameOutcome.builder().result(result).serverPick(serverPick).yourPick(userPick).statistics(newUserScore).build();
    }


}
