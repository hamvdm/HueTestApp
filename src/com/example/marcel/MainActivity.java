package com.example.marcel;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends Activity {
	private TextView text;
	private EditText bridgeIP; 
	private SeekBar hue;
	private SeekBar sat;
	private SeekBar bri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy); 
		text = (TextView) findViewById(R.id.textView1);
		bridgeIP = (EditText) findViewById(R.id.editTextBridgeIP);
		bridgeIP.setText("192.168.0.190");
		SeekBar.OnSeekBarChangeListener l = new SeekBar.OnSeekBarChangeListener() {
	        @Override
	        public void onStopTrackingTouch(SeekBar seekBar) {
	        }
	        @Override
	        public void onStartTrackingTouch(SeekBar seekBar) {
	        }
	        @Override
	        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
	        	if(fromUser){
	        		try{
	        			JSONObject j = new JSONObject();
	        		    j.put("on", Boolean.valueOf(true));
	        		    j.put("bri", Integer.valueOf(bri.getProgress()));
	        		    j.put("hue", Integer.valueOf(hue.getProgress()));
	        		    j.put("sat", Integer.valueOf(sat.getProgress()));
	        		    String url = "http://"+bridgeIP.getText()+"/api/aValidUser/lights/1/state";
	        		    HttpResponse re = doPut(url, j);
	        			text.setText(EntityUtils.toString(re.getEntity())); 
	        		} catch (Exception e) {
	        			text.setText(e.toString());
	        		}
	        	}
	        }
	    };
		hue = (SeekBar) findViewById(R.id.seekBarHue);
		hue.setProgress(8000);
	    hue.setOnSeekBarChangeListener(l);
		sat = (SeekBar) findViewById(R.id.seekBarSat);
		sat.setProgress(255);
	    sat.setOnSeekBarChangeListener(l);
		bri = (SeekBar) findViewById(R.id.seekBarBri);
		bri.setProgress(255);
	    bri.setOnSeekBarChangeListener(l);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void button_Connect_OnClick(View view){
		try{
			JSONObject j = new JSONObject();
		    j.put("on", Boolean.valueOf(true));
		    j.put("bri", Integer.valueOf(255));
		    j.put("hue", Integer.valueOf(8000));
		    j.put("sat", Integer.valueOf(255));
		    String url = "http://"+bridgeIP.getText()+"/api/aValidUser/lights/1/state";
		    HttpResponse re = doPut(url, j);
			text.setText(EntityUtils.toString(re.getEntity())); 
			hue.setProgress(8000);
			sat.setProgress(255);
			bri.setProgress(255);
		} catch (Exception e) {
			text.setText(e.toString());
		}
	}
	
	public void buttonOffClick(View view){
		try{
			JSONObject j = new JSONObject();
		    j.put("on", Boolean.valueOf(false));
		    String url = "http://"+bridgeIP.getText()+"/api/aValidUser/lights/1/state";
		    HttpResponse re = doPut(url, j);
			text.setText(EntityUtils.toString(re.getEntity())); 
		} catch (Exception e) {
			text.setText(e.toString());
		}
	}
	
	public static HttpResponse doPut(String url, JSONObject c) throws ClientProtocolException, IOException 
	{
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPut request = new HttpPut(url);
	    HttpEntity entity;
	    StringEntity s = new StringEntity(c.toString());
	    s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	    entity = s;
	    request.setEntity(entity);
	    HttpResponse response;
	    response = httpclient.execute(request);
	    return response;
	}
}
