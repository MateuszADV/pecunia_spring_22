package pecunia_22.controllers;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class NoteCollectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "ADMIN")
    @WithUserDetails("Mateusz.adv2@gmail.com")
    void shouldReturnEmptyNotesPageForAdmin() throws Exception {

        mockMvc.perform(
                        get("/note/collection/notes/page/1")
                                .param("currencyId", "567")
                                .param("status", "KOLEKCJA")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("note/collection/notes"))
                .andExpect(model().attributeExists("notes"));
    }
}