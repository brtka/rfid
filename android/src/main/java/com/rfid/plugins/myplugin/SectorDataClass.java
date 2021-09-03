package com.rfid.plugins.myplugin;
import java.nio.charset.StandardCharsets;

public class SectorDataClass {
    public int SectorID;
    public byte[] Key;
    public boolean authenticateSector = false;
    public byte[][] Data = new byte[4][16];

    public String GetString(int blockId) {
        return new String(Data[blockId], StandardCharsets.US_ASCII);
    }

    public  String GeTHexString(int blockId){
        String hexString = "";

        for(int i=0; i<Data[blockId].length; i++){
            hexString += String.format("%02X", Data[blockId][i] & 0xff);
        }
        return  hexString;
    }
}

