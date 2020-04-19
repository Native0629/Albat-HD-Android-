package com.albat.mobachir.providers.worldcup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TeamItem implements Serializable {
    @Expose
    @SerializedName("Id")
    int id;

    @Expose
    @SerializedName("Name")
    String name;

    @Expose
    @SerializedName("Code")
    String code;

    @Expose
    @SerializedName("Flag")
    String flag;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
