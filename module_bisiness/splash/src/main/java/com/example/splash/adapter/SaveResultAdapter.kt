package com.example.splash.adapter

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import com.example.splash.R
import com.example.splash.data.SaveResultData
import com.example.splash.activity.SaveResultActivity
import com.example.splash.databinding.ItemSaveResultBinding
import com.hnquxing.home_main.common.BaseDatabindingRecyclerAdapter
import com.liningkang.base.DatabindingViewHolder

class SaveResultAdapter(context: Context) :
    BaseDatabindingRecyclerAdapter<ItemSaveResultBinding, SaveResultData>(context) {

    override fun getItemLayoutId(parent: ViewGroup,
                                 viewType: Int): Int {
        return R.layout.item_save_result
    }

    override fun onBind(holder: DatabindingViewHolder<ItemSaveResultBinding>, position: Int) {
        val saveResultData = dataList[position]
        val binding = holder.binding
        binding.contentText.text = saveResultData.text
        if (saveResultData.isLoading){
            binding.stateView.setBackgroundResource(com.liningkang.ui.R.drawable.loading)
            startLoadingAnimator(binding.stateView,saveResultData)
        }else{
            binding.stateView.setBackgroundResource(R.drawable.ic_complete)
        }
    }


    private fun startLoadingAnimator(view: View,data: SaveResultData) {
        //旋转
        //旋转
        var animator = ObjectAnimator.ofFloat(view, "rotation", 0f, 360f)
        animator?.duration = SaveResultActivity.animationDuration/3
        animator?.repeatMode = ObjectAnimator.RESTART
        animator?.repeatCount = 3
        animator?.interpolator = LinearInterpolator()
        animator.setAutoCancel(true)
        animator?.start()
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {

            }

            override fun onAnimationEnd(animation: Animator) {
                data.isLoading=false
                view.setBackgroundResource(R.drawable.ic_complete)
                animation.removeAllListeners()
                animator?.cancel()
                view.clearAnimation()

            }

            override fun onAnimationCancel(animation: Animator) {

            }

            override fun onAnimationRepeat(animation: Animator) {

            }

        })

    }
}