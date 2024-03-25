package com.liningkang.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.core.LogisticsCenter
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.launcher.ARouter



object RouteConfig {
    // service
    const val ROUTER_SERVICE_UI = "/ui/UiServiceImo"
    const val ROUTER_SERVICE_USERINFO = "/userinfo/UserInfoServiceImp"
    const val ROUTER_SERVICE_SPLASH = "/splash/SplashServiceImp"
    const val ROUTER_SERVICE_MAIN = "/main/MainServiceImp"
    const val ROUTER_SERVICE_NOTIFICATION = "/notification/NotificationServiceImp"

    // 音效播放组件提供的对外服务


    // activity
    const val ROUTER_ACTIVITY_MAIN = "/main/MainActivity"
    const val ROUTER_ACTIVITY_RECORD_START = "/main/RecordStartActivity"
    const val ROUTER_ACTIVITY_PARTNER_LIST = "/main/PartnerListActivity"

    const val ROUTER_ACTIVITY_ADD_PARTNER = "/main/AddPartnerActivity"
    const val ROUTER_ACTIVITY_ADD_TOY = "/main/AddToyActivity"
    const val ROUTER_ACTIVITY_ADD_WHERE = "/main/AddWhereActivity"
    const val ROUTER_ACTIVITY_TIMER = "/main/TimerActivity"
    const val ROUTER_ACTIVITY_TIMER_END = "/main/TimerEndActivity"
    const val ROUTER_ACTIVITY_SPLASH = "/splash/SplashActivity"
    const val ROUTER_ACTIVITY_INPUT_PASSWORD = "/splash/InputPasswordActivity"
    const val ROUTER_ACTIVITY_WELCOME = "/splash/WelcomeActivity"
    const val ROUTER_ACTIVITY_SET_LANGUAGE = "/splash/SetLanguageActivity"
    const val ROUTER_ACTIVITY_USER_GUIDE = "/splash/UserGuideActivity"
    const val ROUTER_ACTIVITY_SAVE_RESULT = "/splash/SaveResultActivity"
    const val ROUTER_ACTIVITY_PRIVACY_POLICY = "/splash/PrivacyPolicyActivity"
    const val ROUTER_ACTIVITY_FIRST_USER_LOAD = "/splash/FirstUserLoadActivity"
    const val ROUTER_ACTIVITY_EXPLAIN = "/splash/ExplainActivity"
    const val ROUTER_ACTIVITY_PRIVACY_EXPLAIN = "/splash/PrivacyExplainActivity"
    const val ROUTER_ACTIVITY_BASE_INFO = "/splash/BaseInfoActivity"
    const val ROUTER_ACTIVITY_SEX_TRAINING = "/splash/SexTrainingActivity"
    const val ROUTER_ACTIVITY_PASSWORD_LOCK = "/splash/PasswordLockActivity"
    const val ROUTER_ACTIVITY_MENSTRUAL_PERIOD = "/splash/MenstrualPeriodActivity"
    const val ROUTER_ACTIVITY_ADVICE = "/train/AdviceActivity"
    const val ROUTER_ACTIVITY_ADVICE_COMPLETE = "/train/AdviceCompleteActivity"
    const val ROUTER_ACTIVITY_KEGEL_EXERCISES = "/train/KegelExercisesActivity"
    const val ROUTER_ACTIVITY_BOX_BREATHING = "/train/BoxBreathingActivity"
    const val ROUTER_ACTIVITY_BREATH_HOLDING = "/train/BreathHoldingActivity"
    const val ROUTER_ACTIVITY_MINDFUL = "/train/MindfulActivity"
    const val ROUTER_ACTIVITY_LOADING = "/loading/LoadingActivity"
    const val ROUTER_ACTIVITY_ANALYSIS = "/report/AnalysisActivity"
    const val ROUTER_ACTIVITY_SETTING = "/setting/SettingActivity"
    const val ROUTER_ACTIVITY_INFORMATION = "/setting/InformationActivity"
    const val ROUTER_ACTIVITY_MENSTRUAL = "/setting/MenstrualActivity"
    const val ROUTER_ACTIVITY_PARTNER = "/setting/PartnerActivity"
    const val ROUTER_ACTIVITY_CROP = "/main/CropActivity"


    // fragment

    const val ROUTER_FRAGMENT_ACHIEVE = "/achieve/AchieveFragment"
    const val ROUTER_FRAGMENT_REPORT = "/report/ReportFragment"
    const val ROUTER_FRAGMENT_TRAIN = "/train/TrainFragment"
    const val ROUTER_FRAGMENT_RECORD = "/main/RecordFragment"



    fun startActivityForResult(
        activityContext: Context,
        path: String,
        params: HashMap<String, Any>? = null,
        requestCode: Int
    ) {
        val postcard = ARouter.getInstance().build(path)
        LogisticsCenter.completion(postcard)
        val destination = postcard.destination
        if (activityContext is Activity) {
            val intent = Intent(activityContext, destination).apply {
                params?.forEach { (t, u) ->
                    when (u) {
                        is String -> {
                            putExtra(t,u)
                        }
                        is Int->{
                            putExtra(t,u)
                        }
                        is Boolean->{
                            putExtra(t,u)
                        }
                        is Float->{
                            putExtra(t,u)
                        }
                        is Double->{
                            putExtra(t,u)
                        }
                        is Long->{
                            putExtra(t,u)
                        }
                        is Byte->{
                            putExtra(t,u)
                        }
                        is Char->{
                            putExtra(t,u)
                        }
                        is Short->{
                            putExtra(t,u)
                        }
                        is ByteArray->{
                            putExtra(t,u)
                        }

                    }
                }
            }

            activityContext.startActivityForResult(intent, requestCode)
        }
    }
}