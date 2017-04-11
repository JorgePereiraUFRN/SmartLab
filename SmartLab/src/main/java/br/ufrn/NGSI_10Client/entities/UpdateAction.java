package br.ufrn.NGSI_10Client.entities;

public enum UpdateAction {

    UPDATE("UPDATE"),
    APPEND("APPEND"),
    DELETE("DELETE");

    private final String action;

    private UpdateAction(final String action) {
        this.action = action;
    }

}