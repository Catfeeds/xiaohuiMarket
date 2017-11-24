package com.zhumg.anlib.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class DatePickerSelectDialog {

    private Context context;
    private DatePickerDialog datePickerDialog;
    private DatePickerDialog.OnDateSetListener listener;
    //显示参数
    private boolean show_year = true;
    private boolean show_month = true;
    private boolean show_day = true;
    private StringBuilder sb = new StringBuilder();

    public DatePickerSelectDialog(Context context, DatePickerDialog.OnDateSetListener listener) {
        this.context = context;
        this.listener = listener;
    }

    private DatePickerDialog createDatePickerDialog(int year, int month, int day) {
        if(datePickerDialog == null) {
            datePickerDialog = new DatePickerDialog(context, null, year, month, day) {
                @Override
                public void onDateChanged(DatePicker view, int year, int month, int day) {
                    super.onDateChanged(view, year, month, day);
                    setTitle(year + "年" + (month + 1) + "月");
                }
            };
            DatePicker dp = datePickerDialog.getDatePicker();
            if(!show_year) {
                ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
            }
            if(!show_month) {
                ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
            }
            // liubolin 5/12修改
            // 代码出错  ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2) is null
            /*if(!show_day) {
                ((ViewGroup)((ViewGroup) dp.getChildAt(0)).getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            }*/
            //手动设置按钮
            datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //通过mDialog.getDatePicker()获得dialog上的DatePicker组件，然后可以获取日期信息
                    DatePicker datePicker = datePickerDialog.getDatePicker();
                    int year = datePicker.getYear();
                    int month = datePicker.getMonth();
                    int day = datePicker.getDayOfMonth();
                    listener.onDateSet(datePicker, year, month, day);
                }
            });
            //取消按钮，如果不需要直接不设置即可
            datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("BUTTON_NEGATIVE~~");
                }
            });
        }

        //初始时间
        datePickerDialog.setTitle(getTitle(year, month, day));
        return datePickerDialog;
    }

    private String getTitle(int year, int month, int day ) {
        sb.delete(0, sb.length());
        if(show_year) {
            sb.append(year).append("年");
        }
        if(show_month) {
            sb.append(month).append("月");
        }

        if(show_day) {
            sb.append(day).append("日");
        }
        return sb.toString();
    }

    public void show(int year, int month, int day) {

        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);

        if(year == 0) {
            year = yy;
        }else if(year == -1) {
            show_year = false;
        }
        if(month == 0) {
            month = mm;
        } else if(month == -1) {
            show_month = false;
        }
        if(day == 0) {
            day = dd;
        } else if(day == -1) {
            show_day = false;
        }

        createDatePickerDialog(year, month, day);
        datePickerDialog.show();
    }

    public void show(String data) {

        int year = 0;
        int month = 0;
        int day = 0;

        String ss[] = data.split("-");
        if(ss != null) {
            if(ss.length > 0) {
                year = Integer.parseInt(ss[0]);
            } else {
                year = -1;
            }
            if(ss.length > 1) {
                month = Integer.parseInt(ss[1]);
            } else {
                month = -1;
            }
            if(ss.length > 2) {
                day = Integer.parseInt(ss[2]);
            } else {
                day = -1;
            }
        }
        show(year, month, day);
    }
}
