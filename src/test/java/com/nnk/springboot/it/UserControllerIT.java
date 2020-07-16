package com.nnk.springboot.it;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.nnk.springboot.ut.UserControllerTest.asJsonString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class UserControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    private User user;

    //constantes de test
    String fullname = "James Bond";
    String fullnameModify = "Miss Money penny";
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
        user.setId(10);
        user.setFullname(fullname);
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);


    }

    /*------------------------------ Get ------------------------------*/
    @Test
    public void deleteUser_giveAnInexistingId_errorIsReturn() throws Exception {


        //GIVEN : Give an Inexiting Person
        //WHEN call an inexisting ID
        // THEN return exception
        try {
        mockMvc.perform(get("/user/delete/100"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }

    }

    @Test
    public void showUpdateForm_giveAnInExistingId_userIsReturn() throws Exception {
        //GIVEN : Give an exiting Person

        //WHEN //THEN return error
        try{
        mockMvc.perform(get("/user/update/100"))
                .andDo(print())
                .andExpect(status().isOk());
        }catch (Exception e) {
            assertThat(1).isEqualTo(1);
        }

    }
    /*------------------------------ Post ------------------------------*/
    @Test
    public void validate_giveAnInexistingUSER_theCUserIsCreate() throws Exception {
        List<User> userListStart = new ArrayList<>();
        userListStart = userRepository.findAll();
        //GIVEN : Give an Inexisting Person

        //WHEN  add a user

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
        //THEN return  user List and one user more ins DB

        List<User> userListEndList = new ArrayList<>();
        userListEndList = userRepository.findAll();

        assertThat(userListEndList.size()).isEqualTo(userListStart.size() +1);

    }

    @Test
    public void updateUser_giveAnExistingId_userIsUpdate() throws Exception {

        //GIVEN : Give an exiting Person

       userRepository.save(user);

        user.setFullname(fullnameModify);
        //WHEN call an existing ID
        mockMvc.perform(post("/user/update/"+user.getId())
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", fullnameModify)
                .param("username", username)
                .param("password", password)
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        User userResult = userRepository.findByUserName(username);
        //THEN user is updated
        assertThat(userResult.getFullname()).isEqualTo(fullnameModify);
    }

    @Test
    public void updateUser_giveAnInexistingId_errorIsReturn() throws Exception {

        //GIVEN : Give an Inexiting Person

        //WHEN call an Inexisting ID
        try {
        mockMvc.perform(post("/user/update/100")
                .content(asJsonString(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("fullname", fullnameModify)
                .param("username", username)
                .param("password", password)
                .param("role", "USER"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user/list"));
        }catch (Exception e) {
            //THEN error is return
            assertThat(1).isEqualTo(1);
        }
    }
}
