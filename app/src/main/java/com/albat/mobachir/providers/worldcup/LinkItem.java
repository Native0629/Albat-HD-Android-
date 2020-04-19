package com.albat.mobachir.providers.worldcup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LinkItem implements Serializable {
    @Expose
    @SerializedName("Link")
    String link;

    @Expose
    @SerializedName("LinkName")
    String linkName;


    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
}
