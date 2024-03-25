package com.example.splash.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.lifecycleScope

import com.alibaba.android.arouter.facade.annotation.Route
import com.example.splash.R
import com.liningkang.base.BaseCommonActivity
import com.liningkang.common.IntentKeys
import com.liningkang.common.RouteConfig
import kotlinx.android.synthetic.main.activity_privacy_policy.contentText
import kotlinx.android.synthetic.main.activity_privacy_policy.titleView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

@Route(path = RouteConfig.ROUTER_ACTIVITY_PRIVACY_POLICY)
class PrivacyPolicyActivity : BaseCommonActivity() {

    var from=0

    override fun getLayoutId(): Int {
       return R.layout.activity_privacy_policy
    }

    override fun initView(savedInstanceState: Bundle?) {
       from= intent.getIntExtra(IntentKeys.PRIVACY_FROM,from)
        if (from==IntentKeys.PRIVACY_FROM_SETTING){
            titleView.titleText=resources.getString(com.liningkang.ui.R.string.about_sex_tracker)
        }
        var inputStream:InputStream?=null
        try {
            lifecycleScope.launch(Dispatchers.IO){
                inputStream = assets.open(if (from==IntentKeys.PRIVACY_FROM_SETTING)"about.txt" else "privacy.txt")
                // 打开assets文件夹下的文件，返回InputStream对象
                // 读取文件内容
                var reader =  BufferedReader( InputStreamReader(inputStream))
                val stringBuilder =  StringBuilder()
                reader.lines().forEach {
                    stringBuilder.append(it).append("\n");
                }
                withContext(Dispatchers.Main){
                    contentText.text=stringBuilder.toString()
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }finally {
            // 关闭输入流
            inputStream?.close()
        }




    }
}