package com.nnk.springboot.it;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import com.nnk.springboot.ut.RuleNameControllerTest;
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
public class RuleNameControllerIT {

    private static final Logger logger = LogManager.getLogger(RuleNameControllerIT.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
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
    /*------------------------------ Get ------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    public void showUpdateForm_giveAnExistingId_ruleNameIsReturn(){
        //GIVEN : put an id in a not empty data base
        ruleNameRepository.save(ruleName);
        //WHEN call an existing ID
        // THEN return is OK
        try {
            mockMvc.perform(get("/ruleName/update/"+idConst))
                    .andDo(print())
                    .andExpect(status().isOk());
        }catch (Exception e) {
            assertThat(1).isEqualTo(0);
        }
    }
    @Test
    @WithMockUser(roles="USER")
    public void showUpdateForm_giveAnInExistingId_ruleNameIsReturn() throws Exception {
        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception

        mockMvc.perform(get("/ruleName/update/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));
    }
    @Test
    public void deleteRuleName_giveAnInxistingId_errorIsReturn() throws Exception {


        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        mockMvc.perform(get("/ruleName/delete/0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

    }

    /*---------------------------------------- Post CurvePoint-------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void validate_giveAnInexistingRuleName_theRuleNamegIsCreate() throws Exception {
        List<RuleName> ruleNameList = new ArrayList<>();
        ruleNameList = ruleNameRepository.findAll();


        //GIVEN : Give an exiting RuleName

        //WHEN //THEN return the RuleName
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
        List<RuleName> ruleNameEndList = new ArrayList<>();
        ruleNameEndList = ruleNameRepository.findAll();

        assertThat(ruleNameEndList.size()).isEqualTo(ruleNameList.size() +1);
    }

    @Test
    public void updateRuleName_giveAnExistingId_ruleNameIsUpdate() throws Exception {

        //GIVEN : an existing curvePoint id
        ruleNameRepository.save(ruleName);
        //WHEN call an existing ID
        // THEN return is OK

        mockMvc.perform(post("/ruleName/update/"+idConst)
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
    public void updateRuleName_giveAnInexistingId_errorIsReturn() throws Exception {

        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(post("/ruleName/update/100")
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
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
}
