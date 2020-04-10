package com.sm.nadaman.ui.fragment

import android.app.Activity.RESULT_OK
import android.text.TextUtils
import com.blackflagbin.kcommon.base.BaseFragment
import com.kennyc.view.MultiStateView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.sm.nadaman.R
import com.sm.nadaman.common.*
import com.sm.nadaman.common.bean.Friend
import com.sm.nadaman.common.db.FriendOpe
import com.sm.nadaman.common.event.onActivityResultEvent
import com.sm.nadaman.common.http.ApiService
import com.sm.nadaman.common.http.CacheService
import com.sm.nadaman.common.widget.PhotoDialog
import com.sm.nadaman.mvp.contract.AddFollowContract
import com.sm.nadaman.mvp.presenter.AddFollowPresenter
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.fragment_add_follow.*
import kotlinx.android.synthetic.main.fragment_add_follow.iv_head
import kotlinx.android.synthetic.main.item_follow.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.toast

class AddFollowFragment : BaseFragment<ApiService, CacheService, AddFollowPresenter, Any?>(),
    AddFollowContract.IAddFollowView {

    private val typeList by lazy {
        ArrayList<String>().apply {
            add("亲人")
            add("朋友")
            add("医生")
            add("其他")
        }
    }
    private val f: Friend by lazy {
        (arguments?.getSerializable("data") as Friend?) ?: Friend()
    }
    private val isEdit by lazy {
        AddFollowFragmentArgs.fromBundle(arguments).isEdit
    }

    private val photoDialog by lazy {
        PhotoDialog(context!!)
    }

    override val swipeRefreshView: SmartRefreshLayout?
        get() = null

    override val multiStateView: MultiStateView?
        get() = null

    override val layoutResId: Int
        get() = R.layout.fragment_add_follow

    override val presenter: AddFollowPresenter
        get() = AddFollowPresenter(this)

    override fun initView() {
        super.initView()
        if (Config.isSingleEcg.not()){
            toolbar.setBackgroundResource(R.mipmap.toolbar_12)
        }
        EventBus.getDefault().register(this)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        tv_title.text = if (isEdit) "编辑亲友" else "添加亲友"
        if (isEdit) {
            et_nickname.setText( f.nickName)
            iv_head.loadCircleImage(f.imgPath?:"")
            et_moblie.setText(f.phone)
            rg_type.check(
                when (f.flag) {
                    typeList[0] -> R.id.rb_family
                    typeList[1] -> R.id.rb_friend
                    typeList[2] -> R.id.rb_doctor
                    else -> R.id.rb_other
                }
            )
        }
        iv_head.setOnClickListener {
            photoDialog.show()
        }
        tv_save.setOnClickListener {
            val nickName = et_nickname.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(nickName)) {
                toast("昵称不能为空")
                return@setOnClickListener
            }
            val phone = et_moblie.text.toString().trim { it <= ' ' }
            if (TextUtils.isEmpty(phone)) {
                toast("手机号码不能为空")
                return@setOnClickListener
            }
            val PHONE_NUMBER_REG =
                "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$"
            if (!phone.matches(PHONE_NUMBER_REG.toRegex())) {
                toast("手机号码格式不正确")
                return@setOnClickListener
            }
            setFriend(nickName, phone)
            if (isEdit.not())
                FriendOpe.create().insert(f)
            else
                FriendOpe.create().update(f)
            finish()
        }
    }

    private fun setFriend(nickName: String, phone: String) {
        f.flag = when (rg_type.checkedRadioButtonId) {
            R.id.rb_family -> typeList[0]
            R.id.rb_friend -> typeList[1]
            R.id.rb_doctor -> typeList[2]
            else -> typeList[3]
        }
        f.nickName = nickName
        f.phone = phone
        f.imgPath = photoDialog.getTakePhoto()
        f.time = System.currentTimeMillis()
    }

    override fun initData() {

    }

    override fun showContentView(data: Any?) {

    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onActivityResultEvent(onActivityResultEvent: onActivityResultEvent) {
        onActivityResultEvent.apply {
            if (resultCode == RESULT_OK)
                when (requestCode) {
                    REQUEST_CODE_CHOOSE -> {
                        Matisse.obtainPathResult(data).get(0).let {
                            iv_head.loadCircleImage(it)
                            //TODO 上传图片
                            it
                        }
                    }
                    REQUEST_CODE_TAKE_PHOTO -> {
                        photoDialog.getTakePhoto()?.let {
                            iv_head.loadCircleImage(it)
                            it
                        }
                    }
                }
        }

    }

}
