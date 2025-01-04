package be.helha.api_recettapp.controllers;

import be.helha.api_recettapp.models.Entry;
import be.helha.api_recettapp.models.Users;
import be.helha.api_recettapp.models.Contest;
import be.helha.api_recettapp.services.IEntryService;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for EntryController
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TestEntryController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEntryService entryService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Create a sample of Entry
     *
     * @return entry the entry created
     */
    private Entry createSampleEntry() {
        Users user = new Users();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("johndoe@example.com");

        Contest contest = new Contest();
        contest.setId(1);
        contest.setTitle("Cooking Contest");
        contest.setMax_participants(50);

        Entry entry = new Entry();
        entry.setId(1);
        entry.setUsers(user);
        entry.setContest(contest);
        entry.setStatus("pending");
        entry.setUuid(UUID.randomUUID());

        return entry;
    }

    /**
     * Tests the GET endpoint to fetch all entries.
     * Checks if the response contains the correct number of entries
     * and if the data matches the expected values.
     *
     * @throws Exception if an error occurs during the request or response processing.
     */
    @Test
    void testGetAllEntries() throws Exception {
        Entry entry1 = createSampleEntry();
        Entry entry2 = createSampleEntry();
        entry2.setId(2);

        List<Entry> entries = Arrays.asList(entry1, entry2);

        given(entryService.getEntries()).willReturn(entries);

        mockMvc.perform(get("/entries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }




}

