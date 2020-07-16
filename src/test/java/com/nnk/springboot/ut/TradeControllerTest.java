package com.nnk.springboot.ut;

import com.nnk.springboot.domain.Trade;
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
public class TradeControllerTest {

    private static final Logger logger = LogManager.getLogger(TradeControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeRepository tradeRepository;

    private Trade trade;

    //constantes de test
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    int tradeIDConst = 1;
    String accountConst = "Account";
    String typeConst = "Type";
    Double buyQuantityConst = 10d;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        trade = new Trade();
        trade.setCreationDate(timestamp);
        trade.setRevisionDate(timestamp);
        trade.setTradeId(tradeIDConst);
        trade.setAccount(accountConst);
        trade.setType(typeConst);
        trade.setBuyQuantity(buyQuantityConst);
     }
    /*---------------------------------------- GET CurvePoint-------------------------------*/
    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void home_sendModel_tradeistIsReturn() throws Exception {
        List<Trade> tradeList = new ArrayList<>();

        //GIVEN : Give an exiting trade
        tradeList.add(trade);
        Mockito.when(tradeRepository.findAll()).thenReturn(tradeList);
        //WHEN //THEN return the trade List
        mockMvc.perform(get("/trade/list"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    public void addtrade_getAddTrade_redirectIsReturn() throws Exception {
        //GIVEN : Give an exiting trade
        //WHEN //THEN return the trade

        mockMvc.perform(get("/trade/add")
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }
    @Test
    public void showUpdateForm_giveAnExistingId_tradeIsReturn() throws Exception {
        //GIVEN : Give an exiting trade
        Mockito.when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(trade));
        //WHEN //THEN return the trade
        mockMvc.perform(get("/trade/update/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void deleteUser_giveAnExistingId_tradeIsDelete() throws Exception {

        //GIVEN : Give an exiting trade
        Mockito.when(tradeRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(trade));
        doNothing().when(tradeRepository).delete(any(Trade.class));
        //WHEN //THEN return list
        mockMvc.perform(get("/trade/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
    }
    /*---------------------------------------- Post trade-------------------------------*/
    @Test
    public void validate_giveAnInexistingTrade_theTradeIsCreate() throws Exception {

        //GIVEN : Give an exiting Trade
        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        //WHEN //THEN return the list
        mockMvc.perform(post("/trade/validate")
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("tradeId", String.valueOf(tradeIDConst))
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("buyQuantity", String.valueOf(buyQuantityConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
    }
    /*---------------------------------------- Post trade-------------------------------*/
    @Test
     public void validate_giveAnIncorrectTradeIdFormat_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Trade

        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        //WHEN //THEN return the add page
        mockMvc.perform(post("/trade/validate")
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("tradeId", "abc")
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("buyQuantity", String.valueOf(buyQuantityConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }
    @Test
    public void updateTrade_giveAnExistingId_tradeIsUpdate() throws Exception {

        //GIVEN : Give an exiting trade
        Mockito.when(tradeRepository.findById(tradeIDConst)).thenReturn(java.util.Optional.ofNullable(trade));
        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        //WHEN //THEN return the list
        mockMvc.perform(post("/trade/update/1")
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("tradeId", String.valueOf(tradeIDConst))
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("buyQuantity", String.valueOf(buyQuantityConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
    }
    @Test
    public void updateTrade_giveAnIncorrectId_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting trade

        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        //WHEN //THEN return the update page
        mockMvc.perform(post("/trade/update/1")
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("tradeId", "abc")
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("buyQuantity", String.valueOf(buyQuantityConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }
}
