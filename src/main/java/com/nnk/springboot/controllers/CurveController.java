package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
import java.sql.Timestamp;


@Controller
public class CurveController {
    private static final Logger logger = LogManager.getLogger(CurveController.class);

    @Autowired
    private CurvePointRepository curvePointRepository;

    /**
     * Return the list of CrrvePoint
     * @param model CurvePoint list
     * @return new page to display
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        logger.info("home start");
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("home finish");
        return "curvePoint/list";
    }

    /**
     * Display the add CurvePoint page
     * @param curvePoint Curvepoint to add
     * @return new page to display
     */
    @GetMapping("/curvePoint/add")
    public String addCurvePointForm(CurvePoint curvePoint) {

        logger.info("addCurvePointForm start/finish");
        return "curvePoint/add";
    }

    /**
     * create a new Curvepoint
     * @param curvePoint Curvepoint to add
     * @param result input information
     * @param model CurvePoint list
     * @return new page to display
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
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

    /**
     * Display the update Curvepoint page
     * @param id CurvePoint ID to update
     * @param model CurvePoint list
     * @return new page to display
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        logger.info("showUpdateForm start for id " + id);
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curvePoint Id:" + id));
        model.addAttribute("curvePoint", curvePoint);
        logger.info("showUpdateForm finish");
        return "curvePoint/update";
    }

    /**
     * Update Curvepoint
     * @param id Curvepoint ID to updtate
     * @param curvePoint new Curvepoint datas
     * @param result input information
     * @param model CurvePoint list
     * @return new page to display
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        logger.info("updateCurvePoint start for id " + id);

        if (result.hasErrors()) {
            logger.error("updateCurvePoint finish with error for curvePoint : "+curvePoint.getCurveId());
            return "curvePoint/update";
        }

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        curvePoint.setCreationDate(timestamp);
        curvePointRepository.save(curvePoint);

        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("updateCurvePoint finish");

        return "redirect:/curvePoint/list";
    }

    /**
     * Delete Curvepoint
     * @param id CurvePoint ID to delete
     * @param model CurvePoint list
     * @return new page to display
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurvePoint(@PathVariable("id") Integer id, Model model) {

        logger.info("deleteCurvePoint start for id " + id);
        CurvePoint curvePoint = curvePointRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid curvePoint Id:" + id));
        curvePointRepository.delete(curvePoint);
        model.addAttribute("curvePoints", curvePointRepository.findAll());
        logger.info("deleteCurvePoint finish");
        return "redirect:/curvePoint/list";
    }
}
