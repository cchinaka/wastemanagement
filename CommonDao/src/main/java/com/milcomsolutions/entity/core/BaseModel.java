package com.milcomsolutions.entity.core;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@MappedSuperclass
public abstract class BaseModel implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    protected Long id;

    private boolean active = true;

    private String description;

    private Date creationDate = new Date();

    public static final int BASE_HASH_SIZE = 997;

    private String dataloadCode;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    @Column(nullable = true)
    public boolean isActive() {
        return active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public String toString() {
        return String.format("%s[id=%s]", this.getClass().getSimpleName(), getId());
    }


    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreationDate() {
        return creationDate;
    }


    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public int hashCode() {
        if (id == null) {
            return 0;
        }
        int result = 17;
        int calculation = (int) (id ^ (id >>> 32));
        return result = 37 * result + calculation;
    }


    protected BigDecimal setBig(BigDecimal thisNumber, BigDecimal thatNumber) {
        if (thatNumber != null) {
            thisNumber = thatNumber.setScale(2, RoundingMode.CEILING);
        } else {
            thisNumber = thatNumber;
        }
        System.out.println("this amount - " + thisNumber);
        return thisNumber;
    }


    protected BigDecimal getBig(BigDecimal thisNumber) {
        if (thisNumber != null) {
            return thisNumber.setScale(2, RoundingMode.CEILING);
        }
        return null;
    }


    public String getDataloadCode() {
        return dataloadCode;
    }


    public void setDataloadCode(String dataloadCode) {
        this.dataloadCode = dataloadCode;
    }
}
