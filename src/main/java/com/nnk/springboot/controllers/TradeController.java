package com.nnk.springboot.controllers;


import com.nnk.springboot.domain.Trade;
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
public class TradeController {

    private static final Logger logger = LogManager.getLogger(TradeController.class);

    @Autowired
    private TradeRepository tradeRepository;

    /**
     * Return the list of trade
     * @param model trade list
     * @return new page to display
     */
    @RequestMapping("/trade/list")
    public String home(Model model)
    {

        logger.info("home start");
        model.addAttribute("trades", tradeRepository.findAll());
        logger.info("home finish");
        return "trade/list";
    }

    /**
     * Display the add trade page
     * @param bid trade to add
     * @return new page to display
     */
    @GetMapping("/trade/add")
    public String addUser(Trade bid) {

        logger.info("start /finish");
        return "trade/add";
    }

    /**
     * create a new trade
     * @param trade trade to add
     * @param result input information
     * @param model trade list
     * @return new page to display
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {

        logger.info("validate start");
        if (!result.hasErrors()) {
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            trade.setCreationDate(timestamp);
            trade.setRevisionDate(timestamp);
            tradeRepository.save(trade);
            model.addAttribute("trades", tradeRepository.findAll());
            logger.info("validate finish correctly for trade ID : "+ trade.getTradeId());
            return "redirect:/trade/list";
        }
        logger.error("validate finish with error for Trade : "+ trade.getTradeId());
        return "trade/add";

    }

    /**
     * Display the update trade page
     * @param id trade id to update
     * @param model trade list
     * @return new page to display
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        logger.info("showUpdateForm start for id " + id);
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        model.addAttribute("trade", trade);
        logger.info("showUpdateForm finish");
        return "trade/update";
    }

    /**
     *  Update trade
     * @param id tradeid to update
     * @param trade new trade datas
     * @param result input information
     * @param model trade list
     * @return new page to display
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
         logger.info(" start for id " + id);

        if (result.hasErrors()) {
            logger.error("updateTrade finish with error for trade : "+trade.getTradeId());
            return "trade/update";
        }
        Trade tradeResult = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        trade.setTradeId(id);
        trade.setCreationDate(tradeResult.getCreationDate());
        trade.setRevisionDate(timestamp);
        tradeRepository.save(trade);

        model.addAttribute("trades", tradeRepository.findAll());
        logger.info(" finish");

        return "redirect:/trade/list";

    }

    /**
     * Delete trade page
     * @param id tradeid to delete
     * @param model trade list
     * @return new page to display
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        logger.info(" start for id " + id);
        Trade trade = tradeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid trade Id:" + id));
        tradeRepository.delete(trade);
        model.addAttribute("trades", tradeRepository.findAll());
        logger.info(" finish");

        return "redirect:/trade/list";
    }
}
