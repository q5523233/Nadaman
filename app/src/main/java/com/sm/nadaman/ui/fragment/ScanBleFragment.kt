package com.sm.nadaman.ui.fragment

import android.bluetooth.BluetoothAdapter.ACTION_REQUEST_ENABLE
import android.bluetooth.BluetoothGatt
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackflagbin.kcommon.utils.PermissionUtils
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.sm.nadaman.R
import com.sm.nadaman.common.*
import com.sm.nadaman.common.event.MsgEvent
import com.sm.nadaman.ui.activity.HomeActivity
import com.sm.nadaman.ui.adapter.BleAdapter
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_sacn_ble.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import java.lang.ref.WeakReference


class ScanBleFragment : RxFragment() {
    private val animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim_rotate)
    }
    private val bleUtils by lazy {
        BleUtils.getInstance()
    }

    val bleAdapter by lazy {
        BleAdapter(scanResultList)
    }

    private val scanResultList by lazy {
        ArrayList<BleDevice>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sacn_ble, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Config.isSingleEcg.not()){
            cl_host.setBackgroundResource(R.mipmap.scan_bg_12)
            iv_scan_bg.setImageResource(R.mipmap.scan_circle_12)
        }
        bleUtils.checkBle()
        bleUtils.blE_DEVICE_NAME =
            if (Config.isSingleEcg) ECG2_BLE_DEVICE_NAME else ECG12_BLE_DEVICE_NAME

        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = bleAdapter
        }

        tv_next.setOnClickListener {
            scanResultList.singleOrNull()?.let {
                bleUtils.connect(it, MyBleGattCallback(this))
            }
        }
        tv_reseach.setOnClickListener {
            startScan()
        }

        tv_go_home.setOnClickListener {
            startActivity(Intent(activity, HomeActivity::class.java).clearTask().newTask())
        }

        PermissionUtils.location(context!!, {
            startScan()
        })

    }


    private fun writeDataToBle(data: ByteArray) {
        bleUtils.writeData(data)
    }

    private fun openBle() {
        val enableBtIntent = Intent(ACTION_REQUEST_ENABLE)
        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    private fun scan() {
        scanResultList.clear()

        bleUtils.startScan(MyBleScanCallback(this))
    }

    private fun connectSuccess() {

        animation.cancel()
        tv_title2.text = "配对成功"
        iv_ble.setImageResource(R.mipmap.ble_connect_success)
        tv_tip.isVisible = false
        tv_title1.isVisible = false
        tv_title2.isVisible = true
        recycler.isVisible = false
        tv_reseach.isVisible = false
        tv_go_home.isVisible = true
        tv_next.isInvisible = true
    }

    fun scanError() {
        animation.cancel()
        tv_title2?.text = "配对失败"
        iv_ble?.setImageResource(R.mipmap.error)
        tv_tip?.isVisible = false
        tv_title1?.isVisible = false
        tv_title2?.isVisible = true
        recycler?.isVisible = false
        tv_reseach?.isVisible = true
        tv_go_home?.isVisible = true
        tv_next?.isInvisible = true
    }

    fun finishScan() {
        animation.cancel()
        tv_title2.text = getString(R.string.choice_your_drive)
        tv_tip.isVisible = false
        tv_title1.isVisible = false
        tv_title2.isVisible = true
        recycler.isVisible = true
        tv_reseach.isVisible = true
        tv_go_home.isVisible = false
        tv_next.isInvisible = false
        tv_next.isEnabled = true
    }

    fun startScan() {
        iv_scan.startAnimation(animation)
        iv_ble.setImageResource(R.mipmap.ble)
        tv_tip.isVisible = true
        tv_title1.isVisible = true
        tv_title2.isVisible = false
        tv_title2.text = getString(R.string.choice_your_drive)
        recycler.isVisible = false
        tv_reseach.isVisible = true
        tv_go_home.isVisible = false
        tv_next.isInvisible = false
        tv_next.isEnabled = false
        when {
            bleUtils.bleIsOpen().not() -> openBle()
            checkGPSIsOpen().not() -> openGPS()
            else -> scan()
        }
    }

    private fun checkGPSIsOpen(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    private fun openGPS() {
        AlertDialog.Builder(activity!!).setMessage("开启GPS定位").setNegativeButton("取消", null)
            .setPositiveButton("打开") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, REQUEST_CODE_OPEN_GPS)
            }.show()
    }

    override fun onStop() {
        super.onStop()
        animation.cancel()
        bleUtils.cancelScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_ENABLE_BT -> if (checkGPSIsOpen()) {
                scan()
            } else {
                openGPS()
            }
            REQUEST_CODE_OPEN_GPS -> if (bleUtils.bleIsOpen()) {
                scan()
            } else {
                openBle()
            }
        }
    }

    class MyBleScanCallback(fragment: ScanBleFragment) : BleScanCallback() {
        private val myFragment: WeakReference<ScanBleFragment> = WeakReference(fragment)

        override fun onScanStarted(success: Boolean) {

        }

        override fun onScanning(result: BleDevice) {
            myFragment.get()?.finishScan()
            myFragment.get()?.bleAdapter?.addData(result)
        }

        override fun onScanFinished(scanResultList: List<BleDevice>) {
            if (scanResultList.isEmpty()) {
                myFragment.get()?.scanError()
            }
        }
    }

    class MyBleGattCallback(fragment: ScanBleFragment) : BleGattCallback() {
        private val myFragment: WeakReference<ScanBleFragment> = WeakReference(fragment)

        override fun onStartConnect() {

        }

        override fun onConnectFail(bleDevice: BleDevice?, exception: BleException?) {
            myFragment.get()?.scanError()
        }

        override fun onConnectSuccess(
            bleDevice: BleDevice?,
            gatt: BluetoothGatt?,
            status: Int
        ) {
            myFragment.get()?.connectSuccess()
            val services = gatt?.services
            myFragment.get()?.bleUtils?.getService(services)
            myFragment.get()
                ?.writeDataToBle(if (Config.isSingleEcg) GET_ECG2_WAVEFORM_ON else GET_ECG12_WAVEFORM_ON)
        }

        override fun onDisConnected(
            isActiveDisConnected: Boolean,
            device: BleDevice?,
            gatt: BluetoothGatt?,
            status: Int
        ) {
            EventBus.getDefault().post(MsgEvent(ACTION_DISCONNECT_BLE, ""))
        }

    }
}