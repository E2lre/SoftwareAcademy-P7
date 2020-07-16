package com.nnk.springboot.controllers;


import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class RuleNameController {
    private static final Logger logger = LogManager.getLogger(RuleNameController.class);

    @Autowired
    private RuleNameRepository ruleNameRepository;

    /**
     * Return the list of RuleName
     * @param model RuleName List
     * @return  new page to display
     */
    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        logger.info("home start");
        model.addAttribute("ruleName", ruleNameRepository.findAll());
        logger.info("home finish");
        return "ruleName/list";
    }

    /**
     * Display the add RuleName page
     * @param bid RuleName to add
     * @return new page to display
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        logger.info("Start / finish");
        return "ruleName/add";
    }

    /**
     * create a new RuleName
     * @param ruleName RuleName to add
     * @param result input information
     * @param model RuleName List
     * @return new page to display
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        logger.info("validate start");
        if (!result.hasErrors()) {
            ruleNameRepository.save(ruleName);
            model.addAttribute("ruleName", ruleNameRepository.findAll());
            logger.info("validate finish correctly for Name : "+ ruleName.getName());
            return "redirect:/ruleName/list";
        }
        logger.error("validate finish with error for Name : "+ ruleName.getName());
        return "ruleName/add";
    }

    /**
     * Display the update RuleName page
     * @param id ruleNameId to update
     * @param model RuleName List
     * @return new page to display
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.info("showUpdateForm start for id " + id);
        try {
            RuleName ruleName = ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));
            model.addAttribute("ruleName", ruleName);
            logger.info("showUpdateForm finish");
            return "ruleName/update";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }

    /**
     * Update RuleName
     * @param id RuleNameId to update
     * @param ruleName new RueleName datas
     * @param result input information
     * @param model RuleName List
     * @return new page to display
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {

        logger.info("updateRating start for id " + id);

        if (result.hasErrors()) {
            logger.error("updateRating finish with error for curvePoint : "+ruleName.getName());
            return "ruleName/update";
        }

        ruleNameRepository.save(ruleName);

        model.addAttribute("ruleNames", ruleNameRepository.findAll());
        logger.info("updateRuleName finish");

        return "redirect:/ruleName/list";
    }

    /**
     * Delete RuleName page
     * @param id RuleNameid to delete
     * @param model RuleName List after delete
     * @return new page to display
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {

        logger.info("deleteRating start for id " + id);
        try {
            RuleName ruleName = ruleNameRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ruleName Id:" + id));
            ruleNameRepository.delete(ruleName);
            model.addAttribute("ruleNames", ruleNameRepository.findAll());
            logger.info("deleteRuleName finish");
            return "redirect:/ruleName/list";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
