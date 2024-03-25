package com.example.languagelib;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;

import com.liningkang.utils.CommSharedUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

/**
 * 多语言切换的帮助类
 */
public class MultiLanguageUtil {

    private static final String TAG = "MultiLanguageUtil";
    private static MultiLanguageUtil instance;
    public static final String SAVE_LANGUAGE = "save_language";

    private static Locale mCurrentSystemLocal = Locale.ENGLISH;

    public static MultiLanguageUtil getInstance() {
        if (instance == null) {
            synchronized (MultiLanguageUtil.class) {
                if (instance == null) {
                    instance = new MultiLanguageUtil();
                }
            }
        }
        return instance;
    }

    private MultiLanguageUtil() {
    }

    /**
     * 如果不是英文、简体中文、繁体中文，默认返回简体中文
     *
     * @return
     */
    public Locale getLanguageLocale(Context context) {
        CommSharedUtil.INSTANCE.initialize(context);
        int languageType = CommSharedUtil.INSTANCE.getInt(MultiLanguageUtil.SAVE_LANGUAGE, 0);
        Log.i("MainApplication", "getLanguageLocale: 初始化语言"+languageType);
        Locale locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            //Locale sysLocale = mSystemCurrentLocal;
            locale = Resources.getSystem().getConfiguration().getLocales().get(0);
        } else if (languageType == LanguageType.LANGUAGE_EN) {
            locale = Locale.ENGLISH;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            locale = Locale.TRADITIONAL_CHINESE;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_西班牙语) {
            locale = new Locale("es", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_俄语) {
            locale = new Locale("ru", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_葡萄牙语) {
            locale = new Locale("pt", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_土耳其语) {
            locale = new Locale("tr", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_波兰语) {
            locale = new Locale("pl", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_法语) {
            locale = new Locale("fr", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_意大利语) {
            locale = new Locale("it", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_德语) {
            locale = new Locale("de", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_韩语) {
            locale = new Locale("ko", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_印尼语) {
            locale = new Locale("in", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_荷兰语) {
            locale = new Locale("nl", "");
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_日语) {
            locale = new Locale("ja", "");
        }

        // Log.e(TAG, "getLanguageLocale  " + getLanguage(locale));
        return locale;
    }

    private String getLanguage(Locale locale) {
        return locale.getLanguage() + "_" + locale.getCountry();
    }

    /**
     * 不要获取系统的Locale直接来判断系统语言，不同的rom获取的Locale会有差别,可以通过Locale的language字段来判断语言
     * 比如华为手机的中文语言下Locale为zh_CN_#Hans
     *
     * @return
     */
    public Locale getSysLocale() {
        return mCurrentSystemLocal;
    }

    public void saveSystemCurrentLanguage(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        mCurrentSystemLocal = locale;

    }

    /**
     * 更新语言
     *
     * @param context
     * @param languageType
     */
    public void updateLanguage(Context context, int languageType) {
        CommSharedUtil.INSTANCE.putInt(MultiLanguageUtil.SAVE_LANGUAGE, languageType);
        setConfiguration(context);
        EventBus.getDefault().post(new OnChangeLanguageEvent(languageType));
    }



    /**
     * 获取到用户保存的语言类型
     *
     * @return
     */
    public int getLanguageType(Context context) {
        int languageType = CommSharedUtil.INSTANCE.getInt(MultiLanguageUtil.SAVE_LANGUAGE, LanguageType.LANGUAGE_FOLLOW_SYSTEM);
        if (languageType == LanguageType.LANGUAGE_CHINESE_SIMPLIFIED) {
            return LanguageType.LANGUAGE_CHINESE_SIMPLIFIED;
        } else if (languageType == LanguageType.LANGUAGE_CHINESE_TRADITIONAL) {
            return LanguageType.LANGUAGE_CHINESE_TRADITIONAL;
        } else if (languageType == LanguageType.LANGUAGE_FOLLOW_SYSTEM) {
            return LanguageType.LANGUAGE_FOLLOW_SYSTEM;
        }
        Log.e(TAG, "getLanguageType  " + languageType);
        return languageType;
    }

    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context);
        } else {
            MultiLanguageUtil.getInstance().setConfiguration(context);
            return context;
        }
    }

    /**
     * 设置语言
     */
    public void setConfiguration(Context context) {
        if (context == null) {
            Log.e(TAG, "No context, MultiLanguageUtil will not work!");
            return;
        }
        Context appContext = context.getApplicationContext();
        Log.e(TAG, "setConfiguration " + context);
        Locale targetLocale = getLanguageLocale(appContext);
        Locale.setDefault(targetLocale);
        Configuration configuration = appContext.getResources().getConfiguration();
        configuration.setLocale(targetLocale);
        context.createConfigurationContext(configuration);
        Resources resources = appContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getInstance().getLanguageLocale(context);
        Log.d(TAG, "getLanguage ${getLanguage(locale)}");
        LocaleList localeList = new LocaleList(locale);
        LocaleList.setDefault(localeList);
        configuration.setLocales(localeList);
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    /**
     * 设置语言类型
     */
    public void setApplicationLanguage(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        Locale locale = getLanguageLocale(context);
        config.locale = locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList localeList = new LocaleList(locale);
            LocaleList.setDefault(localeList);
            config.setLocales(localeList);
            context.createConfigurationContext(config);
            Locale.setDefault(locale);
        }
        resources.updateConfiguration(config, dm);
    }
}
