package pecunia_22.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import pecunia_22.appUser.AppUserRole;
import pecunia_22.security.WithMockAppUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class MedalControllerIT {

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @WithMockUser(authorities = "USER")
//    void userShouldSeeVisibleMedal() throws Exception {
//
//        mockMvc.perform(get("/medal/collection/show/1"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("medal/collection/show/1"))
//                .andExpect(model().attributeExists("medal"));
//    }
//
//    @Test
//    @WithMockUser(authorities = "USER")
//    void userShouldGetMessageWhenMedalNotFound() throws Exception {
//
//        mockMvc.perform(get("/medal/collection/show/999999"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("message"));
//    }
//
//    @Test
//    @WithMockUser(authorities = "ADMIN")
//    void adminShouldAccessMedal() throws Exception {
//
//        mockMvc.perform(get("/medal/collection/show/2"))
//                .andExpectAll(view().name("/medal/collection/show//2"))
//                .andExpect(status().isOk())
//                .andExpect(model().attributeExists("medal"));
//    }




    @Test
    @WithMockAppUser
    void adminShouldAccessMedal() throws Exception {

        mockMvc.perform(get("/medal/collection/show/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("medal/collection/show"))
                .andExpect(model().attributeExists("medal"));
    }

    @Test
    @WithMockAppUser(
            email = "jan@test.com",
            firstName = "Jan",
            lastName = "Kowalski",
            role = AppUserRole.USER
    )
    void userShouldAccessMedal() throws Exception {

        mockMvc.perform(get("/medal/collection/show/2"))
                .andExpect(status().isOk());
    }
}
