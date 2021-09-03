package com.rfid.plugins.myplugin;

import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MifareClassicReadTask extends AsyncTask<Void, Void, Void> {
    // MIFARE Classic tags are divided into sectors, and each sector is sub-divided into blocks.
    // Block size is always 16 bytes (BLOCK_SIZE). Sector size varies.
    // MIFARE Classic 1k are 1024 bytes (SIZE_1K), with 16 sectors each of 4 blocks.

    DataReadListener dataReadListener;
    MifareClassic taskTag;
    List<SectorDataClass> sectorDataList;
    boolean success;
    final int numOfBlockInSector = 4;
    final int sectorCount = 12; // MIFARE Classic 1KB
    TagResultClass result;

    MifareClassicReadTask(Tag tag, byte[] defaultKey) {
        result = new TagResultClass();
        this.taskTag = MifareClassic.get(tag);
        this.sectorDataList = new ArrayList<>();

        byte[] tagId = tag.getId();
        result.TagIdHex = tag.getId();

        for (int i = 0; i < tagId.length; i++) {
            result.TagId += Integer.toHexString(tagId[i] & 0xFF);
        }

        String[] techList = tag.getTechList();
        for (int i = 0; i < techList.length; i++) {
            result.TechList += techList[i] + "\n";
        }

        readMifareClassic(taskTag, result);

        for (int s = 0; s < sectorCount; s++) {
            SectorDataClass sectorData = new SectorDataClass();
            sectorData.Key = defaultKey;
            sectorData.SectorID = s;
            sectorDataList.add(sectorData);
        }
    }

    public void setDataReadListener(DataReadListener dataReadListener) {
        this.dataReadListener = dataReadListener;
    }

    @Override
    protected void onPreExecute() {
        dataReadListener.onPreExecute("Reading Tag, don't remove it!");
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (result.IsSizAccepted == false || result.IsTypeAccepted == false) {
            result.ResultContent = "Tag is not required type !";
            return null;
        }

        try {
            taskTag.connect();

            for (final SectorDataClass sectorData : sectorDataList) {
                if (taskTag.authenticateSectorWithKeyA(sectorData.SectorID, sectorData.Key)) {
                    for (int b = 0; b < numOfBlockInSector; b++) {
                        int blockIndex = (sectorData.SectorID * numOfBlockInSector) + b;
                        sectorData.Data[b] = taskTag.readBlock(blockIndex);
                    }

                    sectorData.authenticateSector = true;
                } else {
                    sectorData.authenticateSector = false;
                }
            }

            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (taskTag != null) {
                try {
                    taskTag.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (success) {
            SectorDataClass sectorO = sectorDataList.get(0);
            SectorDataClass sector1 = sectorDataList.get(1);
            SectorDataClass sector2 = sectorDataList.get(2);
            if (sectorO.authenticateSector && sector1.authenticateSector && sector2.authenticateSector) {
                result.ResultContent = "Reading from tag OK";
                result.Name = sectorO.GetString(1) + sectorO.GetString(2);
                result.Surname = sector1.GetString(0) + sector1.GetString(1);
                result.UserInfo = sector1.GetString(2);
                result.Hash = sector2.GeTHexString(0) + sector2.GeTHexString(1);

                dataReadListener.dataDownloadedSuccessfully(result);
            } else {
                result.ResultContent = "Unauthorized access";
                dataReadListener.dataDownloadFailed(result);
            }
        } else {
            result.ResultContent = "Fail to read Blocks!!!";
            dataReadListener.dataDownloadFailed(result);
        }
    }

    public static interface DataReadListener {
        void dataDownloadedSuccessfully(TagResultClass result);

        void dataDownloadFailed(TagResultClass result);

        void onPreExecute(String info);
    }

    public void readMifareClassic(MifareClassic mifareClassicTag, TagResultClass result) {
        int type = mifareClassicTag.getType();
        switch (type) {
            case MifareClassic.TYPE_PLUS:
                result.TagType = "MifareClassic.TYPE_PLUS";
                break;
            case MifareClassic.TYPE_PRO:
                result.TagType = "MifareClassic.TYPE_PRO";
                break;
            case MifareClassic.TYPE_CLASSIC:
                result.TagType = "MifareClassic.TYPE_CLASSIC";
                result.IsTypeAccepted = true;
                break;
            case MifareClassic.TYPE_UNKNOWN:
                result.TagType = "MifareClassic.TYPE_UNKNOWN";
                break;
            default:
                result.TagType = "unknown...!";
        }

        int size = mifareClassicTag.getSize();
        switch (size) {
            case MifareClassic.SIZE_1K:
                result.TagSize = "MifareClassic.SIZE_1K";
                result.IsSizAccepted = true;
                break;
            case MifareClassic.SIZE_2K:
                result.TagSize = "MifareClassic.SIZE_2K";
                result.IsSizAccepted = true;
                break;
            case MifareClassic.SIZE_4K:
                result.TagSize = "MifareClassic.SIZE_4K";
                result.IsSizAccepted = true;
                break;
            case MifareClassic.SIZE_MINI:
                result.TagSize = "MifareClassic.SIZE_MINI";
                break;
            default:
                result.TagSize = "unknown size...!";
        }
    }
}
