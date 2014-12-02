package com.azs.volleyjacksonrequestsample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.azs.vj.VJRequest;
import com.azs.volleyjacksonrequestsample.pojo.DateTime;
import com.fasterxml.jackson.core.type.TypeReference;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		VJRequest<DateTime> dateTimeRequest = new VJRequest<DateTime>(
				Method.GET, "http://date.jsontest.com",
				new TypeReference<DateTime>() {
				}, new Response.Listener<DateTime>() {

					@Override
					public void onResponse(DateTime response) {
						setTextViewData(R.id.millis, String.valueOf(response.getMillisSinceEpoch()));
						setTextViewData(R.id.date, response.getDate());
						setTextViewData(R.id.time, response.getTime());
					}
					
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						error.printStackTrace();
					}
					
				});
		
		Volley.newRequestQueue(this).add(dateTimeRequest);
	}
	
	private void setTextViewData(int id, String data) {
		((TextView) findViewById(id)).setText(data);
	}
}
