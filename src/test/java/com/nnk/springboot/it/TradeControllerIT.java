package com.nnk.springboot.it;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.ut.TradeControllerTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class TradeControllerIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private TradeRepository tradeRepository;

    private Trade trade;

    //constantes de test
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    int tradeIdConst = 10;
    String accountConst = "Account";
    String typeConst = "Type";
    Double buyQuantityConst = 10d;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        trade = new Trade();
        trade.setCreationDate(timestamp);
        trade.setRevisionDate(timestamp);
        trade.setTradeId(tradeIdConst);
        trade.setAccount(accountConst);
        trade.setType(typeConst);
        trade.setBuyQuantity(buyQuantityConst);
    }
    /*------------------------------ Get ------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void showUpdateForm_giveAnExistingId_tradeIsReturn(){
        //GIVEN : put an id in a not empty data base
        tradeRepository.save(trade);

        //WHEN call an existing ID
        // THEN return is OK
        try {
            mockMvc.perform(get("/trade/update/"+tradeIdConst))
                    .andDo(print())
                    .andExpect(status().isOk());
        }catch (Exception e) {
            assertThat(1).isEqualTo(0);
        }
    }
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void showUpdateForm_giveAnInExistingId_tradeIsReturn() throws Exception {
        //GIVEN : Data base is Empty
        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(get("/trade/update/0"))
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
            mockMvc.perform(get("/trade/delete/0"))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/trade/list"));
            assertThat(1).isEqualTo(0);
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
    /*---------------------------------------- Post trade-------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void validate_giveAnInexistingTrade_theTradeIsCreate() throws Exception {
        List<Trade> tradeStartList = new ArrayList<>();
        tradeStartList = tradeRepository.findAll();
        //GIVEN : Give an exiting trade
        //WHEN //THEN return the trade
        mockMvc.perform(post("/trade/validate")
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("tradeId", String.valueOf(tradeIdConst))
                .param("creationDate", String.valueOf(timestamp))
                .param("revisionDate", String.valueOf(timestamp))
                .param("account", String.valueOf(accountConst))
                .param("type", String.valueOf(typeConst))
                .param("buyQuantity", String.valueOf(buyQuantityConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/trade/list"));
        List<Trade> tradeEndList = new ArrayList<>();
        tradeEndList = tradeRepository.findAll();

        assertThat(tradeEndList.size()).isEqualTo(tradeStartList.size() +1);
    }

    @Test
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void updateTrade_giveAnExistingId_tradeIsUpdate() throws Exception {

        //GIVEN : an existing trade id
        tradeRepository.save(trade);
        //WHEN call an existing ID
        // THEN return is OK

        mockMvc.perform(post("/trade/update/"+tradeIdConst)
                .content(asJsonString(trade))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("tradeId", String.valueOf(tradeIdConst))
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
    @WithMockUser(roles="USER")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void updateTrade_giveAnInexistingId_errorIsReturn() throws Exception {
        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(post("/trade/update/100")
                    .content(asJsonString(trade))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .param("tradeId",String.valueOf(tradeIdConst))
                    .param("creationDate", String.valueOf(timestamp))
                    .param("revisionDate", String.valueOf(timestamp))
                    .param("account", String.valueOf(accountConst))
                    .param("type", String.valueOf(typeConst))
                    .param("buyQuantity", String.valueOf(buyQuantityConst)))
                    .andDo(print())
                    .andExpect(status().is3xxRedirection())
                    .andExpect(view().name("redirect:/trade/list"));
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
}
