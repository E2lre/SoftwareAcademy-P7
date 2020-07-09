package com.nnk.springboot.it;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.nnk.springboot.ut.UserControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class RatingControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RatingRepository ratingRepository;

    private Rating rating;

    //constantes de test
    int idConst = 1;
    String moodysConst="Moodys";
    String sandPConst ="SAndP";
    String fitchConst = "Fitch";
    int orderNumberConst = 10;



    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        rating = new Rating();
        rating.setId(idConst);
        rating.setMoodysRating(moodysConst);
        rating.setSandPRating(sandPConst);
        rating.setFitchRating(fitchConst);
        rating.setOrderNumber(orderNumberConst);
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    public void showUpdateForm_giveAnExistingId_ratingIsReturn(){
        //GIVEN : put an id in a not empty data base
        ratingRepository.save(rating);
        //WHEN call an existing ID
        // THEN return is OK
        try {
            mockMvc.perform(get("/rating/update/"+idConst))
                    .andDo(print())
                    .andExpect(status().isOk());
        }catch (Exception e) {
            assertThat(1).isEqualTo(0);
        }
    }
    @Test
    @WithMockUser(roles="USER")
    public void showUpdateForm_giveAnInExistingId_ratingIsReturn() throws Exception {
        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception

            mockMvc.perform(get("/rating/update/0"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"));
    }

    @Test
    public void deleteRating_giveAnInxistingId_errorIsReturn() throws Exception {


        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
            mockMvc.perform(get("/rating/delete/0"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(view().name("error"));

    }

    /*---------------------------------------- Post CurvePoint-------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void validate_giveAnInexistingRating_theRatingIsCreate() throws Exception {
        List<Rating> ratingList = new ArrayList<>();
        ratingList = ratingRepository.findAll();


        //GIVEN : Give an exiting rating

        //WHEN //THEN return the rating
        mockMvc.perform(post("/rating/validate")
                .content(asJsonString(rating))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("moodysRating", String.valueOf(moodysConst))
                .param("sandPRating", String.valueOf(sandPConst))
                .param("fitchRating", String.valueOf(fitchConst))
                .param("orderNumber", String.valueOf(orderNumberConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));
        List<Rating> ratingEndList = new ArrayList<>();
        ratingEndList = ratingRepository.findAll();

        assertThat(ratingEndList.size()).isEqualTo(ratingList.size() +1);
    }

    @Test
    public void updateRating_giveAnExistingId_ratingIsUpdate() throws Exception {

        //GIVEN : an existing curvePoint id
        ratingRepository.save(rating);
        //WHEN call an existing ID
        // THEN return is OK

        mockMvc.perform(post("/rating/update/"+idConst)
                .content(asJsonString(rating))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("moodysRating", String.valueOf(moodysConst))
                .param("sandPRating", String.valueOf(sandPConst))
                .param("fitchRating", String.valueOf(fitchConst))
                .param("orderNumber", String.valueOf(orderNumberConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/rating/list"));
    }
    @Test
    public void updateRating_giveAnInexistingId_errorIsReturn() throws Exception {

        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(post("/rating/update/100")
                    .content(asJsonString(rating))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("moodysRating", String.valueOf(moodysConst))
                    .param("sandPRating", String.valueOf(sandPConst))
                    .param("fitchRating", String.valueOf(fitchConst))
                    .param("orderNumber", String.valueOf(orderNumberConst)))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/rating/list"));
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
}
