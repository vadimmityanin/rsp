package com.mityanin.rsp.integration;

import com.mityanin.rsp.domain.enums.GameEntity;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.mityanin.rsp.domain.enums.GameEntity.*;
import static com.mityanin.rsp.domain.enums.Result.DRAW;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class IntegrationTest {

    private final String apiPath = "/api/v1/rsp";

    @Autowired
    private MockMvc mvc;

    @Test
    public void mustUseMostProbableStateTransition() throws Exception {
        //as SCISSORS->PAPER transition was 2 times, SCISSORS->ROCK 1 time
        // Server suppose SCISSORS->PAPER transition the most probable and must pick counter to PAPER(it is SCISSORS)
        var expectedEntity = SCISSORS;
        //last user pick is SCISSORS vs most probable => DRAW
        var expectedResult = DRAW;

        var requests = List.of(
                Pair.of("user1", SCISSORS),
                Pair.of("user1", PAPER),

                Pair.of("user1", SCISSORS),
                Pair.of("user1", PAPER),

                Pair.of("user1", SCISSORS),
                Pair.of("user1", ROCK),

                Pair.of("user1", SCISSORS)
        );

        //running request for Server to get statistics
        for (var request : requests) {
            performRequest(request);
        }

        //running evaluation pick
        performRequest(Pair.of("user1", SCISSORS))
                .andExpect(jsonPath("$.result", Is.is(expectedResult.name())))
                .andExpect(jsonPath("$.serverPick", Is.is(expectedEntity.name())))
        ;
    }

    private ResultActions performRequest(Pair<String, GameEntity> request) throws Exception {
        ResultActions perform = mvc.perform(get(apiPath)
                .param("username", request.getFirst())
                .param("entity", request.getSecond().name())
                .contentType(MediaType.APPLICATION_JSON));
        return perform
                .andDo(print())
                .andExpect(status().isOk());
    }
}
