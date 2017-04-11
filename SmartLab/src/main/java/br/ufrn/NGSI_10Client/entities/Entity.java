package br.ufrn.NGSI_10Client.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jonas on 4/8/2016.
 */
public class Entity {

    @SerializedName("type")
    private String type;

    @SerializedName("id")
    private String id;

    @SerializedName("isPattern")
    private boolean isPattern;

    public Entity(String type, String id, boolean isPattern) {
        this.type = type;
        this.id = id;
        this.isPattern = isPattern;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isPattern() {
        return isPattern;
    }

    public void setIsPattern(boolean isPattern) {
        this.isPattern = isPattern;
    }
}
