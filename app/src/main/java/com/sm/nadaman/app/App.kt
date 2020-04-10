package com.techne.nomnompos.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.multidex.MultiDex
import com.blackflagbin.kcommon.entity.net.IApiException
import com.blackflagbin.kcommon.facade.CommonLibrary
import com.blankj.utilcode.util.SPUtils
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.sm.nadaman.BuildConfig
import com.sm.nadaman.R
import com.sm.nadaman.common.Account
import com.sm.nadaman.common.Config
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.Loge
import com.sm.nadaman.common.SIGNOUT_CODE_FORBIDDEN
import com.sm.nadaman.common.SIGNOUT_CODE_SINGLE
import com.sm.nadaman.common.SP_NAME
import com.sm.nadaman.common.bean.User
import com.sm.nadaman.common.dao.DaoSession
import com.sm.nadaman.common.db.manager.UserDBController
import com.sm.nadaman.common.utils.SpUtils
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by blackflagbin on 2018/3/19.
 */
class App : Application(), Application.ActivityLifecycleCallbacks {

    init {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            layout.setPrimaryColorsId(android.R.color.transparent, R.color.black)//全局设置主题颜色
            ClassicsHeader(context)//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter(context).setDrawableSize(20f).setAccentColorId(R.color.black)
        }


    }


    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (activityList.contains(activity)) {
            activityList.remove(activity)//从集合中移除
            activity?.finish()//销毁当前Activity
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        activityList.add(activity)
    }


    //定位相关
/*    private var locationManager: LocationManager? = null
    private val mLocationListener = object : LocationListener {
        @SuppressLint("MissingPermission")
        override fun onLocationChanged(location: Location?) {
            location?.apply {
                App.latitude = latitude.toString()
                App.longitude = longitude.toString()

            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
        }

        override fun onProviderEnabled(provider: String) {
        }

        override fun onProviderDisabled(provider: String) {
        }
    }*/

    companion object {


        var app: App? = null

        val activityList: ArrayList<Activity?> = ArrayList()

        fun getMyUUID(): String {
            SPUtils.getInstance(SP_NAME).getString("uuid")?.let {
                if (it.isNotEmpty())
                    return it
            }

            val tm = CommonLibrary.instance.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val tmDevice: String
            val tmSerial: String
            val androidId: String
            tmDevice = "" + tm.deviceId//设备唯一号码
            tmSerial = "" + tm.simSerialNumber//sim 卡标识
            androidId = "" + android.provider.Settings.Secure.getString(
                CommonLibrary.instance.context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID
            )//在设备首次启动时，系统会随机生成一个64位的数字
            val deviceUuid = UUID(
                androidId.hashCode().toLong(),
                tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong()
            )
            SPUtils.getInstance(SP_NAME).put("uuid", deviceUuid.toString())
            return deviceUuid.toString()

        }

        /**
         * 销毁所有的Activity
         */
        fun removeALLActivity() {
            //通过循环，把集合中的所有Activity销毁
            for (activity in activityList
            ) {
                Loge("removeALLActivity: " + activity?.getLocalClassName())
                activity?.finish()
            }
        }

/*        fun removeNotMainActivityWithStart(claz: Class<*>, bundle: Bundle?) {
            val intent = Intent(activityList[0], claz)
            if (bundle != null) {
                intent.putExtras(bundle)
            }
            activityList[0]?.startActivity(intent)
            for (activity in activityList) {
                if (!(activity is MainActivity) && activity?.javaClass != claz) {
                    activity?.finish()
                    Loge("removeNotMainActivityWithStart: " + activity?.getLocalClassName())
                }
            }
        }*/
    }

    fun isAvilible(packageName: String): Boolean {
        val packageNames = ArrayList<String>()
        this.packageManager?.getInstalledPackages(0)?.let {
            for (info: PackageInfo in it) {
                packageNames.add(info.packageName)
            }
        }
        return packageNames.contains(packageName)
    }

    //Multidex
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    fun getCurrentUserDaoSession(): DaoSession {
        return UserDBController.getInstance().daoSession
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        Account(this)
        Config(this)
        // 注册Activity的生命周期监听，用来控制退出的
        registerActivityLifecycleCallbacks(this)
        //初始化LeakCanary，用于测试内存泄漏
/*
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this)
*/

//        var user:User? = SpUtils.getInstance().readObject("user", User::class.java)

        UserDBController.getInstance().initBaseDB()
        UserDBController.getInstance().switchUser(User("vea",true))

        //初始化CommonLibrary
        CommonLibrary.instance.initLibrary(
            this,
            BuildConfig.APP_URL,
            ApiService::class.java,
            CacheService::class.java,
            spName = SP_NAME,
            errorHandleMap = hashMapOf<Int, (exception: IApiException) -> Unit>(90000 to { exception ->
                Account.signout(SIGNOUT_CODE_SINGLE)
            }, 90001 to { exception ->
                Account.signout(SIGNOUT_CODE_SINGLE)
            }, 8903 to { exception ->
                Account.signout(SIGNOUT_CODE_SINGLE)
            }, 8901 to { exception ->
                Account.signout(SIGNOUT_CODE_FORBIDDEN)
            }, 8902 to { exception ->
                Account.signout(SIGNOUT_CODE_FORBIDDEN)
            }, 9100 to { exception ->

            }),
            isDebug = BuildConfig.DEBUG,
            headerMap = hashMapOf("token" to SPUtils.getInstance(SP_NAME).getString("token", ""))
        )
    }


}