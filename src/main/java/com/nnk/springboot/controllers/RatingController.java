package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.CurvePointRepository;
import com.nnk.springboot.repositories.RatingRepository;
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
public class RatingController {
    private static final Logger logger = LogManager.getLogger(RatingController.class);

    @Autowired
    private RatingRepository ratingRepository;


    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        logger.info("home start");
        model.addAttribute("rating", ratingRepository.findAll());
        logger.info("home finish");
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        logger.info("Start / finish");
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {

        logger.info("validate start");
        if (!result.hasErrors()) {
            ratingRepository.save(rating);
            model.addAttribute("rating", ratingRepository.findAll());
            logger.info("validate finish correctly for Order : "+ rating.getOrderNumber());
            return "redirect:/rating/list";
        }
        logger.error("validate finish with error for rating : "+ rating.getOrderNumber());
        return "rating/add";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {

        logger.info("showUpdateForm start for id " + id);
        try {
            Rating rating = ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
            model.addAttribute("rating", rating);
            logger.info("showUpdateForm finish");
            return "rating/update";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {

        logger.info("updateRating start for id " + id);

        if (result.hasErrors()) {
            logger.error("updateRating finish with error for curvePoint : "+rating.getOrderNumber());
            return "rating/update";
        }

        ratingRepository.save(rating);

        model.addAttribute("ratings", ratingRepository.findAll());
        logger.info("updateRating finish");

        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        logger.info("deleteRating start for id " + id);
        try {
            Rating rating = ratingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid rating Id:" + id));
            ratingRepository.delete(rating);
            model.addAttribute("ratings", ratingRepository.findAll());
            logger.info("deleteRating finish");
            return "redirect:/rating/list";
        } catch (Exception e) {
            logger.error(e.getMessage());
            model.addAttribute("errorMsg", e.getMessage());
            return "error";
        }
    }
}
