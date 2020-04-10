package com.sm.nadaman.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import com.blackflagbin.kcommon.base.BaseFragment
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.exception.BleException
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.*
import com.sm.nadaman.common.bean.Health
import com.sm.nadaman.common.db.HealthOpe
import com.sm.nadaman.common.ecg.Ecg2DataFrame
import com.sm.nadaman.common.ecg.WaveInfo
import com.sm.nadaman.common.event.MsgEvent
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.mvp.contract.EcgMeasureContract
import com.sm.nadaman.mvp.presenter.EcgMeasurePresenter
import com.sm.nadaman.ui.activity.ReportResultActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_ecg_measure.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.toast
import sm.ecg.EcgAlgorithm
import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class EcgMeasureFragment : BaseFragment<ApiService, CacheService, EcgMeasurePresenter, Any?>(),
    EcgMeasureContract.IEcgMeasureView {

    private var maxBpms: Int = 0  //最大心率
    private var minBpms: Int = 0  //最小心率
    private var aveBpms: Int = 0  //平均心率
    /**
     * 开始测量60秒时间到生成报告
     */
    private var strException: StringBuilder? = null
    private var heartException: IntArray? = null
    private var numException = 0//测量结果级别 来自于heartException最后一位 0 正常 2轻度 4中度 6重度
    private var time: Long = 0L
    private val bpmList: ArrayList<Int> by lazy {
        ArrayList<Int>()
    }


    private val healthOpe: HealthOpe by lazy {
        HealthOpe.create()
    }

    private val format: SimpleDateFormat by lazy {
        SimpleDateFormat("HH:mm:ss")
    }

    private val countDownTimer = object : CountDownTimer(60 * 1000, 1000) {
        override fun onFinish() {
            stopQuickMeasure()
        }

        override fun onTick(millisUntilFinished: Long) {
            val time = millisUntilFinished / 1000
            if (time > 0) {
                getRealTimeHR()
                tv_time.text = "00:00:${time}"
            }
        }
    }

    private val date: Date = Date(0L)


    private val observable by lazy {
        Observable.interval(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io()).doOnDispose {
                stopManualMeasure()
            }
    }
    var timer: Disposable? = null


    private val bleUtils by lazy {
        BleUtils.getInstance()
    }
    private var isStartMeasure = false

    private val ecg2DataFrame: Ecg2DataFrame by lazy {
        Ecg2DataFrame()
    }

    private val e: EcgAlgorithm by lazy {
        EcgAlgorithm()
    }

    private lateinit var mPoints: StringBuilder

    private lateinit var buffer: StringBuffer

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_ecg_measure

    override val presenter: EcgMeasurePresenter
        get() = EcgMeasurePresenter(this)

    override fun initView() {
        super.initView()
        EventBus.getDefault().register(this)
        mPoints = StringBuilder()
        e.changeMode(0)
        back_layout.post {
            back_layout.startDraw()
            layout_wave.initStyle()
            WaveInfo.getInstance().init(back_layout)
            WaveInfo.getInstance().setLayout(WaveInfo.WaveLayout.LAYOUT_12x1)

            if (bleUtils.isConnected) {
                bleUtils.notifyCharacteristic(MyBleNotifyCallback(this))
            } else {
                toast("设备未连接！")
            }
        }

        rb_fast.setOnClickListener {
            if (bleUtils.isConnected.not()) {
                toast("设备未连接！")
                return@setOnClickListener
            }

            if (isStartMeasure && it.isSelected.not()) {
                toast("请先停止测量")
                return@setOnClickListener
            } else if (it.isSelected) {
                toast("测量未结束")
            } else {
                startQuickMeasure()
            }
        }

        rb_manual.setOnClickListener {
            if (bleUtils.isConnected.not()) {
                toast("设备未连接！")
                return@setOnClickListener
            }

            if (isStartMeasure && it.isSelected.not()) {
                toast("请先停止测量")
                return@setOnClickListener
            } else if (it.isSelected) {
                stopManualMeasure()
            } else {
                startManualMeasure()
            }
        }

        rg_mode.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.rb_stand ->
                    e.changeMode(0)
                R.id.rb_walk ->
                    e.changeMode(1)
                R.id.rb_run ->
                    e.changeMode(2)
            }
        }

    }

    private fun clear() {
        tv_time?.text = "00:00:00"
        timer = null
        bpmList.clear()
        mPoints = StringBuilder()
        strException = StringBuilder()
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
        health.mPoint = mPoints.toString()
        health.date = Date().getFormatTime(TIME_FORMAT_YMDHMS)
        health.exceptionLevel = numException
        health.measureDuration = if (date.time == 0L) 60 else date.time.div(1000L).toInt()
        val id = healthOpe.insertHealth(health)
        /*
        showToast("数据保存成功")
        bundle = Bundle()
        bundle.putLong("healthId", health.getId())*/
        startActivity(ReportResultActivity::class.java, Bundle().apply {
            putLong("healthId", id)
        })
    }


    private fun createStrException(exc: String): String {
        val result = StringBuilder()
        val excArr = exc.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var count1 = 0
        var count2 = 0
        var count5 = 0
        var count6 = 0
        var count7 = 0
        var count8 = 0
        var count9 = 0
        var count10 = 0
        var count11 = 0
        var count14 = 0
        for (str in excArr) {
            if ("心脏停搏" == str) {
                count1++
            } else if ("室颤/室速" == str) {
                count2++
            } else if ("室早RONT" == str) {
                count5++
            } else if ("两个连发室性早搏" == str) {
                count6++
            } else if ("单个早搏" == str) {
                count7++
            } else if ("偶发早搏" == str) {
                count8++
            } else if ("频发早搏" == str) {
                count9++
            } else if ("窦性心动过速" == str) {
                count10++
            } else if ("窦性心动过缓" == str) {
                count11++
            } else if ("心律不齐" == str) {
                count14++
            }
        }
        if (count1 != 0) {
            result.append("心脏停搏:").append(count1).append("次 ")
        }
        if (count2 != 0) {
            result.append("室颤/室速:").append(count2).append("次 ")
        }
        if (count5 != 0) {
            result.append("室早RONT:").append(count5).append("次 ")
        }
        if (count6 != 0) {
            result.append("两个连发室性早搏:").append(count6).append("次 ")
        }
        if (count7 != 0) {
            result.append("单个早搏:").append(count7).append("次 ")
        }
        if (count8 != 0) {
            result.append("偶发早搏:").append(count8).append("次 ")
        }
        if (count9 != 0) {
            result.append("频发早搏:").append(count9).append("次 ")
        }
        if (count10 != 0) {
            result.append("窦性心动过速:").append(count10).append("次 ")
        }
        if (count11 != 0) {
            result.append("窦性心动过缓:").append(count11).append("次 ")
        }
        if (count14 != 0) {
            result.append("心律不齐:").append(count14).append("次 ")
        }

        return result.toString()
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


    /*========================================手动测量=======================================*/
    private fun startManualMeasure() {
        rb_manual.isSelected = true
        isStartMeasure = true
        timer = observable.subscribe {
            date.time += 1000L
            getRealTimeHR()
            tv_time.text = format.format(date)
        }
    }

    private fun stopManualMeasure() {
        rb_manual.isSelected = false
        timer?.dispose()
        e.nativeAnalysisWave()
        heartException = e.nativeGetArrType()
        getExceptionMsg(heartException)
        doInStop()
        isStartMeasure = false
        date.time = 0L
        clear()
    }


    /*========================================快速测量=======================================*/
    private fun startQuickMeasure() {
        rb_fast.isSelected = true
        isStartMeasure = true
        countDownTimer.start()
    }

    private fun stopQuickMeasure() {
        rb_fast.isSelected = false
        isStartMeasure = false
        e.nativeAnalysisWave()
        heartException = e.nativeGetArrType()
        getExceptionMsg(heartException)
        doInStop()
        clear()

    }


    /*=====================数据相关================*/
    private fun getRealTimeHR() {
        e.getHeartHR()
    }

    private fun getExceptionMsg(exception: IntArray?) {
        if (exception == null) {
            return
        }
        if (exception.size >= 11)
            numException = exception[10]
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
            strException?.append("$s    ")
        }
    }

    override fun onDestroy() {
        bleUtils.removeNotifyCallback()
        super.onDestroy()
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

    private fun dealData(data: ByteArray?) {
        data?.forEach {
            if (ecg2DataFrame.appendByte(it, data) == 1) {//7
                handleDataFrame(data)
            }
        }
    }

    private fun handleDataFrame(values: ByteArray) {
        val type = ecg2DataFrame.type
        val datas = ecg2DataFrame.data
        when (type) {
            1//版本号
            -> {
            }
            2//心电数据
            -> {
                var temp: Short
                for (i in 0..7) {
                    val realData = datas[2 * i].toByte() * 256 + datas[2 * i + 1]
                    temp = (realData * 2).toShort()
                    buffer = StringBuffer()
                    buffer.append(temp.toInt()).append(",")
                    e.nativeEcgWrite(temp)//采样率250 底层是500，所以调用2次
                    val filterData: Int = e.nativeEcgWrite(temp) * 5
                    if (isStartMeasure)
                        mPoints.append(filterData).append(",")
                    if (back_layout != null)
                        layout_wave?.length(filterData)
                }
            }
            3//电池电量
            -> {
            }
            4//运动状态
            -> {
            }
            5//电极脱落检测
            -> if (datas[0] == 0x00)
                toast(getString(R.string.leadoff))
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(msgEvent: MsgEvent) {
        when (msgEvent.action) {
            ACTION_GET_HR -> {
                val hr = msgEvent.msg
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

                if (isStartMeasure) {
//                    if (Integer.parseInt(hr) > 0)
                    bpmList.add(Integer.parseInt(hr))
                }
            }
            ACTION_DISCONNECT_BLE -> {
                toast(getString(R.string.device_disconnect))
                if (isStartMeasure) {
                    countDownTimer.cancel()
                    timer?.dispose()
                    clear()
                }
            }
        }
    }

    override fun onDestroyView() {
        if (isStartMeasure) {
            countDownTimer.cancel()
            timer?.dispose()
            clear()
        }
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    class MyBleNotifyCallback(fragment: EcgMeasureFragment) : BleNotifyCallback() {
        private val myFragment: WeakReference<EcgMeasureFragment> = WeakReference(fragment)
        override fun onNotifySuccess() {

        }

        override fun onNotifyFailure(exception: BleException?) {
            myFragment.get()?.toast("心电图启动失败...")
        }

        override fun onCharacteristicChanged(data: ByteArray?) {
            myFragment.get()?.dealData(data)
        }

    }

}
