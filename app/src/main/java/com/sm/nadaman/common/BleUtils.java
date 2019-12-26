package com.sm.nadaman.common;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;
import com.clj.fastble.BleManager;
import com.clj.fastble.callback.BleGattCallback;
import com.clj.fastble.callback.BleNotifyCallback;
import com.clj.fastble.callback.BleScanCallback;
import com.clj.fastble.callback.BleWriteCallback;
import com.clj.fastble.data.BleDevice;
import com.clj.fastble.exception.BleException;
import com.clj.fastble.scan.BleScanRuleConfig;
import com.sm.nadaman.R;
import com.techne.nomnompos.app.App;

import java.util.List;
import java.util.UUID;

import static com.sm.nadaman.common.ConstantKt.ECG2_BLE_DEVICE_NAME;

/**
 * Created by shenmai_vea on 2018/7/3.
 */

public class BleUtils {
    private Context context;
    private static BleUtils bleUtils;

    private final BleManager bleManager;
    private BleDevice bleDevice;

    public UUID getUUID_HEART_RATE_MEASUREMENT_SERVICES() {
        return UUID_HEART_RATE_MEASUREMENT_SERVICES;
    }

    public String getBLE_DEVICE_NAME() {
        return BLE_DEVICE_NAME;
    }

    public UUID getUUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE() {
        return UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE;
    }

    public UUID getUUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC() {
        return UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC;
    }

    private UUID UUID_HEART_RATE_MEASUREMENT_SERVICES;
    private String BLE_DEVICE_NAME;
    private UUID UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE;
    private UUID UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC;


    private BleUtils() {
        context = App.Companion.getApp().getApplicationContext();
        bleManager = BleManager.getInstance();
        initBlueTooth();
    }

    public static BleUtils getInstance() {
        if (bleUtils == null) {
            bleUtils = new BleUtils();
        }
        return bleUtils;
    }

    public void setBLE_DEVICE_NAME(String BLE_DEVICE_NAME) {
        this.BLE_DEVICE_NAME = BLE_DEVICE_NAME;
    }

    /**
     * 是否是单导联
     *
     * @return
     */
    public boolean isSingleEcg() {
        return this.BLE_DEVICE_NAME.equals(ECG2_BLE_DEVICE_NAME);
    }

    public void setBleDevice(BleDevice bleDevice) {
        this.bleDevice = bleDevice;

    }

    public void checkBle() {
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(context, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(context, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void initBlueTooth() {
        bleManager.init(App.Companion.getApp());
        bleManager
                .enableLog(true)
                .setReConnectCount(1, 10000)
                .setOperateTimeout(10000);
        /*UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC = ECG2_UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC;
        UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE = ECG2_UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE;
        UUID_HEART_RATE_MEASUREMENT_SERVICES = ECG2_UUID_HEART_RATE_MEASUREMENT_SERVICES;*/
        BLE_DEVICE_NAME = ECG2_BLE_DEVICE_NAME;
    }

    private void setScanRule() {
        BleScanRuleConfig scanRuleConfig = new BleScanRuleConfig.Builder()
                /*.setServiceUuids(new UUID[]{UUID_HEART_RATE_MEASUREMENT_SERVICES})      // 只扫描指定的服务的设备，可选*/
                .setDeviceName(true, BLE_DEVICE_NAME)   // 只扫描指定广播名的设备，可选
                .setScanTimeOut(10000)              // 扫描超时时间，可选，默认10秒
                .build();
        bleManager.initScanRule(scanRuleConfig);
    }

    /**
     * 开始扫描
     *
     * @param bleScanCallback
     */
    public void startScan(BleScanCallback bleScanCallback) {
        setScanRule();

        bleManager.scan(bleScanCallback);
    }

    /**
     * 取消扫描
     */
    public void cancelScan() {
        bleManager.cancelScan();
    }

    /**
     * 连接ble
     *
     * @param bleDevice
     * @param bleGattCallback
     */
    public void connect(BleDevice bleDevice, BleGattCallback bleGattCallback) {
        this.bleDevice = bleDevice;
        bleManager.connect(bleDevice, bleGattCallback);
    }

    public void getService(List<BluetoothGattService> services) {
        for (BluetoothGattService service : services) {
            if (service.getUuid().toString().contains("18f0")) {
                UUID_HEART_RATE_MEASUREMENT_SERVICES = service.getUuid();
                List<BluetoothGattCharacteristic> characteristics = service.getCharacteristics();
                for (BluetoothGattCharacteristic characteristic : characteristics) {
                    int properties = characteristic.getProperties();
                    if (properties == BluetoothGattCharacteristic.PROPERTY_WRITE + BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) {
                        UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE = characteristic.getUuid();
                    } else if (properties == BluetoothGattCharacteristic.PROPERTY_NOTIFY) {
                        UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC = characteristic.getUuid();
                    }
                }
            }
        }
    }

    public void writeData(byte[] bytes) {
        if (bleDevice != null) {
            bleManager.write(bleDevice,
                    UUID_HEART_RATE_MEASUREMENT_SERVICES.toString(),
                    UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE.toString(),
                    bytes,
                    new BleWriteCallback() {
                        @Override
                        public void onWriteSuccess(int current, int total, byte[] justWrite) {
                            // 发送数据到设备成功
                        }

                        @Override
                        public void onWriteFailure(BleException exception) {
                            // 发送数据到设备失败
                        }
                    });
        }
    }

    public void notifyCharacteristic(BleNotifyCallback bleNotifyCallback) {
        if (bleDevice != null) {
            bleManager.notify(bleDevice,
                    UUID_HEART_RATE_MEASUREMENT_SERVICES.toString(),
                    UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC.toString(),
                    bleNotifyCallback);
        }

    }
    public void stopNotify(){
        if (bleDevice!=null)
            bleManager.stopNotify(bleDevice,UUID_HEART_RATE_MEASUREMENT_SERVICES.toString(),UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC.toString());
    }
    public void disconnect() {
        if (bleDevice != null && bleManager.isConnected(bleDevice)) {
            bleManager.disconnect(bleDevice);
            bleManager.removeConnectGattCallback(bleDevice);
            bleManager.removeNotifyCallback(bleDevice, UUID_HEART_RATE_MEASUREMENT_CHARACTER_SYNC.toString());
            bleManager.removeWriteCallback(bleDevice, UUID_HEART_RATE_MEASUREMENT_CHARACTER_WRITE.toString());
        }
    }

    public boolean bleIsOpen() {
        return bleManager.isBlueEnable();
    }


}
