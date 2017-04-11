package br.ufrn.NGSI_10Client.entities;

import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("value")
    private String value;

    
    public Metadata(){
    	
    }
    
    public Metadata(String name, String type, String value) {
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

	@Override
	public String toString() {
		return "Metadata [name=" + name + ", type=" + type + ", value=" + value
				+ "]";
	}
    
    
}