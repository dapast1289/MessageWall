package com.messagewall.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.messagewall.R;
import com.messagewall.core.Global;
import com.messagewall.core.User;
import com.messagewall.json.JSONParser;

public class Login extends Activity implements OnClickListener {

	private final static String tag = "Login.java";
	private EditText id; // 帳號
	private EditText pasword; // 密碼

	private Button btn_login;
	private Button btn_login_guess;
	private Button btn_regist;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate()");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		mContext = this;
		id = (EditText) findViewById(R.id.members_id);
		pasword = (EditText) findViewById(R.id.members_pasword);
		id.setText("lulucat");
		pasword.setText("123456");
		btn_login = (Button) findViewById(R.id.btn_loging);
		btn_login.setOnClickListener(this);
		btn_login_guess = (Button) findViewById(R.id.btn_loging_guess);
		btn_login_guess.setOnClickListener(this);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_regist.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_loging:
			Log.d(tag, "onClick(View R.id.btn_loging)");
			String member_id = id.getText().toString().trim();
			String member_pasword = pasword.getText().toString().trim();
			new MemberLogin(member_id, member_pasword).execute();
			break;
		case R.id.btn_loging_guess:
			Log.d(tag, "onClick(View R.id.btn_loging_guess)");
			new MemberLogin("guess", "guess").execute();
			break;
		case R.id.btn_regist:
			Log.d(tag, "onClick(View R.id.btn_regist)");
			break;
		default:
			Log.d(tag, "onClick(default)");
			break;
		}
	}

	// 登入thread
	public class MemberLogin extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		String id = null;
		String pasword = null;

		public MemberLogin(String id, String pasword) {
			this.id = id;
			this.pasword = pasword;
		}

		@Override
		protected void onPreExecute() {
			Log.d(tag, "onPreExecute()");
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading..., Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... param) {
			Log.d(tag, "doInBackground()");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", id));
			params.add(new BasicNameValuePair("password", pasword));
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					Global.URL_query_member, "GET", params);
			pDialog.dismiss();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			Log.d(tag, "onPostExecute()");
			String msg = null;
			boolean success = false;
			try {
				success = result.getBoolean("success");
				msg = result.getString("message");
			} catch (JSONException e) {
				Toast.makeText(mContext, "網際網路沒有連結" + msg, Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
			if (msg != null) {
				Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
			}
			if (success) {
				// 登入成功
				User.id = id;
				Intent intent = new Intent();
				intent.setClass(Login.this, Kanban.class);
				startActivity(intent);
			}

		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Log.d(tag, "onCreateOptionsMenu()");
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(tag, "onOptionsItemSelected()");
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Log.d(tag, "id == R.id.action_settings");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
