package com.nnk.springboot.controllers;

import com.nnk.springboot.config.SecurityConfig;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SecurityConfig securityConfig;

    /**
     * Return the list of users
     * @param model users list
     * @return new page to display
     */
    @RequestMapping("/user/list")
    public String home(Model model)
    {
        logger.info("home start");
        model.addAttribute("users", userRepository.findAll());
        logger.info("home finish");
        return "user/list";
    }

    /**
     * Display the add user page
     * @param bid user to add
     * @return new page to display
     */
    @GetMapping("/user/add")
    public String addUser(User bid) {
        logger.info("addUser start/finish");
        return "user/add";
    }

    /**
     * create a new user
     * @param user user to add
     * @param result input information
     * @param model user list
     * @return newpage to display
     */
    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model)  {
        logger.info("validate start");

        if (!result.hasErrors()) {
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    user.setPassword(encoder.encode(user.getPassword()));
                    userRepository.save(user);
                    model.addAttribute("users", userRepository.findAll());
                    logger.info("validate finish correctly for user : "+user.getUsername());
                    return "redirect:/user/list";
         }
        logger.error("validate finish with error for user : "+user.getUsername());
        return "user/add";
    }

    /**
     * Display the update user page
     * @param id userid to update
     * @param model information user to update
     * @return newpage to display
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.info("showUpdateForm start for id " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setPassword("");
        model.addAttribute("user", user);
        logger.info("showUpdateForm finish");
        return "user/update";
    }

    /**
     * Update a user
     * @param id userid to update
     * @param user new user datas
     * @param result input information
     * @param model user list
     * @return new page to display
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) {
        logger.info("updateUser start for id " + id);
        if (result.hasErrors()) {
            logger.error("updateUser finish with error for user : "+user.getUsername());
            return "user/update";
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        logger.info("updateUser finish");
        return "redirect:/user/list";
    }

    /**
     * Delete user
     * @param id userid to delete
     * @param model new list of user
     * @return new page to display
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        logger.info("deleteUser start for id " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
        logger.info("delete User finish");
        return "redirect:/user/list";
    }
}
