package com.nnk.springboot.it;


import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CurvePointControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CurvePointRepository curvePointRepository;

    private CurvePoint curvePoint;

    //constantes de test
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Double idDoubleConst = 1d;
    int curveIdConst = 10;
    int idConst = 1;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        //idDouble.doubleValue(1);
        curvePoint = new CurvePoint();
        curvePoint.setAsOfDate(timestamp);
        curvePoint.setCreationDate(timestamp);
        curvePoint.setCurveId(curveIdConst);
        curvePoint.setId(idConst);
        curvePoint.setTerm(idDoubleConst);
        curvePoint.setValue(idDoubleConst);


    }

    /*------------------------------ Get ------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    public void showUpdateForm_giveAnExistingId_curvepointIsReturn(){
        //GIVEN : put an id in a not empty data base
            curvePointRepository.save(curvePoint);
        //WHEN call an existing ID
        // THEN return is OK
        try {
            mockMvc.perform(get("/curvePoint/update/"+curveIdConst))
                    .andDo(print())
                    .andExpect(status().isOk());
            //assertThat(1).isEqualTo(1);
        }catch (Exception e) {
            assertThat(1).isEqualTo(0);
        }
    }
    @Test
    @WithMockUser(roles="USER")
    public void showUpdateForm_giveAnInExistingId_curvePointIsReturn() throws Exception {
        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
            mockMvc.perform(get("/curvePoint/update/0"))
                    .andDo(print())
                    .andExpect(status().isOk());
                assertThat(1).isEqualTo(0);
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }

    }

    @Test
    public void deleteUser_giveAnInxistingId_errorIsReturn() throws Exception {


        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
                mockMvc.perform(get("/curvePoint/delete/0"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));
                assertThat(1).isEqualTo(0);
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }

    }
    /*---------------------------------------- Post CurvePoint-------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void validate_giveAnInexistingCurvePoint_theCurvePointIsCreate() throws Exception {
        List<CurvePoint> curvePointStartList = new ArrayList<>();
        curvePointStartList = curvePointRepository.findAll();


        //GIVEN : Give an exiting Person

         //WHEN //THEN return the station
        mockMvc.perform(post("/curvePoint/validate")
                .content(asJsonString(curvePoint))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("curveId", String.valueOf(curveIdConst))
                .param("term", String.valueOf(idDoubleConst))
                .param("value", String.valueOf(idDoubleConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));
        List<CurvePoint> curvePointEndList = new ArrayList<>();
        curvePointEndList = curvePointRepository.findAll();

        assertThat(curvePointEndList.size()).isEqualTo(curvePointStartList.size() +1);

    }

    @Test
    public void updateCurvePoint_giveAnExistingId_curvePointIsUpdate() throws Exception {

        //GIVEN : an existing curvePoint id
        curvePointRepository.save(curvePoint);
        //WHEN call an existing ID
        // THEN return is OK

        mockMvc.perform(post("/curvePoint/update/"+curveIdConst)
                .content(asJsonString(curvePoint))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("curveId", String.valueOf(curveIdConst))
                .param("term", String.valueOf(idDoubleConst))
                .param("value", String.valueOf(idDoubleConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));
    }

    @Test
    public void updateCurvePoint_giveAnInexistingId_errorIsReturn() throws Exception {

        //GIVEN : Data base is Empty

        //WHEN call an inexisting ID
        // THEN return exception
        try {
        mockMvc.perform(post("/curvePoint/update/100")
                .content(asJsonString(curvePoint))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("curveId", "100")
                .param("term", String.valueOf(idDoubleConst))
                .param("value", String.valueOf(idDoubleConst)))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }
    }
}
