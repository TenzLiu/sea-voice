package cn.sinata.xldutils.widget;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

public class MyDatePickerDialog extends DatePickerDialog {

	public MyDatePickerDialog(Context context, int theme,
			OnDateSetListener listener, int year, int monthOfYear,
			int dayOfMonth) {
		super(context, theme, listener, year, monthOfYear, dayOfMonth);
	}

	public MyDatePickerDialog(Context context, OnDateSetListener callBack,
			int year, int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	/**
	 * android api11以上有效
	 * @param minDate 最小时间
     */
	public void setMinDate(long minDate){
		if (Build.VERSION.SDK_INT>=11) {
			getDatePicker().setMinDate(minDate);
		}
	}
	/**
	 * android api11以上有效
	 * @param maxDate 最大时间
	 */
	public void setMaxDate(long maxDate){
		if (Build.VERSION.SDK_INT>=11) {
			getDatePicker().setMaxDate(maxDate);
		}
	}
}
