package com.sm.nadaman.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.blackflagbin.kcommon.base.BaseFragment
import com.blackflagbin.kcommon.utils.StatusBarUtil
import com.blankj.utilcode.util.ToastUtils
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.exception.BleException
import com.kennyc.view.MultiStateView
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.*
import com.sm.nadaman.common.bean.Ecg12Data
import com.sm.nadaman.common.bean.Health
import com.sm.nadaman.common.db.EcgDataOpe
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.ecg.Ecg2DataFrame
import com.sm.nadaman.common.event.MsgEvent
import com.sm.nadaman.common.helper.DataHelpeFactory
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.widget.SettingWindow
import com.sm.nadaman.common.widget.ecg.WaveTView
import com.sm.nadaman.common.widget.ecg.WaveViewConfig
import com.sm.nadaman.mvp.contract.Ecg12MeasureContract
import com.sm.nadaman.mvp.presenter.Ecg12MeasurePresenter
import com.sm.nadaman.ui.activity.ReportResult12Activity
import com.sm.nadaman.ui.activity.ReportResultActivity
import kotlinx.android.synthetic.main.fragment_ecg12_measure.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.toast
import sm.ecg.EcgAlgorithm
import java.lang.ref.WeakReference
import java.util.*

class Ecg12MeasureFragment : BaseFragment<ApiService, CacheService, Ecg12MeasurePresenter, Any?>(),
    Ecg12MeasureContract.IEcg12MeasureView {
    var isStart = false
    val picMode = intArrayOf(R.mipmap.stand_12, R.mipmap.walk_12, R.mipmap.run_12)

    private var maxBpms: Int = 0  //最大心率
    private var minBpms: Int = 0  //最小心率
    private var aveBpms: Int = 0  //平均心率
    private var selectIndex = byteArrayOf(0, 1, 0, 1, 1)

    private var time: Long = 0L
    private var measure: Int = 0
    private var strException: StringBuilder? = null
    private var heartException: IntArray? = null
    private var numException = 0//测量结果级别 来自于heartException最后一位 0 正常 2轻度 4中度 6重度
    private var countDownTimer: CountDownTimer = object : CountDownTimer(
        when (measure) {
            0 -> 60 * 1000
            1 -> 30 * 1000
            else -> 10 * 1000
        }, 1000
    ) {
        override fun onFinish() {
            stop()
        }

        override fun onTick(millisUntilFinished: Long) {
            val time = millisUntilFinished / 1000
            if (time > 0) {
                e.getHeartHR()
                getExceptionMsg(heartException)
                tv_start.text = "${time}sec"
            }
        }
    }

    private val bpmList: ArrayList<Int> by lazy {
        ArrayList<Int>()
    }
    val lineTitle by lazy {
        resources.getStringArray(R.array.ecgTitles)
    }

    private val mList: List<WaveTView> by lazy {
        ecg12ViewDataHelper.views
    }

    val config by lazy {
        WaveViewConfig().setGain(10F).setSpeed(25F).setPagesize(1)
    }
    val ecg12ViewDataHelper by lazy { DataHelpeFactory.create(activity, config) }

    val adapter: PagerAdapter by lazy {
        object : PagerAdapter() {
            override fun getCount(): Int {
                return mList.size
            }

            override fun isViewFromObject(view: View, o: Any): Boolean {
                return view === o
            }

            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                val child = mList.get(position)
                container.addView(child)
                return child
            }

            override fun destroyItem(container: ViewGroup, position: Int, t: Any) {
                container.removeView(t as View)
            }

            override fun getItemPosition(t: Any): Int {
                return POSITION_NONE
            }
        }
    }

    val e by lazy {
        EcgAlgorithm()
    }
    val ecg2DataFrame by lazy {
        Ecg2DataFrame()
    }
    val healthOpe by lazy {
        HealthOpe.create()
    }
    val ecg12DataDao by lazy {
        EcgDataOpe.create()
    }

    val popupView by lazy {
        SettingWindow(activity!!) {
            applyConfig(it)
        }
    }
    val basePopupView by lazy {
        XPopup.Builder(activity)
            .hasShadowBg(true)
            .hasStatusBarShadow(true)
            .popupPosition(PopupPosition.Top)
            .enableDrag(false)
            .asCustom(popupView)
    }

    private var displayMode: Int = 0//0：单屏 1双屏

    val bleUtils by lazy { BleUtils.getInstance() }
    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_ecg12_measure

    override val presenter: Ecg12MeasurePresenter
        get() = Ecg12MeasurePresenter(this)

    override fun initView() {
        EventBus.getDefault().register(this)
        super.initView()
        e.changeMode(0)
        if (bleUtils.isConnected) {
            bleUtils.writeData(GET_ECG12_WAVEFORM_ON)
            bleUtils.notifyCharacteristic(MyBleNotifyCallback(this))
        }
        act_measure_ecg12_layout_wave.adapter = adapter
        tv_start.setOnClickListener {
            start(true)
        }
        go_home.setOnClickListener {
            activity?.finish()
        }
        setting.setOnClickListener {
            if(isStart){
                ToastUtils.showShort("测量未结束！")
                return@setOnClickListener
            }
            popupView.selectedIndex = selectIndex
            basePopupView.show()
        }


        tv_start.postDelayed({

            Config.config?.also {
                applyConfig(it)
            }
        },500)
    }


    override fun onStop() {
        super.onStop()
        ecg12ViewDataHelper.pause()
    }

    override fun onResume() {
        super.onResume()
        ecg12ViewDataHelper.resume()
    }


    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        countDownTimer.cancel()
        super.onDestroyView()
    }

    private fun start(start: Boolean) {
        if (isStart != start) {
            if (isStart) {
                if (bleUtils.isConnected) {
                    ToastUtils.showShort("设备未连接！")
                    isStart = false
                    return
                }
            } else {
                countDownTimer.start()
            }
            isStart = start
        }
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }


    private fun applyConfig(configBean: SettingWindow.ConfigBean) {
        Config.config = configBean
        this.selectIndex = configBean.selectedIndex
        e.changeMode(configBean.mode)
        measure = configBean.measure

        countDownTimer = object : CountDownTimer(
            when (measure) {
                0 -> 60 * 1000
                1 -> 30 * 1000
                else -> 10 * 1000
            }, 1000
        ) {
            override fun onFinish() {
                stop()
            }
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                if (time > 0) {
                    e.getHeartHR()
                    getExceptionMsg(heartException)
                    tv_start.text = "${time}sec"
                }
            }
        }

        iv_mode.setImageResource(picMode[configBean.mode])
        config.setGain(configBean.gain).setSpeed(configBean.speed)
        act_measure_ecg12_tv_augment_mv.text =
            String.format(Locale.CHINA, "%smm/mV", configBean.gain)
        act_measure_ecg12_tv_speed.text = String.format(Locale.CHINA, "%smm/s", configBean.speed)
        if (displayMode != configBean.displayMode) {
            //changed
            displayMode = configBean.displayMode
            config.pagesize = displayMode + 1
            ecg12ViewDataHelper.reset(config)
            adapter.notifyDataSetChanged()
        } else
            ecg12ViewDataHelper.reset(config)
    }

    private fun getExceptionMsg(exception: IntArray?) {
        if (exception == null) {
            return
        }
        strException = StringBuilder()
        val stringArray = resources.getStringArray(R.array.result)
        for (i in exception) {
            if (i == 0) {
                break
            }
            val s = stringArray[i]
            if (s.contains("逆钟向转位") || s.contains("顺钟向转位"))
            //该结果属于正常现象，不该显示给用户，所以屏蔽
            {
                continue
            }
            strException?.append(s)?.append("    ")
        }
    }

    fun stop() {
        getResult()
        tv_start.text = "开始记录"
        tv_bpm.text = "0"
        isStart = false
        maxBpms = 0
        aveBpms = 0
        minBpms = 0
        numException = 0
        strException = null
        heartException = null
        bpmList.clear()
    }


    private fun getResult() {
        e.nativeAnalysisWave()
        heartException = e.nativeGetArrType()
        aveBpms = e.nativeGetAnaHR()//获取平均心率
        getExceptionMsg(heartException)
        doInStop()
    }

    private fun saveData() {
        var exc = strException.toString()
        if ("" == exc) {
            exc = "正常"
        } else {
            //            exc = createStrException(exc);
        }
        val health = Health()
        health.aveHeartRate = aveBpms
        health.heartStrException = exc
        health.maxHeartRate = maxBpms
        health.minHeartRate = minBpms
        val ecg12Data = Ecg12Data()
        ecg12Data.data = ecg12ViewDataHelper.data
        health.ecg12Data = ecg12Data
        health.date = Date().getFormatTime(TIME_FORMAT_YMDHMS)
        health.exceptionLevel = heartException?.get(heartException!!.size - 1) ?: 0
        health.measureDuration = when (measure) {
            0 -> 60
            1 -> 30
            else -> 10 
        }
        health.type = 1
        val l = healthOpe.insertHealth(health)
        if (l >= 0) {
            ecg12Data.id = health.id
            ecg12DataDao.insert(ecg12Data)
            startActivity(ReportResult12Activity::class.java, Bundle().apply {
                putLong("healthId", health.id)
            })
            toast("数据保存成功")
        } else {
            toast("发生意外，请重新测量")
        }

    }


    private fun doInStop() {
        bpmList.sort()
        if (bpmList.size < 2) {
            toast("测量失败，没有获取到心率值")
            return
        }
        maxBpms = bpmList[bpmList.size - 1]
        minBpms = bpmList[0]
        aveBpms = e.nativeGetAnaHR()
        saveData()
    }

    fun decode(values: ByteArray) {
        for (value in values) {
            if (ecg2DataFrame.appendByte(value, values) === 1) {
                handleDataFrame(values)
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msgEvent: MsgEvent) {
        when (msgEvent.action) {
            ACTION_GET_HR -> {
                val hr = if (Integer.parseInt(msgEvent.msg) > 0) msgEvent.msg else "0"
                tv_bpm.text = hr
                if (System.currentTimeMillis() - time >= 3000) {
                    if (Integer.parseInt(hr) > 120) {
                        toast(getString(R.string.heart_rate_high))
                    } else if (Integer.parseInt(hr) <= 0) {
                        toast(getString(R.string.heart_rate_no_detected))
                    } else if (Integer.parseInt(hr) < 50) {
                        toast(getString(R.string.heart_rate_low))
                    }
                    time = System.currentTimeMillis()
                }
                if (isStart) {
//                    if (Integer.parseInt(hr) > 0)
                        bpmList.add(Integer.parseInt(hr))
                }
            }
            ACTION_DISCONNECT_BLE -> {
                countDownTimer.cancel()
                countDownTimer.onFinish()
                toast(getString(R.string.device_disconnect))
            }
        }
    }


    private fun handleDataFrame(values: ByteArray) {
        val type = ecg2DataFrame.type
        val datas = ecg2DataFrame.data
        when (type) {
            0x11//协议版本
            -> {
            }
            0x12//心电数据
            -> {
                if (datas.size != 16) {
                    return
                }
                val out = ShortArray(12)
                val data1 = ShortArray(8)
                for (i in 0..7) {
                    val data = datas[2 * i]
                    val realData = data.toByte() * 256 + datas[2 * i + 1]
                    data1[i] = (realData * 2).toShort()
                }
                e.nativeEcgWrite(data1, out, 1.toShort())
                e.nativeEcgWrite(data1, out, 1.toShort())
                e.nativeEcgWrite(data1, out, 1.toShort())
                e.nativeEcgWrite(data1, out, 1.toShort())
                for (i in out.indices) {
                    out[i] = (out[i] * 5).toShort()
                }
                ecg12ViewDataHelper.addData(out)
            }
            0x13//电量
            -> {
            }
            0x14//运动状态
            -> {
            }
            0x15 -> {
                val str = StringBuilder()
                var isFall = false
                for (i in values.indices) {
                    if (values[i].toInt() == 0) {
                        isFall = true
                        if (i == values.size - 1) {
                            str.append(lineTitle[i])
                        } else {
                            str.append(lineTitle[i]).append(",")
                        }
                    }
                }
                if (isFall) {
                    str.append("导联脱落")
                    toast(str.toString())
                }
            }
        }

    }


    class MyBleNotifyCallback(fragment: Ecg12MeasureFragment) : BleNotifyCallback() {
        private val myFragment: WeakReference<Ecg12MeasureFragment> = WeakReference(fragment)
        override fun onNotifySuccess() {

        }

        override fun onNotifyFailure(exception: BleException?) {
            myFragment.get()?.toast("心电图启动失败...")
        }

        override fun onCharacteristicChanged(data: ByteArray?) {
            data?.let {
                myFragment.get()?.decode(it)
            }
        }

    }

}

