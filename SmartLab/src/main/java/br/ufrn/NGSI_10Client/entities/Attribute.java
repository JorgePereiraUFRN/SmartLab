package br.ufrn.NGSI_10Client.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Attribute {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    @SerializedName("metadatas")
    private List<Metadata> metadatas = new ArrayList<Metadata>();

    public Attribute(){
    	
    }
    
    public Attribute(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<Metadata> getMetadatas() {
        return metadatas;
    }

    public void setMetadatas(List<Metadata> metadatas) {
        this.metadatas = metadatas;
    }

    public void addMetadata(Metadata metadata){
        metadatas.add(metadata);
    }

	@Override
	public String toString() {
		return "Attribute [name=" + name + ", type=" + type + ", value="
				+ value + ", metadatas=" + metadatas + "]";
	}
    
    
}