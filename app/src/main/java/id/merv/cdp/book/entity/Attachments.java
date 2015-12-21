package id.merv.cdp.book.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by akm on 19/12/15.
 */
public class Attachments {

    @JsonProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
