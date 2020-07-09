package com.nnk.springboot.ut;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.repositories.RuleNameRepository;
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
public class RuleNameControllerTest {

    private static final Logger logger = LogManager.getLogger(RuleNameControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameRepository ruleNameRepository;

    private RuleName ruleName;

    //constantes de test
    int idConst = 1;
    String nameConst="Name";
    String descriptionConst ="description";
    String jsonConst ="json";
    String templateConst = "template";
    String sqlStrConst = "sqlStr";
    String sqlPartConst = "sqlPart";

        @BeforeEach
    public void setUpEach() {
        ruleName = new RuleName();
        ruleName.setId(idConst);
        ruleName.setName(nameConst);
        ruleName.setDescription(descriptionConst);
        ruleName.setJson(jsonConst);
        ruleName.setTemplate(templateConst);
        ruleName.setSqlStr(sqlStrConst);
        ruleName.setSqlPart(sqlPartConst);
    }

    /*---------------------------------------- GET -------------------------------*/
    @Test
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    public void home_sendModel_ruleNameListIsReturn() throws Exception {
        List<RuleName> ruleNameList = new ArrayList<>();

        //GIVEN : Give an exiting ruleName
        ruleNameList.add(ruleName);
        Mockito.when(ruleNameRepository.findAll()).thenReturn(ruleNameList);
        //WHEN //THEN return ruleName list

        mockMvc.perform(get("/ruleName/list"))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void addRating_getAddRuleName_redirectIsReturn() throws Exception {


        //GIVEN : Give an exiting ruleName

        //WHEN //THEN return the ruleName

        mockMvc.perform(get("/ruleName/add")
                .content(asJsonString(ruleName))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }
    @Test
    public void showUpdateForm_giveAnExistingId_ruleNameIsReturn() throws Exception {
        //GIVEN : Give an exiting RuleName
        Mockito.when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(ruleName));
        //WHEN //THEN return the RuleName
        mockMvc.perform(get("/ruleName/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void deleteUser_giveAnExistingId_ruleNameIsDelete() throws Exception {

        //GIVEN : Give an exiting Rating
        Mockito.when(ruleNameRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(ruleName));
        doNothing().when(ruleNameRepository).delete(any(RuleName.class));

        //WHEN //THEN return list
        mockMvc.perform(get("/ruleName/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));

    }

    /*---------------------------------------- Post -------------------------------*/
    @Test

    public void validate_giveAnInexistingRuleName_theRuleNameIsCreate() throws Exception {


        //GIVEN : Give an exiting Rating
        Mockito.when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        //WHEN //THEN return the list
        mockMvc.perform(post("/ruleName/validate")
                .content(asJsonString(ruleName))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", String.valueOf(nameConst))
                .param("description", String.valueOf(descriptionConst))
                .param("json", String.valueOf(jsonConst))
                .param("template", String.valueOf(templateConst))
                .param("sqlStr", String.valueOf(sqlStrConst))
                .param("sqlPart", String.valueOf(sqlPartConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));
    }
    @Test
    public void validate_giveAnIncorrectOrderFormat_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Rating
        Mockito.when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        //WHEN //THEN return the add page
        mockMvc.perform(post("/ruleName/validate")
                .content(asJsonString(ruleName))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", String.valueOf(""))
                .param("description", String.valueOf(descriptionConst))
                .param("json", String.valueOf(jsonConst))
                .param("template", String.valueOf(templateConst))
                .param("sqlStr", String.valueOf(sqlStrConst))
                .param("sqlPart", String.valueOf(sqlPartConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    public void updateRating_giveAnExistingId_ruleNameIsUpdate() throws Exception {

        //GIVEN : Give an exiting Rating

        Mockito.when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        //WHEN //THEN return the List
        mockMvc.perform(post("/ruleName/update/1")
                .content(asJsonString(ruleName))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", String.valueOf(nameConst))
                .param("description", String.valueOf(descriptionConst))
                .param("json", String.valueOf(jsonConst))
                .param("template", String.valueOf(templateConst))
                .param("sqlStr", String.valueOf(sqlStrConst))
                .param("sqlPart", String.valueOf(sqlPartConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ruleName/list"));
    }

    @Test
    public void updateRating_giveAnIncorrectValue_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Rating

        Mockito.when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        //WHEN //THEN return the update page
        mockMvc.perform(post("/ruleName/update/1")
                .content(asJsonString(ruleName))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("name", String.valueOf(""))
                .param("description", String.valueOf(descriptionConst))
                .param("json", String.valueOf(jsonConst))
                .param("template", String.valueOf(templateConst))
                .param("sqlStr", String.valueOf(sqlStrConst))
                .param("sqlPart", String.valueOf(sqlPartConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));
    }

}
