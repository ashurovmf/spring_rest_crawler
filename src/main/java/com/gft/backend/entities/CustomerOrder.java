package com.gft.backend.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by miav on 2016-09-23.
 */
@Entity
@Table(name="customerorder")
public class CustomerOrder implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private int id;
    @Column(name="location")
    private String location;
    @NotNull
    @Column(name="name")
    private String name;
    @NotNull
    @Column(name="key")
    private String keySearchString;
    @NotNull
    @Column(name="category_id")
    private String categoryId;
    @NotNull
    @Column(name="status")
    private String status;
    @NotNull
    @Column(name="username")
    private String userName;

    @JsonManagedReference
    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER)
    private Set<OrderResult> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeySearchString() {
        return keySearchString;
    }

    public void setKeySearchString(String keySearchString) {
        this.keySearchString = keySearchString;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() { return userName; }

    public void setUserName(String userName) { this.userName = userName; }

    public Set<OrderResult> getResults() {
        return results;
    }

    public void setResults(Set<OrderResult> results) {
        this.results = results;
    }
}
