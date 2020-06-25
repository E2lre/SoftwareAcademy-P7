package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.CurvePointRepository;
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
import java.sql.Timestamp;
import java.util.Date;

@Controller
public class CurveController {
    private static final Logger logger = LogManager.getLogger(CurveController.class);
    // TODO: Inject Curve Point service
    @Autowired
    private CurvePointRepository curvePointRepository;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        logger.info("home start");
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("home finish");
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {

        logger.info("addCurvePointForm start/finish");
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Curve list
        logger.info("validate start");
        if (!result.hasErrors()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            curvePoint.setAsOfDate(timestamp);
            curvePoint.setCreationDate(timestamp);
            curvePointRepository.save(curvePoint);
            model.addAttribute("curvePoints", curvePointRepository.findAll());
            logger.info("validate finish correctly for curve ID : "+ curvePoint.getCurveId());
            return "redirect:/curvePoint/list";
        }
        logger.error("validate finish with error for curvePoint : "+ curvePoint.getCurveId());
        return "curvePoint/add";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get CurvePoint by Id and to model then show to the form

        logger.info("showUpdateForm start for id " + id);
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curvePoint Id:" + id));
        model.addAttribute("curvePoint", curvePoint);
        logger.info("showUpdateForm finish");
        return "curvePoint/update";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Curve and return Curve list
        logger.info("updateCurvePoint start for id " + id);

        if (result.hasErrors()) {
            logger.error("updateCurvePoint finish with error for curvePoint : "+curvePoint.getCurveId());
            return "curvePoint/update";
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        curvePoint.setAsOfDate(timestamp);
        curvePoint.setCreationDate(timestamp);
        curvePointRepository.save(curvePoint);

        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("updateCurvePoint finish");

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Curve by Id and delete the Curve, return to Curve list
        logger.info("deleteCurvePoint start for id " + id);
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curvePoint Id:" + id));
        curvePointRepository.delete(curvePoint);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("deleteCurvePoint finish");
        return "redirect:/curvePoint/list";
    }
}
