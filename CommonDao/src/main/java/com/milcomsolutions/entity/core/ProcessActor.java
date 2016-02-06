package com.milcomsolutions.entity.core;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ProcessActor extends BaseModel {

    private static final long serialVersionUID = 6466785961987679104L;

    protected String name;


    public ProcessActor() {
        super();
    }


    public ProcessActor(Long id) {
        setId(id);
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }
}
