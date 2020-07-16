package com.nnk.springboot.ut;

import com.fasterxml.jackson.databind.ObjectMapper;

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
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
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
    String password = "Abcdefg0$";
    String incorrectPassword = "Abc";
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
    public void addUser_getAddUser_redirectIsReturn() throws Exception {
        //GIVEN : Give an exiting Person

        //WHEN //THEN return the station

        mockMvc.perform(get("/user/add")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
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
    public void showUpdateForm_giveAnInexistingId_userIsReturn() throws Exception {

        //GIVEN : Give an exiting Person

        //GIVEN : Give an exiting Person
        Mockito.when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(user));
        //WHEN //THEN return the station
        mockMvc.perform(get("/user/update/1"))
                .andDo(print())
                .andExpect(status().isOk());

    }
   @Test
    public void deleteUser_giveAnExistingId_userIsDelete() throws Exception {

        //GIVEN : Give an exiting Person

        //GIVEN : Give an exiting Person
 //       Mockito.when(userRepository.findById(anyInt())).thenThrow(NullPointerException.class);
       Mockito.when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.ofNullable(user));
       doNothing().when(userRepository).delete(any(User.class));

        //WHEN //THEN return the station
        mockMvc.perform(get("/user/delete/1"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

    }
/*------------------------------ Post ------------------------------*/
    @Test
    public void validate_giveAnInexistingUSER_theCUserIsCreate() throws Exception {


        //GIVEN : Give an Inexisting Person
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        //WHEN //THEN return user List
        mockMvc.perform(post("/user/validate")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", fullname)
                .param("username", username)
                .param("password", password)
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));

    }

    @Test
    public void validate_giveAnIncorrectFormatPasswaord_errorIsReturn() throws Exception {


        //GIVEN : Give an exiting Person
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        //WHEN //THEN stay on page whith error for invalid password

        mockMvc.perform(post("/user/validate")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", fullname)
                .param("username", username)
                .param("password", incorrectPassword)
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/add"));
    }


    @Test
   public void updateUser_giveAnExistingId_userIsUpdate() throws Exception {

        //GIVEN : Give an exiting Person
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(encryptPasswordConst);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        //WHEN //THEN return the list updated of person
        mockMvc.perform(post("/user/update/1")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", fullname)
                .param("username", username)
                .param("password", password)
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
    }
    @Test
    public void updateUser_giveAnIncorrectPassword_errorIsReturn() throws Exception {

        //GIVEN : Give an exiting Person
        Mockito.when(passwordEncoder.encode(anyString())).thenReturn(encryptPasswordConst);
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        //WHEN //THEN return stay on page with error
        mockMvc.perform(post("/user/update/1")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", fullname)
                .param("username", username)
                .param("password", incorrectPassword)
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("user/update"));
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
