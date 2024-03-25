package com.example.splash.activity

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.languagelib.LanguageType
import com.example.languagelib.MultiLanguageUtil
import com.example.splash.adapter.LanguageAdapter
import com.example.splash.R
import com.example.splash.data.LanguageData
import com.example.splash.dialog.AgreementDialog
import com.liningkang.base.BaseCommonActivity
import com.liningkang.common.IntentKeys
import com.liningkang.common.RouteConfig
import kotlinx.android.synthetic.main.activity_set_language.recyclerView
import kotlinx.android.synthetic.main.activity_set_language.titleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.liningkang.utils.CommSharedUtil

@Route(path = RouteConfig.ROUTER_ACTIVITY_SET_LANGUAGE)
class SetLanguageActivity : BaseCommonActivity() {
    private val languageDataList = ArrayList<LanguageData>()
    private var selectData: LanguageData? = null
    private var type = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_set_language
    }

    override fun initView(savedInstanceState: Bundle?) {
        type = intent.getIntExtra(IntentKeys.LANGUAGE_FROM, IntentKeys.LANGUAGE_FROM_GUIDE)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val languageAdapter = LanguageAdapter(this)
        recyclerView.adapter = languageAdapter
        lifecycleScope.launch(Dispatchers.IO){
            languageDataList.add(
                LanguageData(
                    getString(com.liningkang.ui.R.string.跟随系统),
                    R.drawable.ic_general,
                    true,
                    LanguageType.LANGUAGE_FOLLOW_SYSTEM
                )
            )
            languageDataList.add(
                LanguageData(
                    getString(com.liningkang.ui.R.string.简体中文),
                    R.drawable.ic_zhongguo,
                    false,
                    LanguageType.LANGUAGE_CHINESE_SIMPLIFIED
                )
            )
            languageDataList.add(
                LanguageData(
                    getString(com.liningkang.ui.R.string.英语),
                    R.drawable.ic_meiguo,
                    false,
                    LanguageType.LANGUAGE_EN
                )
            )
            languageDataList.add(
                LanguageData(
                    getString(com.liningkang.ui.R.string.西班牙语),
                    R.drawable.ic_xibanya,
                    false,
                    LanguageType.LANGUAGE_CHINESE_西班牙语
                )
            )
            val currentLanguage = CommSharedUtil.getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM)
            for (data in languageDataList) {
                data.isChecked = data.type == currentLanguage
                if (data.isChecked) selectData = data
            }
            withContext(Dispatchers.Main){
                languageAdapter.dataList = languageDataList
                languageAdapter.notifyDataSetChanged()
            }
        }

        languageAdapter.setOnItemClickListener { data, position ->
           if (data?.isChecked==false){
               selectData = data
               for (itemData in languageAdapter.dataList) {
                   itemData.isChecked = itemData.type == data?.type
               }
               languageAdapter.notifyDataSetChanged()
           }

        }

        titleView.setOnSkipClickListener{
            MultiLanguageUtil.getInstance().updateLanguage(this@SetLanguageActivity,selectData!!.type)
            if (type==IntentKeys.LANGUAGE_FROM_GUIDE){
                AgreementDialog(this@SetLanguageActivity,type).show()
            }else{
                ARouter.getInstance().build(RouteConfig.ROUTER_ACTIVITY_WELCOME)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    .withString(IntentKeys.SPLASH_FROM,this@SetLanguageActivity.javaClass.simpleName)
                    .navigation(this)
            }
        }

    }
}