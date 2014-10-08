package edu.odu.ece486.hm_app;

import android.bluetooth.BluetoothDevice;

import java.util.UUID;

/**
 * Created by Larry on 10/8/2014.
 * This class will provide services for assisting with bluetooth device information
 * reception and verification
 */
public class BluetoothHelper {
    public static String shortUuidFormat = "0000%04X-0000-1000-8000-00805F9B34FB";

    public static UUID sixteenBitUuid(long shortUuid) {
        assert shortUuid >= 0 && shortUuid <= 0xFFFF;
        return UUID.fromString(String.format(shortUuidFormat, shortUuid & 0xFFFF));
    }

    //Test function to pull bluetooth device information.
    public static String getDeviceInfoText(BluetoothDevice device, int rssi, byte[] scanRecord) {
        return new StringBuilder()
                .append("Name: ").append(device.getName())
                .append("\nMAC: ").append(device.getAddress())
                .append("\nRSSI: ").append(rssi)
                .append("\nScan Record: ").append(parseScanRecord(scanRecord))
                .toString();
    }

    //Test function to pull bluetooth device information.
    private static String parseScanRecord(byte[] scanRecord) {
        StringBuilder output = new StringBuilder();
        int i = 0;
        while (i < scanRecord.length){
            int len = scanRecord[i++] & 0xFF;
            if (len == 0) break;
            switch (scanRecord[i] & 0xFF){
                case 0x0A:
                    output.append("\n  Tx Power: ").append(scanRecord[i+1]);
                    break;
                case 0xFF:
                    output.append("\n  Advertisement Data: ")
                            .append(HexAsciiConverter.bytesToHex(scanRecord, i+3, len));
                    String ascii = HexAsciiConverter.bytesToAsciiMaybe(scanRecord, i+3, len);
                    if (ascii != null){
                        output.append(" (\"").append(ascii).append("\")");
                    }
                    break;
            }
            i += len;
        }
        return output.toString();
    }
}
