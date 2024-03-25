package com.liningkang.ui.view.picker_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.liningkang.ui.R;
import com.liningkang.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WheelMonthPicker extends WheelPicker implements IWheelMonthPicker {
    private int mSelectedMonth;

    public WheelMonthPicker(Context context) {
        this(context, null);
    }

    public WheelMonthPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WheelMonthPicker);
        int model = a.getInt(R.styleable.WheelMonthPicker_model, 0);
        if (model==0){
            List<Integer> data = new ArrayList<>();
            for (int i = 1; i <= 12; i++)
                data.add(i);
            super.setData(data);

        }else if (model==1){
            List<String> data = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM", context.getResources().getConfiguration().locale);
            for (int i = 1; i <= 12; i++) {
                Date date = new Date(0, i - 1, 1);
                data.add(sdf.format(date));
            }

            super.setData(data);
        }
        mSelectedMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;

        updateSelectedYear();
    }

    private void updateSelectedYear() {
        setSelectedItemPosition(mSelectedMonth-1,false);
    }

    @Override
    public void setData(List data) {
        throw new UnsupportedOperationException("You can not invoke setData in WheelMonthPicker");
    }

    @Override
    public int getSelectedMonth() {
        return mSelectedMonth;
    }

    @Override
    public void setSelectedMonth(int month) {
        mSelectedMonth = month;
        updateSelectedYear();
    }

    @Override
    public int getCurrentMonth() {
        return getCurrentItemPosition()+1;
    }
}