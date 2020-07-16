package com.nnk.springboot.domain;

import org.springframework.beans.factory.annotation.Required;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
public class BidList {
    // TODO: Map columns in data table BIDLIST with corresponding java fields

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer BidListId;
    @Column(name="account")
    @NotNull
    @NotBlank(message = "{bidlist.account.mandatory}")
    private String account;
    @Column(name="type")
    @NotNull
    @NotBlank(message = "{bidlist.type.mandatory}")
    private String type;
    @Column(name="bidQuantity")
    private Double bidQuantity;
    @Column(name="askQuantity")
    private Double askQuantity;
    @Column(name="bid")
    private Double bid;
    @Column(name="ask")
    private Double ask;
    @Column(name="benchmark")
    private String benchmark;
    @Column(name="bidListDate")
    private Timestamp bidListDate;
    @Column(name="commentary")
    private String commentary;
    @Column(name="security")
    private String security;
    @Column(name="status")
    private String status;
    @Column(name="trader")
    private String trader;
    @Column(name="book")
    private String book;
    @Column(name="creationName")
    private String creationName;
    @Column(name="creationDate")
    private Timestamp creationDate;
    @Column(name="revisionName")
    private String revisionName;
    @Column(name="revisionDate")
    private Timestamp revisionDate;
    @Column(name="dealName")
    private String dealName;
    @Column(name="dealType")
    private String dealType;
    @Column(name="sourceListId")
    private String sourceListId;
    @Column(name="side")
    private String side;


    public BidList() {
    }

    public BidList(String account,String type,Double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;

    }

    public Integer getBidListId() {
        return BidListId;
    }

    public void setBidListId(Integer bidListId) {
        BidListId = bidListId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getBidQuantity() {
        return bidQuantity;
    }

    public void setBidQuantity(Double bidQuantity) {
        this.bidQuantity = bidQuantity;
    }

    public Double getAskQuantity() {
        return askQuantity;
    }

    public void setAskQuantity(Double askQuantity) {
        this.askQuantity = askQuantity;
    }

    public Double getBid() {
        return bid;
    }

    public void setBid(Double bid) {
        this.bid = bid;
    }

    public Double getAsk() {
        return ask;
    }

    public void setAsk(Double ask) {
        this.ask = ask;
    }

    public String getBenchmark() {
        return benchmark;
    }

    public void setBenchmark(String benchmark) {
        this.benchmark = benchmark;
    }

    public Timestamp getBidListDate() {
        //return bidListDate;
        Timestamp localTimestamp = bidListDate;
        return localTimestamp;
    }

    public void setBidListDate(Timestamp bidListDate) {
        //this.bidListDate = bidListDate;
        if (bidListDate == null) {
            this.bidListDate = null;
        }
        else {
            this.bidListDate = new Timestamp(bidListDate.getTime());
        }
    }

    public String getCommentary() {
        return commentary;
    }

    public void setCommentary(String commentary) {
        this.commentary = commentary;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTrader() {
        return trader;
    }

    public void setTrader(String trader) {
        this.trader = trader;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getCreationName() {
        return creationName;
    }

    public void setCreationName(String creationName) {
        this.creationName = creationName;
    }

    public Timestamp getCreationDate() {
        //return creationDate;
        Timestamp localTimestamp = creationDate;
        return localTimestamp;
    }

    public void setCreationDate(Timestamp creationDate) {
        //this.creationDate = creationDate;
        if (creationDate == null) {
            this.creationDate = null;
        }
        else {
            this.creationDate = new Timestamp(creationDate.getTime());
        }

    }

    public String getRevisionName() {
        return revisionName;
    }

    public void setRevisionName(String revisionName) {
        this.revisionName = revisionName;
    }

    public Timestamp getRevisionDate() {
        //return revisionDate;
        Timestamp localTimestamp = revisionDate;
        return localTimestamp;
    }

    public void setRevisionDate(Timestamp revisionDate) {
        //this.revisionDate = revisionDate;
        if (revisionDate == null) {
            this.revisionDate = null;
        }
        else {
            this.revisionDate = new Timestamp(revisionDate.getTime());
        }
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDealType() {
        return dealType;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getSourceListId() {
        return sourceListId;
    }

    public void setSourceListId(String sourceListId) {
        this.sourceListId = sourceListId;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

}
