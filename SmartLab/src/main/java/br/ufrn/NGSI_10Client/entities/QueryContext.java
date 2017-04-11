package br.ufrn.NGSI_10Client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 4/8/2016.
 */
public class QueryContext {

    @SerializedName("entities")
    private List<Entity> entities = new ArrayList<Entity>();

    @SerializedName("attributes")
    private List<String> attributes = new ArrayList<String>();

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public List<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<String> attributes) {
        this.attributes = attributes;
    }

    public void addAttributes(String attribute){
        attributes.add(attribute);
    }

    public void addEntity(Entity entity){
        entities.add(entity);
    }
}
