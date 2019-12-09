package com.blackflagbin.kcommon.http

import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSONObject
import com.blackflagbin.kcommon.facade.CommonLibrary
import com.blackflagbin.kcommon.utils.Base64
import com.blackflagbin.kcommon.utils.MD5Utils
import com.blackflagbin.kcommon.utils.RandomString
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.rx_cache2.internal.RxCache
import io.victoralbertos.jolyglot.GsonSpeaker
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by blackflagbin on 2017/9/11.
 */

class HttpProvider private constructor() {

    fun <P> provideApiService(
        baseUrl: String = CommonLibrary.instance.baseUrl,
        headerMap: Map<String, String>? = CommonLibrary.instance.headerMap
    ): P {

        return Retrofit.Builder().client(provideOkHttpClient(headerMap)).baseUrl(baseUrl).addConverterFactory(
            GsonConverterFactory.create()
        ).addCallAdapterFactory(
            RxJava2CallAdapterFactory.create()
        ).build().create(CommonLibrary.instance.apiClass) as P
    }

    fun <T> provideCacheService(): T {
        CommonLibrary.instance.context.cacheDir.setReadable(true)
        CommonLibrary.instance.context.cacheDir.setWritable(true)
        return RxCache.Builder().persistence(
            CommonLibrary.instance.context.cacheDir, GsonSpeaker()
        ).using(
            CommonLibrary.instance.cacheClass
        ) as T
    }

    private fun provideOkHttpClient(headerMap: Map<String, String>? = CommonLibrary.instance.headerMap): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = when (CommonLibrary.instance.isDebug) {
            true -> HttpLoggingInterceptor.Level.BODY
            else -> HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder().connectTimeout(
            DEFAULT_MILLISECONDS, TimeUnit.SECONDS
        ).readTimeout(
            DEFAULT_MILLISECONDS, TimeUnit.SECONDS
        ).addNetworkInterceptor { chain ->

            val jsonObject = JSONObject().apply {
                put("appType", "0")
                put("platformType", "1")
                put("versionCode", CommonLibrary.instance.versionCode)
//                put("uuid", getMyUUID())
            }
            val builder = chain.request().newBuilder()
            CommonLibrary.instance.headerMap?.forEach {
                builder.addHeader(it.key, it.value)
            }
            builder.addHeader("VersionInfo", Base64.encode(jsonObject.toString().toByteArray()))
            val nonceStr = RandomString(16).nextString()//随机字符串
            val utcStr = System.currentTimeMillis().toString()//时间戳
            builder.addHeader("nonceStr", nonceStr)
                .addHeader("utcStr", utcStr)
                .removeHeader("User-Agent")
            var request = builder.build()
            if (isFile) {
                val buffer = StringBuffer()
                buffer.append("nonceStr=").append(nonceStr).append("&")//随机字符串
                    .append("utcStr=").append(utcStr).append("&")//时间戳
                    .append("key=nomnomtechnehkcnskklwe33dsasdferek")//这是和后端约定好的key
                builder.addHeader("Sign", MD5Utils.md5(buffer.toString()).toUpperCase())
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .addHeader("user-agent", "pos")
                request = builder.build()
                isFile = false
            } else {
                val queryKeyNames = request.url().queryParameterNames()
                //进行排序
                val paramMap = TreeMap<String, String?>()
                val requestBody = request.body()
                if (requestBody != null) {
                    val bufferedSink = Buffer()
                    request.body()!!.writeTo(bufferedSink)
                    try {
                        val content = bufferedSink.readString(Charset.forName("UTF-8"))
                        Log.e("Tag", "intercept: $content")
                        val requestJsonObject = JSONObject.parseObject(content)
                        if (requestJsonObject != null) {
                            for (entry in requestJsonObject.entries) {
                                if (entry.value != null) {
                                    paramMap[entry.key] = entry.value.toString()
                                }
                            }
                        }
                    } catch (e: Exception) {
                    }
                }
                paramMap["nonceStr"] = nonceStr//随机字符串
                paramMap["utcStr"] = utcStr//时间戳
                for (s in queryKeyNames) {
                    Log.e("tag", "intercept: $s")
                    paramMap[s] = request.url().queryParameter(s)
                }
                val iterator = paramMap.entries.iterator()
                val buffer = StringBuffer()
                while (iterator.hasNext()) {
                    val entry = iterator.next() as java.util.Map.Entry<*, *>
                    val key = entry.key as String
                    val value = entry.value as String
                    if (!TextUtils.isEmpty(value)) {
                        buffer.append("$key=$value")
                        buffer.append("&")
                    }
                }
                buffer.append("key=nomnomtechnehkcnskklwe33dsasdferek")//这是和后端约定好的key
                builder.addHeader("Sign", MD5Utils.md5(buffer.toString()).toUpperCase())
                    .addHeader("Content-Type", "application/json;charset=utf-8")
                    .addHeader("user-agent", "pos")
                request = builder.build()
            }
            chain.proceed(request)
        }.addInterceptor(httpLoggingInterceptor).build()
    }

    private object InnerClass {
        internal var instance = HttpProvider()
    }

    companion object {

        var isFile: Boolean = false

        //默认超时时间
        const val DEFAULT_MILLISECONDS: Long = 30

        val instance: HttpProvider
            get() = InnerClass.instance
    }


}
