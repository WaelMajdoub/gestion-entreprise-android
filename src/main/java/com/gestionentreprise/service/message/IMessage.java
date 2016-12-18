package com.gestionentreprise.service.message;

public interface IMessage {
    public static final int SUCCES = 1;
    public static final int IN_PROGRESS = 2;
    public static final int PARSE_EXCEPTION = 3;
    public static final int DATA_NOT_FOUND = 4;
    public static final int CONNECTION_EXCEPTION = 0;

    public int getCode();

    public String getLibelle();
}
