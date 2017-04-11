package br.ufrn.NGSI_10Client.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jonas on 4/16/2016.
 */
public class UpdateContext {

    @SerializedName("contextElements")
    private List<ContextElement> contextElements = new ArrayList<ContextElement>();

    @SerializedName("updateAction")
    private UpdateAction updateAction;

    public List<ContextElement> getContextElements() {
        return contextElements;
    }

    public void setContextElements(List<ContextElement> contextElements) {
        this.contextElements = contextElements;
    }

    public UpdateAction getUpdateAction() {
        return updateAction;
    }

    public void setUpdateAction(UpdateAction updateAction) {
        this.updateAction = updateAction;
    }

    public void addContextElement(ContextElement contextElement){
        contextElements.add(contextElement);
    }
}
