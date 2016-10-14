package com.gft.backend.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
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
    @Column(name="user_id")
    private int userId;

    @ElementCollection(targetClass=OrderResult.class)
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Set<OrderResult> getResults() {
        return results;
    }

    public void setResults(Set<OrderResult> results) {
        this.results = results;
    }
}
