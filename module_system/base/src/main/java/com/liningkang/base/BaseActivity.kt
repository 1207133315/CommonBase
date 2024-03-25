package com.liningkang.base

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle

import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewConfiguration
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.languagelib.MultiLanguageUtil
import com.liningkang.base.FastClick.isFastClick
import com.liningkang.common.IntentKeys
import com.liningkang.common.bean.EventData
import com.lky.toucheffectsmodule.factory.TouchEffectsFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe


/**
 * @description 所有基类Activity的基类,主要便于扩展,一般Activity都不继承本类,而是继承这个类的子类
 */
abstract class BaseActivity : AppCompatActivity(), OnClickListener {
    /**
     * 记录处于前台的Activity
     */
    private var mForegroundActivity: BaseActivity? = null
    protected var interstitialAdNumber = -1
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguageUtil.attachBaseContext(newBase))
        //app杀进程启动后会调用Activity attachBaseContext
        MultiLanguageUtil.getInstance().setConfiguration(newBase)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        window.setBackgroundDrawableResource(R.drawable.window_bg)
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.extras?.apply {
            interstitialAdNumber = getInt(IntentKeys.INTERSTITIAL_AD_NUMBER, interstitialAdNumber)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        //  StatusBarUtil.setTranslucent(this, 125)
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }
        AppManager.addActivity(this)
        intent.extras?.apply {
            interstitialAdNumber = getInt(IntentKeys.INTERSTITIAL_AD_NUMBER, interstitialAdNumber)
        }

        TouchEffectsFactory.initTouchEffects(this);

        /*      window.decorView.findViewById<View>(android.R.id.content)
                  .setPadding(0, 0, 0, getNavigationBarHeight());*/
        /*  val slide =  Slide();
          slide.duration = 3000;
          window.exitTransition = slide;//出去的动画
          window.enterTransition = slide;//进来的动画
          val explode =  Explode();
          explode.duration = 3000;
          window.exitTransition = explode;//出去的动画
          window.enterTransition = explode;//进来的动画
          val fade =  Fade();
          fade.duration = 3000;
          window.exitTransition = fade;//出去的动画
          window.enterTransition = fade;//进来的动画*/

        super.onCreate(savedInstanceState)
        transparentStatusBar(window)

    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            0,
            R.anim.activity_close_out_anim
        )
    }

    final override fun onClick(v: View?) {
        if (!FastClick.isFastClick()) {
            onViewClick(v)
        }
    }

    open fun onViewClick(v: View?) {

    }


    open fun transparentStatusBar(window: Window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            val vis = window.decorView.systemUiVisibility
            window.decorView.systemUiVisibility = option or vis
            window.statusBarColor = Color.parseColor("#1A000000")
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    private fun getNavigationBarHeight(): Int {

        val hasMenuKey = ViewConfiguration.get(this).hasPermanentMenuKey();

        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {

            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");

            //获取NavigationBar的高度
            return resources.getDimensionPixelSize(resourceId);

        } else {

            return 0;

        }

    }


    override fun onStart() {
        super.onStart()
        mForegroundActivity = this
    }

    @Subscribe
    fun event(eventData: EventData<String, Any>) {
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        AppManager.removeActivity(this)
    }

    /**
     * 获取当前处于前台的activity
     */
    fun getForegroundActivity(): BaseActivity? {
        return mForegroundActivity
    }

    fun setLayoutView(view: View) {
        if (view != null) {
            setContentView(view)
        }
    }

    /**
     * 设置layoutId
     *
     * @return
     */
    protected abstract fun getLayoutId(): Int

    /**
     * 初始化视图
     */
    protected abstract fun initView(savedInstanceState: Bundle?)


}