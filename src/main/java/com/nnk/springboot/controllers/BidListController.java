package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.TradeRepository;
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
public class BidListController {
    private static final Logger logger = LogManager.getLogger(BidListController.class);

    @Autowired
    private BidListRepository bidListRepository;


    @RequestMapping("/bidList/list")
    public String home(Model model)
    {
        logger.info("home start");
        model.addAttribute("bidLists", bidListRepository.findAll());
        logger.info("home finish");
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        logger.info("start /finish");
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return bid list
        logger.info("validate start");
        if (!result.hasErrors()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            bid.setCreationDate(timestamp);
            bid.setRevisionDate(timestamp);
            bidListRepository.save(bid);
            model.addAttribute("bidList", bidListRepository.findAll());
            logger.info("validate finish correctly for bid ID : "+ bid.getBidListId());
            return "redirect:/bidList/list";
        }
        logger.error("validate finish with error for bid : "+ bid.getBidListId());
        return "bidList/add";

    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        logger.info(" start for id " + id);
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        model.addAttribute("bidList", bidList);
        logger.info(" finish");
        return "bidList/update";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid
        logger.info(" start for id " + id);

        if (result.hasErrors()) {
            logger.error("updateTrade finish with error for trade : "+bidList.getBidListId());
            return "bidList/update";
        }
        BidList bidListResult = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid bid Id:" + id));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        bidList.setBidListId(id);
        bidList.setCreationDate(bidListResult.getCreationDate());
        bidList.setRevisionDate(timestamp);
        bidListRepository.save(bidList);

        model.addAttribute("bidLists", bidListRepository.findAll());
        logger.info(" finish");

        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        logger.info(" start for id " + id);
        BidList bidList = bidListRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid BidList Id:" + id));
        bidListRepository.delete(bidList);
        model.addAttribute("bidLists", bidListRepository.findAll());
        logger.info(" finish");

        return "redirect:/bidList/list";
    }
}
