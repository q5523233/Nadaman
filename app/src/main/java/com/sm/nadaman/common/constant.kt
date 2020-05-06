package com.sm.nadaman.common

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.sm.nadaman.R
import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

const val ACTION_CHANGE_SELECT_USER = "ACTION_CHANGE_SELECT_USER"
const val ACTION_DISCONNECT_BLE = "ACTION_DISCONNECT_BLE"
const val ACTION_GET_HR = "ACTION_GET_HR"

const val ECG12_BACKLAYOUT_YNUMBER = 24 * 5
const val ECG2_BACKLAYOUT_YNUMBER = 10 * 5

const val REQUEST_ENABLE_BT = 1
const val REQUEST_CODE_OPEN_GPS = 2
const val REQUEST_CODE_PERMISSION_LOCATION = 3

//ecg2.0
const val ECG2_BLE_DEVICE_NAME = "VeanEcg2.0"
public val GET_ECG2_WAVEFORM_ON =
    byteArrayOf(0xfa.toByte(), 0xfa.toByte(), 0xfa.toByte(), 0x02, 0x02, 0x01, 0x03)
//ecg12.0
const val ECG12_BLE_DEVICE_NAME = "VeanEcg12.0"
public val GET_ECG12_WAVEFORM_ON = byteArrayOf(0xfa.toByte(), 0x02, 0x12, 0x01, 0x13)


//时间格式
val TIME_FORMAT_YMDHMS = "yyyy-MM-dd \nHH:mm:ss"

const val SP_NAME = "Nomnompos"
//选择图片
const val REQUEST_CODE_CHOOSE = 10001
//拍照
const val REQUEST_CODE_TAKE_PHOTO = 10002

const val LOGIN_CODE_PROFILE = 1000
const val LOGIN_CODE_TICKETS = 1001
const val LOGIN_CODE_PACKAGE = 1002
const val LOGIN_CODE_GUESTLIST = 1003

const val CODE_PASSWORD = 100

const val CODE_PAYMENT = 101

//單點登陸
const val SIGNOUT_CODE_SINGLE = 9001
//賬號禁用
const val SIGNOUT_CODE_FORBIDDEN = 9002

fun Loge(msg: String) {
    Log.e("cdd", msg)
}

fun <T> lazyFast(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)

fun <T> LiveData<T>.observeX(owner: LifecycleOwner, block: (obj: T) -> Unit) {
    observe(owner, Observer {
        block(it)
    })
}

fun getTime(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy").format(date)
}

fun Date.getFormatTime(format: String): String {
    return SimpleDateFormat(format).format(this)
}

fun getDateFromString(time: String): Date? {
    var parse = Date()

    val format = SimpleDateFormat("yyyy-MM-dd")
    try {
        parse = format.parse(time)
    } catch (mE: ParseException) {

    } finally {
        return parse

    }
}

//改变大小写
fun changeCapitals(et: EditText, start: Int, mSb: StringBuilder) {
    var c = mSb[start]
    if (c >= 'a' && c <= 'z') {
        c -= 32
        mSb.replace(start, start + 1, c + "")
        if (start + 1 < mSb.length) {
            var c1 = mSb[start + 1]
            if (c1 >= 'A' && c1 <= 'Z') {
                c1 += 32
                mSb.replace(start + 1, start + 2, c1 + "")
            }
        }
        et.setText(mSb)
        et.setSelection(start + 1)
    }
}

fun String.dateStringToLong(format: String = "dd/MM/yyyy"): Long {
    var parse = Date().time.div(1000)
    try {
        parse = SimpleDateFormat(format).parse(this).time.div(1000)
    } catch (mE: ParseException) {

    } finally {
        return parse
    }
}

fun CharSequence.dateStringToLong(format: String = "dd/MM/yyyy"): Long {
    var parse = Date().time.div(1000)
    try {
        parse = SimpleDateFormat(format).parse(this.toString()).time.div(1000)
    } catch (mE: ParseException) {

    } finally {
        return parse
    }
}


fun Fragment.finish() {
    findNavController().navigateUp()
}

fun NavController.navigate1(d: NavDirections) {
    try {
        navigate(d)
    } catch (e: Exception) {

    }

}


val gson by lazy { Gson() }

fun ImageView.loadImage(url: Int) {
    Glide.with(this).load(url).apply {

    }.into(this)
}

fun ImageView.loadCircleImage(url: String) {
    Glide.with(this).load(url).apply(
        RequestOptions().placeholder(R.mipmap.default_head).circleCrop()
    ).into(this)
}
fun stingToIntArr(arr: String, split: String): IntArray {
    val strings = arr.split(split.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val intArr = IntArray(strings.size)
    for (i in strings.indices) {
        intArr[i] = Integer.parseInt(strings[i])
    }
    return intArr
}
fun ImageView.loadImage(url: String?, default: Int = -1) {
    Glide.with(this).load(url).apply(
        RequestOptions().placeholder(default)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
    ).into(this)
}

fun ImageView.loadImageNoDefault(url: String) {
    Glide.with(this).load(url).apply(
        RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
    ).into(this)
}

fun Long.getTimeFormat(format: String = "hh:mm aa dd/MM/yyyy"): String {

    return SimpleDateFormat(format).format(Date(if (this < 1 * 1000000000000) this * 1000L else this))
}

fun Long.getDayOfMonth(): String {
    return Calendar.getInstance().let {
        it.timeInMillis = if (this < 1 * 1000000000000) this * 1000L else this
        String.format("%02d", it.get(Calendar.DAY_OF_MONTH))
    }
}

fun Long.getMonth(): Int {
    return Calendar.getInstance().let {
        it.timeInMillis = if (this < 1 * 1000000000000) this * 1000L else this
        it.get(Calendar.MONTH)
    }
}

fun EditText.addTextWatcher(
    afterChanged: ((s: Editable?) -> Unit)? = null,
    beforeChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
    onChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterChanged?.invoke(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            beforeChanged?.invoke(s, start, count, after)
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            onChanged?.invoke(s, start, before, count)
        }
    })
}

fun BigDecimal.setScaleForMoney(newScale: Int): String {
    return DecimalFormat("###,###.00").format(this.setScale(2))
}

fun getDayRange(year: Int, mouth: Int, day: Int): List<Long> {
    val list = ArrayList<Long>()
    val calendar = java.util.Calendar.getInstance()
    calendar.set(java.util.Calendar.YEAR, year)
    calendar.set(java.util.Calendar.MONTH, mouth)
    calendar.set(java.util.Calendar.DAY_OF_MONTH, day)
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    list.add(calendar.timeInMillis / 1000)
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 24)
    list.add(calendar.timeInMillis / 1000 - 1)
    return list
}

fun getDayRange(curDate: Calendar): List<Long> {
    val list = ArrayList<Long>()
    val calendar = java.util.Calendar.getInstance()
    calendar.set(java.util.Calendar.YEAR, curDate.get(Calendar.YEAR))
    calendar.set(java.util.Calendar.MONTH, curDate.get(Calendar.MONTH))
    calendar.set(java.util.Calendar.DAY_OF_MONTH, curDate.get(Calendar.DAY_OF_MONTH))
    calendar.set(java.util.Calendar.MILLISECOND, 0)
    calendar.set(java.util.Calendar.SECOND, 0)
    calendar.set(java.util.Calendar.MINUTE, 0)
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 0)
    list.add(calendar.timeInMillis / 1000)
    calendar.set(java.util.Calendar.HOUR_OF_DAY, 24)
    list.add(calendar.timeInMillis / 1000 - 1)
    return list
}


