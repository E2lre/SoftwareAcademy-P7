package com.nnk.springboot.ut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

//import static com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken.Optional;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
//@AutoConfigureMockMvc
@AutoConfigureMockMvc(addFilters = false)
@WebAppConfiguration
public class UserControllerTest {

    private static final Logger logger = LogManager.getLogger(UserControllerTest.class);
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    private User user;

    //constantes de test
    String fullname = "James Bond";
    String username = "007";
    String password = "MI6";
    String role = "USER";
    String encryptPasswordConst = "$2a$12$scj6PvgZYRLahntmwOmm/.PnXJjHYK2SpsgsWb6fFbZBr5nWpbmJ6";

    @BeforeEach
    public void setUpEach() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Double idDouble = 1d;
        //idDouble.doubleValue(1);
        user = new User();
        user.setId(1);
        user.setFullname(fullname);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);


    }
    /*------------------------------ Get ------------------------------*/
    @Test
    @WithMockUser(roles="USER")
    //@DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void home_sendModel_userListIsReturn() throws Exception {
        List<User> userList = new ArrayList<>();

        //GIVEN : Give an exiting Person
        userList.add(user);

        //GIVEN : Give an exiting Person
        Mockito.when(userRepository.findAll()).thenReturn(userList);
        //WHEN //THEN return the station
        mockMvc.perform(get("/user/list"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void showUpdateForm_giveAnExistingId_userIsReturn() throws Exception {


        //GIVEN : Give an exiting Person


        //GIVEN : Give an exiting Person
        Mockito.when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(user));
        //WHEN //THEN return the station
        mockMvc.perform(get("/user/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void showUpdateForm_giveAnInExistingId_userIsReturn() throws Exception {


        //GIVEN : Give an exiting Person


        //GIVEN : Give an exiting Person
        Mockito.when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(user));
        //WHEN //THEN return the station
        mockMvc.perform(get("/user/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
 /*   @Test
    public void deleteUser_giveAnExistingId_userIsDelete() throws Exception {


        //GIVEN : Give an exiting Person


        //GIVEN : Give an exiting Person
        Mockito.when(userRepository.findById(anyInt())).thenThrow(NullPointerException.class);
        doNothing().when(userRepository).delete(any(User.class));

        //WHEN //THEN return the station
        mockMvc.perform(get("/user/delete/1"))
                .andDo(print())
                .andExpect(status().isFound());

    }*/
/*------------------------------ Post ------------------------------*/
    @Test
    //TODO A CREUSER : https://blog.codeleak.pl/2014/08/spring-mvc-test-assert-given-model-attribute-global-errors.html

    //TODO ICICICICIC

 //   @Import(RequestValidator.class) //https://stackoverflow.com/questions/52001043/how-to-mock-bindingresult-in-spring-boot-test
    public void validate_giveAnInexistingUSER_theCUserIsCreate() throws Exception {


        //GIVEN : Give an exiting Person
//        BindingResult result = new BindingResult();
//        Model model = new Model();
        //Mockito.when(curvePointRepository.save(curvePoint)).thenReturn(curvePoint);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        //WHEN //THEN return the station
/*        mockMvc.perform(post("/user/validate"))
                .andDo(print())
                .andExpect(status().isOk());*/
        mockMvc.perform(post("/user/validate")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", "moi")
                .param("username", "moi")
                .param("password", "Abcdefg0$")
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
//https://stackoverflow.com/questions/55535796/how-to-write-a-test-for-a-controller-which-includes-errors-bindingresult

        //              .andExpect(model().hasErrors())
        //             .andExpect(model().attribute("success", true)) //https://stackoverflow.com/questions/41016228/spring-validator-does-not-trigger-in-integration-test
        //              .andExpect(model().attributeHasFieldErrors(FORM_MODEL_NAME, "field"))
        //              .andExpect(model().attributeHasFieldErrorCode(FORM_MODEL_NAME, "anotherfield", "error") //https://stackoverflow.com/questions/55535796/how-to-write-a-test-for-a-controller-which-includes-errors-bindingresult
        //.andExpect(status().isOk())
        //.andExpect(view().name("user/add"))


        //TODO Regarder cela pour déclencher une erreur  : https://www.baeldung.com/spring-mvc-custom-validator au 9.4
    }

    @Test
   public void updateUser_giveAnExistingId_userIsUpdate() throws Exception {


        //GIVEN : Give an exiting Person


        //GIVEN : Give an exiting Person
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(encryptPasswordConst);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        //WHEN //THEN return the station
        mockMvc.perform(post("/user/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

//                     .content(asJsonString(user))
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON))

    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
