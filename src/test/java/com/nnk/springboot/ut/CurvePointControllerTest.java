package com.nnk.springboot.ut;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class CurvePointControllerTest {
    private static final Logger logger = LogManager.getLogger(CurvePointControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointRepository curvePointRepository;

    private CurvePoint curvePoint;

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Double idDouble = 1d;
            //idDouble.doubleValue(1);
            curvePoint = new CurvePoint();
            curvePoint.setAsOfDate(timestamp);
            curvePoint.setCreationDate(timestamp);
            curvePoint.setCurveId(1);
            curvePoint.setId(1);
            curvePoint.setTerm(idDouble);
            curvePoint.setValue(idDouble);


    }
    /*---------------------------------------- GET CurvePoint-------------------------------*/
    @Test
    //@WithMockUser(roles="USER")
    @WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void home_sendModel_curvepointListIsReturn() throws Exception {
        List<CurvePoint> curvePointList = new ArrayList<>();

        //GIVEN : Give an exiting Person
        curvePointList.add(curvePoint);
        Mockito.when(curvePointRepository.findAll()).thenReturn(curvePointList);
        //WHEN //THEN return the station
        //mockMvc.perform(get("/curvePoint/list").with(csrf()))
        mockMvc.perform(get("/curvePoint/list"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    /*---------------------------------------- Post CurvePoint-------------------------------*/
    @Test
    //@WithMockUser(roles="USER")
    //@WithMockUser(username = "admin", authorities = { "ADMIN", "USER" })
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)

    public void validate_giveAnInexistingCurvePoint_theCurvePointIsCreate() throws Exception {


        //GIVEN : Give an exiting Person

        //Mockito.when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        Mockito.when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        //WHEN //THEN return the station
        mockMvc.perform(post("/curvePoint/validate"))
                .andDo(print())
                .andExpect(status().isOk());
                //.andExpect(MockMvcResultMatchers.redirectedUrl("/curvePoint/list"));
                //.andExpect(status().is3xxRedirection());
                //.andExpect(content().string(containsString("redirect:/curvePoint/list")));
                //.andExpect(forwardedUrl("redirect:/curvePoint/list"));
        //TODO ICICICICIC pour test erreur
//        .andExpect(MockMvcResultMatchers.redirectedUrl("/user/list"))
//                .andExpect(status().is3xxRedirection());

    }

}
