package com.nnk.springboot.ut;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.TradeRepository;
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

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
public class BidListControllerTest {

    private static final Logger logger = LogManager.getLogger(BidListControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListRepository bidListRepository;

    private BidList bidList;
    //constantes de test
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    int bidListIDConst = 1;
    String accountConst = "Account";
    String typeConst = "Type";
    Double bidQuantityConst = 10d;

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

    /*---------------------------------------- GET CurvePoint-------------------------------*/
    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void home_sendModel_bidListIstIsReturn() throws Exception {
        List<BidList> bidLists = new ArrayList<>();

        //GIVEN : Give an exiting bidList
        bidLists.add(bidList);
        Mockito.when(bidListRepository.findAll()).thenReturn(bidLists);
        //WHEN //THEN return the bidList List
        mockMvc.perform(get("/bidList/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void addBidList_getAddBidList_redirectIsReturn() throws Exception {
        //GIVEN : Give an exiting bidList
        //WHEN //THEN return the bidList

        mockMvc.perform(get("/bidList/add")
                .content(asJsonString(bidListIDConst))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }
    @Test
    public void showUpdateForm_giveAnExistingId_bidListIsReturn() throws Exception {
        //GIVEN : Give an exiting bidList
        Mockito.when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(bidList));
        //Mockito.when(bidListRepository.findById(anyInt())).thenReturn(bidList);

        //WHEN //THEN return the bidList
        mockMvc.perform(get("/bidList/update/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_giveAnExistingId_bidListIsDelete() throws Exception {

        //GIVEN : Give an exiting trade
        Mockito.when(bidListRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(bidList));
        //Mockito.when(bidListRepository.findById(anyInt())).thenReturn(bidList);
        doNothing().when(bidListRepository).delete(any(BidList.class));
        //WHEN //THEN return list
        mockMvc.perform(get("/bidList/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bidList/list"));
    }
    /*---------------------------------------- Post -------------------------------*/
    @Test
    public void validate_giveAnInexistingBidList_theBidListIsCreate() throws Exception {

        //GIVEN : Give an exiting bidList
        Mockito.when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        //WHEN //THEN return the list
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
    }
    /*---------------------------------------- Post -------------------------------*/
    @Test
    public void validate_giveAnIncorrectBidListIdFormat_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting bidList
        //Mockito.when(bidListRepository.findById(bidListIDConst)).thenReturn(bidList);

        Mockito.when(bidListRepository.findById(bidListIDConst)).thenReturn(java.util.Optional.ofNullable(bidList));
        //Mockito.when(bidListRepository.findById(bidListIDConst)).thenReturn(bidList);
        Mockito.when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        //WHEN //THEN return the add page
        mockMvc.perform(post("/bidList/validate")
                .content(asJsonString(bidList))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("bidListId", "abc")
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("bidQuantity", String.valueOf(bidQuantityConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"));
    }
    @Test
    public void updateBidList_giveAnExistingId_BidListIsUpdate() throws Exception {

        //GIVEN : Give an exiting bidList
        Mockito.when(bidListRepository.findById(bidListIDConst)).thenReturn(java.util.Optional.ofNullable(bidList));
        //Mockito.when(bidListRepository.findById(bidListIDConst)).thenReturn(bidList);

        Mockito.when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        //WHEN //THEN return the list
        mockMvc.perform(post("/bidList/update/1")
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
    }
    @Test
    public void updateBidList_giveAnIncorrectId_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting BidList

        Mockito.when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        //WHEN //THEN return the update page
        mockMvc.perform(post("/bidList/update/1")
                .content(asJsonString(bidList))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("bidListId", "abc")
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("bidQuantity", String.valueOf(bidQuantityConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));
    }

}
