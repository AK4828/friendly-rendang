package id.merv.cdp.book.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by akm on 16/12/15.
 */
public class Categories {
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
