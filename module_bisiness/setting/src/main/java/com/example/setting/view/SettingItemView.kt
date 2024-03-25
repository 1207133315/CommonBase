package com.example.setting.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.example.setting.R
import kotlinx.android.synthetic.main.item_setting.view.hintView
import kotlinx.android.synthetic.main.item_setting.view.itemNameText
import kotlinx.android.synthetic.main.item_setting.view.labelImage

class SettingItemView(context: Context, attrs: AttributeSet?, defStyleAttr:Int): FrameLayout(context,attrs,defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet):this(context,attrs,0)
    constructor(context: Context):this(context,null,0)
    companion object{
        const val INFORMATION=0
        const val PARTNER=1
        const val PASSWORD=2
        const val ABOUT=3
        const val LANGUAGE=4
        const val GOOGLE_PLAY=5
        const val TERM=6
        const val SHARE=7
        const val PRIVACY_POLICY=8
        const val FEATURES=9


    }
    init {
       var view= LayoutInflater.from(context).inflate(R.layout.item_setting,this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemView)
        var type = typedArray.getInteger(R.styleable.SettingItemView_itemType,0)
        typedArray.recycle()

        val resources = context.resources
        when(type){
            INFORMATION->{
                labelImage.setBackgroundResource(R.drawable.ic_information)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.personal_information)
            }
            PARTNER->{
                labelImage.setBackgroundResource(R.drawable.ic_label_partner)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.partner)
            }
            PASSWORD->{

                labelImage.setBackgroundResource(R.drawable.ic_screen_lock)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.privacy_password)
                hintView.setBackgroundResource(R.drawable.ic_security)
                hintView.visibility= View.VISIBLE
            }
            ABOUT->{
                labelImage.setBackgroundResource(R.drawable.ic_about)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.about_sex_tracker)
            }
            LANGUAGE->{
                labelImage.setBackgroundResource(R.drawable.ic_about)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.about_sex_tracker)
            }
            GOOGLE_PLAY->{
                labelImage.setBackgroundResource(R.drawable.ic_google_play)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.go_google_play_to_score)
            }
            SHARE->{
                labelImage.setBackgroundResource(R.drawable.ic_share)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.share)
            }
            PRIVACY_POLICY->{
                labelImage.setBackgroundResource(R.drawable.ic_privacy)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.privacy_policy)
            }
            TERM->{
                labelImage.setBackgroundResource(R.drawable.ic_term)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.term_of_service)
            }
            FEATURES->{
                labelImage.setBackgroundResource(R.drawable.ic_features)
                itemNameText.text=resources.getString(com.liningkang.ui.R.string.features)
                hintView.setBackgroundResource(R.drawable.ic_features_2)
                hintView.visibility= View.VISIBLE
            }

        }

    }
}