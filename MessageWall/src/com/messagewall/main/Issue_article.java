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
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.messagewall.R;
import com.messagewall.core.Global;
import com.messagewall.core.User;
import com.messagewall.json.JSONParser;

/**
 * @author Leo
 * @description 紀錄標題和內容，發表文章
 */

public class Issue_article extends Fragment implements OnClickListener {

	private final static String tag = "Issue_article.class";

	private Context mContext;
	private EditText article_title;
	private EditText article_content;
	private Button btn_issue2Server;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.i(tag, "onCreateView...");
		View view = inflater.inflate(R.layout.issue_article, container, false);
		article_title = (EditText) view.findViewById(R.id.edit_article_title);
		article_content = (EditText) view
				.findViewById(R.id.edit_article_content);
		btn_issue2Server = (Button) view.findViewById(R.id.btn_issue2Server);
		btn_issue2Server.setOnClickListener(this);
		this.mContext = getActivity();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_issue2Server:
			Log.d(tag, "你按了 傳送 按鈕");
			String title = article_title.getText().toString().trim();
			String content = article_content.getText().toString().trim();
			new IssueArticle(User.id, title, content).execute();
			article_title.setText("");
			article_content.setText("");
			break;
		}
	}

	
	// 發表文章Thread
	public class IssueArticle extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;
		private String author;
		private String title;
		private String content;

		public IssueArticle(String id, String title, String content) {
			Log.d(tag, "IssueArticle(String " + id + ", String " + title
					+ ", String " + content + ")");
			this.title = title;
			this.author = id;
			this.content = content;
		}

		@Override
		protected void onPreExecute() {
			Log.i(tag, "onPreExecute()");
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading..., Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... param) {
			Log.i(tag, "doInBackground()");
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("author", author));
			params.add(new BasicNameValuePair("title", title));
			params.add(new BasicNameValuePair("content", content));
			Log.d(tag, "list.add()");
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					Global.URL_create_article, "POST", params);
			Log.d(tag, "result = " + json);
			pDialog.dismiss();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			Log.i(tag, "onPostExecute()");
			String msg = null;
			// boolean success = false;
			try {
				// success = result.getBoolean("success");
				msg = result.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (msg != null) {
				Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
				Activity activity = getActivity();
				if (activity instanceof Kanban) {
					Kanban myactivity = (Kanban) activity;
					myactivity.fragment_kanban.setVisibility(View.VISIBLE);
					myactivity.fragment_issue.setVisibility(View.INVISIBLE);
					myactivity.onClick(myactivity.btn_reflesh);
				}
			}
		}

	}

}
