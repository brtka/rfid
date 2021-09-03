package com.rfid.plugins.myplugin;

import java.security.PublicKey;

public class TagResultClass {
    public String ResultContent;
    public String Name;
    public String Surname;
    public String UserInfo;
    public String Hash;
    public String Pass;

    public byte[] TagIdHex;
    public String TagId = "";

    public String TechList = "";
    public String TagType;
    public String TagSize;
    public boolean IsTypeAccepted = false;
    public boolean IsSizAccepted = false;
}
