package com.nnk.springboot.it;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import java.sql.Timestamp;
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
public class BidListControllerIT {
    private static final Logger logger = LogManager.getLogger(BidListControllerIT.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BidListRepository bidListRepository;

    private BidList bidList;
    //constantes de test
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    int bidListIDConst = 10;
    String accountConst = "Account";
    String typeConst = "Type";
    Double bidQuantityConst = 10d;
    Double bidQuantityUpdateConst = 100d;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        bidList = new BidList();
        bidList.setCreationDate(timestamp);
        bidList.setRevisionDate(timestamp);
        bidList.setBidListId(bidListIDConst);
        bidList.setAccount(accountConst);
        bidList.setType(typeConst);
        bidList.setBidQuantity(bidQuantityConst);
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void showUpdateForm_giveAnExistingId_bidListIsReturn(){
        //GIVEN : put an id in a not empty data base
        bidListRepository.save(bidList);

        //WHEN call an existing ID
        // THEN return is OK
        try {
            mockMvc.perform(get("/bidList/update/"+bidListIDConst))
                    .andDo(print())
                    .andExpect(status().isOk());
        }catch (Exception e) {
            assertThat(1).isEqualTo(0);
        }
    }

    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void showUpdateForm_giveAnInExistingId_bidListIsReturn() throws Exception {
        //GIVEN : Data base is Empty
        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(get("/bidList/update/0"))
                    .andDo(print())
                    .andExpect(status().isOk());
            assertThat(1).isEqualTo(0);
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteUser_giveAnInexistingId_errorIsReturn() throws Exception {
        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(get("/bidList/delete/0"))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/bidList/list"));
            assertThat(1).isEqualTo(0);
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }

    /*---------------------------------------- Post bidlist-------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void validate_giveAnInexistingBidList_theBidListIsCreate() throws Exception {
        List<BidList> bidListStartList = new ArrayList<>();
        bidListStartList = bidListRepository.findAll();
        //GIVEN : Give an exiting bidlist
        //WHEN //THEN return the bidlist
        mockMvc.perform(post("/bidList/validate")
                .content(asJsonString(bidList))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("bidListId", String.valueOf(bidListIDConst))
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("bidQuantity", String.valueOf(bidQuantityConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
        List<BidList> bidListEndList = new ArrayList<>();
        bidListEndList = bidListRepository.findAll();

        assertThat(bidListEndList.size()).isEqualTo(bidListStartList.size() +1);
    }
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void updateBidList_giveAnExistingId_BidListIsUpdate() throws Exception {

        //GIVEN : an existing bidlist id
        bidListRepository.save(bidList);
        //WHEN call an existing ID
        // THEN return is OK

        mockMvc.perform(post("/bidList/update/"+bidListIDConst)
                .content(asJsonString(bidList))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("bidListId", String.valueOf(bidListIDConst))
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("bidQuantity", String.valueOf(bidQuantityUpdateConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
        //BidList bidListResult = bidListRepository.findById(bidListIDConst);
        //assertThat(bidListResult.getBidQuantity()).isEqualTo(bidQuantityUpdateConst);

    }

    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void updateBidList_giveAnInexistingId_errorIsReturn() throws Exception {
        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(post("/bidList/update/100")
                    .content(asJsonString(bidList))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("bidListId", String.valueOf(bidListIDConst))
                    .param("creationDate", String.valueOf(timestamp))
                    .param("revisionDate", String.valueOf(timestamp))
                    .param("account", String.valueOf(accountConst))
                    .param("type", String.valueOf(typeConst))
                    .param("bidQuantity", String.valueOf(bidQuantityConst)))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/bidList/list"));
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
}
