package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.models.ContestCategory;
import be.helha.api_recettapp.services.IContestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class of ContestController
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class TestContestController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IContestService contestService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Create a sample of contest
     * @return @type Contest contest object
     * @throws Exception
     */
    private Contest createSampleContest() throws Exception {
        ContestCategory category = new ContestCategory();
        category.setId(1L);
        category.setTitle("Cooking");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Contest contest = new Contest();
        contest.setId(1);
        contest.setTitle("Cooking Contest");
        contest.setMax_participants(50);
        contest.setStart_date(sdf.parse("2024-01-01"));
        contest.setEnd_date(sdf.parse("2024-01-10"));
        contest.setStatus("not yet begun");
        contest.setCategory(category);

        return contest;
    }

    /**
     * Test getContests controller
     * @throws Exception
     */
    @Test
    void testGetAllContests() throws Exception {
        Contest contest1 = createSampleContest();
        Contest contest2 = createSampleContest();
        contest2.setId(2);
        contest2.setTitle("Art Contest");

        List<Contest> contests = Arrays.asList(contest1, contest2);

        // Mock the contestService
        given(contestService.getContests()).willReturn(contests);

        mockMvc.perform(get("/contests/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].title").value("Cooking Contest"))
                .andExpect(jsonPath("$[1].title").value("Art Contest"));
    }

    /**
     * Test getContestsByName controller
     * @throws Exception
     */
    @Test
    void testGetContestByTitle() throws Exception {
        Contest contest = createSampleContest();
        List<Contest> contests = List.of(contest);

        given(contestService.getContestByTitle("Cooking Contest")).willReturn(contests);

        mockMvc.perform(get("/contests/Cooking Contest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Cooking Contest"));
    }

    /**
     * Test addContest controller
     * @throws Exception
     */
    @Test
    void testAddContest() throws Exception {
        Contest contest = createSampleContest();

        given(contestService.addContest(Mockito.any(Contest.class))).willReturn(contest);

        mockMvc.perform(post("/contests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cooking Contest"))
                .andExpect(jsonPath("$.max_participants").value(50));
    }

    /**
     * Test updateContest controller
     * @throws Exception
     */
    @Test
    void testUpdateContest() throws Exception {
        Contest contest = createSampleContest();
        contest.setTitle("Updated Contest");

        given(contestService.updateContest(Mockito.any(Contest.class))).willReturn(contest);

        mockMvc.perform(put("/contests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(contest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Contest"));
    }

    /**
     * Test deleteContest controller
     * @throws Exception
     */
    @Test
    void testDeleteContest() throws Exception {
        Mockito.doNothing().when(contestService).deleteContest(1);

        mockMvc.perform(delete("/contests/1"))
                .andExpect(status().isOk());
    }
}
