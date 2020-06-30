package com.nnk.springboot.ut;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.CurvePointRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.nnk.springboot.ut.UserControllerTest.asJsonString;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@AutoConfigureTestDatabase
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class CurvePointControllerTest {
    private static final Logger logger = LogManager.getLogger(CurvePointControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointRepository curvePointRepository;

    private CurvePoint curvePoint;

    //constantes de test
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    Double idDoubleConst = 1d;
    int curveIdConst = 1;
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
    /*---------------------------------------- GET CurvePoint-------------------------------*/
    @Test
    //@WithMockUser(roles="USER")
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void home_sendModel_curvepointListIsReturn() throws Exception {
        List<CurvePoint> curvePointList = new ArrayList<>();

        //GIVEN : Give an exiting curvePoint
        curvePointList.add(curvePoint);
        Mockito.when(curvePointRepository.findAll()).thenReturn(curvePointList);
        //WHEN //THEN return the curvePoint List
        mockMvc.perform(get("/curvePoint/list"))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void addCurvePoint_getAddCurvePoint_redirectIsReturn() throws Exception {


        //GIVEN : Give an exiting curvePoint

        //WHEN //THEN return the curvepoint

        mockMvc.perform(get("/curvePoint/add")
                .content(asJsonString(curvePoint))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }
//TODO ICICCI AJOUTER LES TEST DES GET
    @Test
    public void showUpdateForm_giveAnExistingId_curvePointIsReturn() throws Exception {

        //GIVEN : Give an exiting CurvePoint
        Mockito.when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(curvePoint));
        //WHEN //THEN return the CurvePoint
        mockMvc.perform(get("/curvePoint/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
    //TODO Faut il laisser ce test ? il doit être couvert par la partie integ à vérifier
    @Test
    public void showUpdateForm_giveAnInExistingId_curvePontIsReturn() throws Exception {


        //GIVEN : Give an exiting Person


        //GIVEN : Give an exiting Person
        Mockito.when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(curvePoint));
        //WHEN //THEN return the station
        mockMvc.perform(get("/curvePoint/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void deleteUser_giveAnExistingId_curvePointIsDelete() throws Exception {

        //GIVEN : Give an exiting curvePoint
        //       Mockito.when(userRepository.findById(anyInt())).thenThrow(NullPointerException.class);
        Mockito.when(curvePointRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(curvePoint));
        doNothing().when(curvePointRepository).delete(any(CurvePoint.class));

        //WHEN //THEN return list
        mockMvc.perform(get("/curvePoint/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/curvePoint/list"));

    }
    /*---------------------------------------- Post CurvePoint-------------------------------*/
    @Test
    //@WithMockUser(roles="USER")
    //@WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)

    public void validate_giveAnInexistingCurvePoint_theCurvePointIsCreate() throws Exception {


        //GIVEN : Give an exiting CurvePoint

        //Mockito.when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        Mockito.when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        //WHEN //THEN return the list
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


    }
    /*---------------------------------------- Post CurvePoint-------------------------------*/
    @Test
    //@WithMockUser(roles="USER")
    //@WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)

    public void validate_giveAnIncorrectCurveIdFormat_errorIsReturn() throws Exception {


        //GIVEN : Give an exiting CurvePoint


        Mockito.when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        //WHEN //THEN return the add page
        mockMvc.perform(post("/curvePoint/validate")
                .content(asJsonString(curvePoint))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("curveId", "abc")
                .param("term", String.valueOf(idDoubleConst))
                .param("value", String.valueOf(idDoubleConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"));
    }

    //TODO ICICCI AJOUTER LES TEST DES POST
    @Test
    public void updateCurvePoint_giveAnExistingId_curvePointIsUpdate() throws Exception {

        //GIVEN : Give an exiting CurvePoint

        Mockito.when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        //WHEN //THEN return the list
        mockMvc.perform(post("/curvePoint/update/1")
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
    public void updateCurvePoint_giveAnIncorrectId_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Curvepoint

        Mockito.when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        //WHEN //THEN return the update page
        mockMvc.perform(post("/curvePoint/update/1")
                .content(asJsonString(curvePoint))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("curveId", "abc")
                .param("term", String.valueOf(idDoubleConst))
                .param("value", String.valueOf(idDoubleConst)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"));
    }

}
