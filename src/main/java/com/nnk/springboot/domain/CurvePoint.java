package com.nnk.springboot.domain;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.util.Date;


@Entity
@Table(name = "curvepoint")
public class CurvePoint {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    @Column(name="CurveId")
    @NotNull(message = "{curvepoint.curveid.mandatory}")
    //@Pattern(regexp = "[0-9]+",message="{curvepoint.curveid.numeric}")
    private Integer curveId;
    @Column(name="asOfDate")
    private Timestamp asOfDate;
    @NotNull(message = "{curvepoint.term.mandatory}")
    @Column(name="term")
    private Double term;
    @NotNull(message = "{curvepoint.value.mandatory}")
    @Column(name="value")
    private Double value;
    @Column(name="creationDate")
    private Timestamp creationDate;

    public CurvePoint() {
    }
    public CurvePoint(Integer curveId,Double term,Double value) {
        this.curveId = curveId;
        this.term = term;
        this.value = value;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getCurveId() {
        return curveId;
    }

    public void setCurveId(Integer curveId) {
        this.curveId = curveId;
    }

    public Timestamp getAsOfDate() {
        Timestamp localTimestamp = asOfDate;
        return localTimestamp;
        //return asOfDate;
    }

    public void setAsOfDate(Timestamp asOfDate) {
        //this.asOfDate = asOfDate;

        if (asOfDate == null) {
            this.asOfDate = null;
        }
        else {
            this.asOfDate = new Timestamp(asOfDate.getTime());
        }
    }

    public Double getTerm() {
        return term;

    }

    public void setTerm(Double term) {
        this.term = term;
    }

    /*@Pattern(regexp="(?=.*d)", message="Numeric Field only")*/
    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
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
}
