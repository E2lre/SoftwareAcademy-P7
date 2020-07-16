package com.nnk.springboot.ut;


import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.nnk.springboot.ut.UserControllerTest.asJsonString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
public class RatingControllerTest {
    private static final Logger logger = LogManager.getLogger(RatingControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
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

    /*---------------------------------------- GET -------------------------------*/
    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void home_sendModel_ratingListIsReturn() throws Exception {
        List<Rating> ratingList = new ArrayList<>();

        //GIVEN : Give an exiting rating
        ratingList.add(rating);
        Mockito.when(ratingRepository.findAll()).thenReturn(ratingList);
        //WHEN //THEN return rating list

        mockMvc.perform(get("/rating/list"))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void addRating_getAddRating_redirectIsReturn() throws Exception {


        //GIVEN : Give an exiting rating

        //WHEN //THEN return the rating

        mockMvc.perform(get("/rating/add")
                .content(asJsonString(rating))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }
    @Test
    public void showUpdateForm_giveAnExistingId_ratingIsReturn() throws Exception {
       //GIVEN : Give an exiting Rating
        Mockito.when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(rating));
        //WHEN //THEN return the Rating
        mockMvc.perform(get("/rating/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void deleteUser_giveAnExistingId_ratingIsDelete() throws Exception {

     //GIVEN : Give an exiting Rating
     Mockito.when(ratingRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(rating));
     doNothing().when(ratingRepository).delete(any(Rating.class));

     //WHEN //THEN return list
     mockMvc.perform(get("/rating/delete/1"))
             .andDo(print())
             .andExpect(status().is3xxRedirection())
             .andExpect(view().name("redirect:/rating/list"));

    }

    /*---------------------------------------- Post -------------------------------*/
    @Test

    public void validate_giveAnInexistingRating_theRatingIsCreate() throws Exception {


        //GIVEN : Give an exiting Rating
        Mockito.when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        //WHEN //THEN return the list
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
    }

    @Test
    public void validate_giveAnIncorrectOrderFormat_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Rating
        Mockito.when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        //WHEN //THEN return the add page
        mockMvc.perform(post("/rating/validate")
                .content(asJsonString(rating))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("moodysRating", String.valueOf(moodysConst))
                .param("sandPRating", String.valueOf(sandPConst))
                .param("fitchRating", String.valueOf(fitchConst))
                .param("orderNumber", "abc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"));
    }

    @Test
    public void updateRating_giveAnExistingId_ratingIsUpdate() throws Exception {

        //GIVEN : Give an exiting Rating

        Mockito.when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        //WHEN //THEN return the List
        mockMvc.perform(post("/rating/update/1")
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
    public void updateRating_giveAnIncorrectId_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Rating

        Mockito.when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        //WHEN //THEN return the update page
        mockMvc.perform(post("/rating/update/1")
                .content(asJsonString(rating))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("moodysRating", String.valueOf(moodysConst))
                .param("sandPRating", String.valueOf(sandPConst))
                .param("fitchRating", String.valueOf(fitchConst))
                .param("orderNumber", "abc"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"));
    }

}
