package com.rfid.plugins.myplugin;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.annotation.CapacitorPlugin;
import android.nfc.Tag;
import android.content.Intent;
import android.nfc.NfcAdapter;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import org.xmlpull.v1.XmlPullParser;

@CapacitorPlugin(name = "Rfid")
public class RfidPlugin extends Plugin {

    @Override
    protected void handleOnNewIntent(Intent intent) {
        super.handleOnNewIntent(intent);

        if (intent.getAction().equals(NfcAdapter.ACTION_TECH_DISCOVERED)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            byte[] defaultKey = ReadingPass(); //{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};

            MifareClassicReadTask readTask = new MifareClassicReadTask(tag, defaultKey);
            readTask.setDataReadListener(new MifareClassicReadTask.DataReadListener() {
                @SuppressWarnings("unchecked")
                @Override
                public void dataDownloadedSuccessfully(TagResultClass result) {
                    JSObject ret = new JSObject();
                    ret.put("resultContent", result.ResultContent);
                    ret.put("name", result.Name);
                    ret.put("surname", result.Surname);
                    ret.put("userInfo", result.UserInfo);
                    ret.put("hash", result.Hash);
                    ret.put("UID", result.TagId);

                    notifyListeners("rfid", ret);
                }

                @Override
                public void dataDownloadFailed(TagResultClass result) {
                    JSObject ret = new JSObject();
                    ret.put("resultContent", result.ResultContent);
                    notifyListeners("rfid", ret);
                }

                @Override
                public void onPreExecute(String info) {
                    JSObject ret = new JSObject();
                    ret.put("resultContent", info);
                    notifyListeners("rfid", ret);
                }
            });
            readTask.execute();

        }
    }

    @PluginMethod
    public void echo(PluginCall call) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(getContext());
        if (nfcAdapter == null) {
            //Toast.makeText(getContext(), "NFC NOT supported on this devices!", Toast.LENGTH_LONG).show();

            JSObject ret = new JSObject();
            ret.put("resultContent", "NFC NOT supported on this devices!");
            notifyListeners("rfid", ret);
        } else if (!nfcAdapter.isEnabled()) {
            //Toast.makeText(getContext(), "NFC NOT Enabled!", Toast.LENGTH_LONG).show();

            JSObject ret = new JSObject();
            ret.put("resultContent", "NFC NOT Enabled!");
            notifyListeners("rfid", ret);
        }
    }

    private byte[] ReadingPass(){
        String pass = "";
        byte[] array = new byte[6];
        try {
            XmlPullParser xpp = getContext().getResources().getXml(R.xml.authenticationpass);
            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                if(eventType == XmlPullParser.TEXT)
                {
                    pass = xpp.getText();
                }
                eventType = xpp.next();
            }

            for (int i = 0, arrayIndex = 0; i < pass.length(); i += 2, arrayIndex++) {
                array[arrayIndex] = Integer.valueOf(pass.substring(i, i + 2), 16).byteValue();
            }
        } catch (Exception ex) {
            JSObject ret = new JSObject();
            ret.put("resultContent", "Error reading pass");
            notifyListeners("rfid", ret);
        }
        return array;
    }
}
