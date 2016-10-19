package com.gft.backend.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * Created by miav on 2016-09-30.
 */
@Entity
@Table(name = "ebaycategory")
public class EBayCategory {
    @Id
    @Column(name="id")
    private String id;
    @Column(name="level")
    @NotNull
    private String level;
    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="parent_id")
    private String parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
