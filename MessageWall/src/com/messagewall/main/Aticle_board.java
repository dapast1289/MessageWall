package com.messagewall.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Toast;

import com.messagewall.R;
import com.messagewall.adapter.ArticleAdapter;
import com.messagewall.core.Global;
import com.messagewall.core.User;
import com.messagewall.db.Article;
import com.messagewall.db.Msg;
import com.messagewall.json.JSONParser;

/*
 * 詳細文章內容
 * 及
 * 留言
 */

public class Aticle_board extends Fragment implements OnClickListener {

	private final static String tag = "Aticle_board.class";

	private Button leaveMsg;
	private ListView msgBoard;
	private Dialog dialog;
	private EditText input;
	public static ArticleAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.article_board, container, false);
		leaveMsg = (Button) view.findViewById(R.id.leave_message);
		leaveMsg.setOnClickListener(this);
		msgBoard = (ListView) view.findViewById(R.id.article_message);
		adapter = new ArticleAdapter(getActivity(), new Article());
		msgBoard.setAdapter(adapter);
		dialog = getLMsgDialog();
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leave_message:
			dialog.show();
			Toast.makeText(getActivity(), "留言", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	// 留言 Dialog
	public Dialog getLMsgDialog() {
		LayoutInflater factory = LayoutInflater.from(getActivity());
		View view = factory.inflate(R.layout.write_msg, null);
		input = (EditText) view.findViewById(R.id.input_msg);
		AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
		dialog.setTitle("留言");
		dialog.setView(view);
		dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String articlePid = "" + adapter.getArticle().getPid();
				String author = User.id;
				String msg = input.getText().toString().trim();
				input.setText("");
				new LeaveMsg(articlePid, author, msg).execute();
			}
		});
		return dialog.create();
	}

	// 留言thread
	public class LeaveMsg extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;
		String articlePid;
		String author;
		String msg;

		public LeaveMsg(String articlePid, String author, String msg) {
			this.articlePid = articlePid;
			this.author = author;
			this.msg = msg;
		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading..., Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... param) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			Log.d(tag, "articlePid = " + articlePid + "  author = " + author
					+ "  msg = " + msg);
			params.add(new BasicNameValuePair("article_pid", articlePid));
			params.add(new BasicNameValuePair("author", author));
			params.add(new BasicNameValuePair("msg", msg));
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					Global.URL_create_lmsg, "POST", params);
			// Log.d(tag, "result = " + json);
			pDialog.dismiss();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			new GetArticleMsg(articlePid, author).execute();
		}
	}

	// get留言thread
	public class GetArticleMsg extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;
		private String article_pid;
		private String author;

		public GetArticleMsg(String article_pid, String author) {
			this.article_pid = article_pid;
			this.author = author;
		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Loading..., Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... param) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("article_pid", article_pid));
			params.add(new BasicNameValuePair("author", author));
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					Global.URL_get_article_lmsg, "GET", params);
			Log.d(tag, "json = " + json.toString());
			pDialog.dismiss();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			String msg = null;
			boolean success = false;
			JSONArray Mmsg = null;
			ArrayList<Msg> list = new ArrayList<Msg>();
			try {
				success = result.getBoolean("success");
				msg = result.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (msg != null) {
				Toast.makeText(getActivity(), "" + msg, Toast.LENGTH_SHORT)
						.show();
			}
			if (success) {
				try {
					Mmsg = result.getJSONArray("lmsgss");
					for (int i = 0; i < Mmsg.length(); i++) {
						JSONObject c = Mmsg.getJSONObject(i);
						int pid = c.getInt("pid");
						int article_pid = c.getInt("article_pid");
						String author = c.getString("author");
						String LMSG = c.getString("msg");
						Msg mm = new Msg(pid, article_pid, author, LMSG);
						list.add(mm);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				adapter.setMsg(list);
			}
		}

	}

}
