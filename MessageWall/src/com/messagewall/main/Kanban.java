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
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.messagewall.R;
import com.messagewall.adapter.KanBanAdapter;
import com.messagewall.asyctask.GetArticleMsg;
import com.messagewall.core.Global;
import com.messagewall.core.User;
import com.messagewall.db.Article;
import com.messagewall.json.JSONParser;

public class Kanban extends FragmentActivity implements OnClickListener,
		OnItemClickListener {

	private final static String tag = "Kanban.class";

	private Context mContext;

	public TableLayout fragment_kanban;
	public TableLayout fragment_issue;
	public TableLayout fragment_article;
	public TableLayout fragment_search;

	private ListView kanbanView;
	private KanBanAdapter adapter;
	private Button btn_issue;
	private Button btn_search;
	public Button btn_reflesh;

	private Dialog dialog_search;
	private TextView title;
	private TextView authur;
	private TextView popularity;
	private EditText input;
	private int type; // 搜尋的類型

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(tag, "onCreate...");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.kanban);
		mContext = this;
		fragment_kanban = (TableLayout) findViewById(R.id.fragment_kanBan);
		Log.d(tag, "註冊fragment_issue");
		fragment_issue = (TableLayout) findViewById(R.id.fragment_issue);
		Log.d(tag, "註冊fragment_issue end");
		fragment_issue.setVisibility(View.INVISIBLE);
		fragment_article = (TableLayout) findViewById(R.id.fragment_article);
		fragment_article.setVisibility(View.INVISIBLE);
		fragment_search = (TableLayout) findViewById(R.id.fragment_search);
		fragment_search.setVisibility(View.INVISIBLE);
		kanbanView = (ListView) findViewById(R.id.kanbanBoard);
		adapter = new KanBanAdapter(this, new ArrayList<Article>());
		kanbanView.setAdapter(adapter);
		kanbanView.setOnItemClickListener(this);
		btn_issue = (Button) findViewById(R.id.btn_issue);
		btn_issue.setOnClickListener(this);
		btn_search = (Button) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);
		dialog_search = getSearchDialog();
		btn_reflesh = (Button) findViewById(R.id.btn_reflesh);
		btn_reflesh.setOnClickListener(this);

		// 獲取所有文章標題
		new GetAllArticle().execute();
	}

	// 搜尋 Dialog 介面
	public Dialog getSearchDialog() {
		Log.d(tag, "getSearchDialog()");
		LayoutInflater factory = LayoutInflater.from(this);
		View view = factory.inflate(R.layout.search_dialog, null);
		title = (TextView) view.findViewById(R.id.search_title);
		title.setOnClickListener(this);
		title.setBackgroundResource(R.color.selectColor);
		authur = (TextView) view.findViewById(R.id.search_authur);
		authur.setOnClickListener(this);
		popularity = (TextView) view.findViewById(R.id.search_popularity);
		popularity.setOnClickListener(this);
		type = R.id.search_title;
		input = (EditText) view.findViewById(R.id.search_edittext);
		AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.ThemeDialogCustom);
		dialog.setTitle("搜尋");
		dialog.setView(view);
		dialog.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				fragment_search.setVisibility(View.VISIBLE);
				fragment_kanban.setVisibility(View.INVISIBLE);
				String in = input.getText().toString().trim();
				Search_board.text.setText("搜尋   ");
				switch (type) {
				case R.id.search_title:
					Search_board.text.setText("搜尋   標題:" + in);
					break;
				case R.id.search_authur:
					Search_board.text.setText("搜尋   作者:" + in);
					break;
				case R.id.search_popularity:
					Search_board.text.setText("搜尋   流量:" + in);
					break;
				}
				new SearchArticle(in).execute();
				input.setText("");
			}
		});
		return dialog.create();
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_issue:
			Log.d(tag, "你按了發表文章");
			fragment_issue.setVisibility(View.VISIBLE);
			fragment_kanban.setVisibility(View.INVISIBLE);
			// Toast.makeText(this, "你按了發表文章", Toast.LENGTH_SHORT).show();
			break;
		case R.id.btn_search:
			dialog_search.show();
			// Toast.makeText(this, "你按了搜尋", Toast.LENGTH_SHORT).show();
			break;
		case R.id.search_title:
			type = R.id.search_title;
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			title.setBackgroundResource(R.color.selectColor);
			authur.setBackgroundResource(R.color.black);
			popularity.setBackgroundResource(R.color.black);
			// Toast.makeText(this, "標題", Toast.LENGTH_SHORT).show();
			break;
		case R.id.search_authur:
			type = R.id.search_authur;
			input.setInputType(InputType.TYPE_CLASS_TEXT);
			authur.setBackgroundResource(R.color.selectColor);
			title.setBackgroundResource(R.color.black);
			popularity.setBackgroundResource(R.color.black);
			// Toast.makeText(this, "作者", Toast.LENGTH_SHORT).show();
			break;
		case R.id.search_popularity:
			type = R.id.search_popularity;
			input.setInputType(InputType.TYPE_CLASS_DATETIME);
			popularity.setBackgroundResource(R.color.selectColor);
			title.setBackgroundResource(R.color.black);
			authur.setBackgroundResource(R.color.black);
			// Toast.makeText(this, "流量", Toast.LENGTH_SHORT).show();
			input.setText("");
			break;
		case R.id.btn_reflesh:
			new GetAllArticle().execute();
			break;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(tag, "onItemClick(AdapterView<?> " + parent + ", View " + view
				+ ", int " + position + ", long " + id + ")");
		fragment_article.setVisibility(View.VISIBLE);
		fragment_kanban.setVisibility(View.INVISIBLE);
		Aticle_board.adapter.init();
		Aticle_board.adapter.setItem(adapter.getItem(position));
		//抓取選取文章底下的留言串
		new GetArticleMsg(this, "" + adapter.getItem(position).getPid()).execute();

	}

	@Override
	public void onBackPressed() {
		if (fragment_issue.getVisibility() == View.VISIBLE
				|| fragment_article.getVisibility() == View.VISIBLE
				|| fragment_search.getVisibility() == View.VISIBLE) {
			fragment_issue.setVisibility(View.INVISIBLE);
			fragment_article.setVisibility(View.INVISIBLE);
			fragment_search.setVisibility(View.INVISIBLE);
			fragment_kanban.setVisibility(View.VISIBLE);
		} else {
			super.onBackPressed();
		}
	}

	// GetAllArticle Thread 獲取所有文章列表
	public class GetAllArticle extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;
		private final static String tag = "Kanban.class";

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
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					Global.URL_get_all_article, "GET", params);
			pDialog.dismiss();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			String msg = null;
			boolean success = false;
			JSONArray articles = null;
			ArrayList<Article> list = new ArrayList<Article>();
			try {
				success = result.getBoolean("success");
				msg = result.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (msg != null) {
				Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
			}
			if (success) {
				try {
					articles = result.getJSONArray("articles");
					for (int i = 0; i < articles.length(); i++) {
						JSONObject c = articles.getJSONObject(i);
						int pid = c.getInt("pid");
						String title = c.getString("title");
						String author = c.getString("author");
						int popularity = c.getInt("popularity");
						String content = c.getString("content");
						Article arc = new Article(pid, title, author,
								popularity, content);
						list.add(arc);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				adapter.setList(list);
			}
		}

	}

	// 搜尋AsycTask
	public class SearchArticle extends AsyncTask<String, String, JSONObject> {

		private ProgressDialog pDialog;

		private String titleOrauthor;

		public SearchArticle(String titleOrauthor) {
			this.titleOrauthor = titleOrauthor;
		}

		@Override
		protected void onPreExecute() {
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading..., Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... param) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			switch (type) {
			case R.id.search_title:
				Log.d(tag, "type = " + "search_title");
				params.add(new BasicNameValuePair("title", titleOrauthor));
				break;
			case R.id.search_authur:
				Log.d(tag, "type = " + "search_authur");
				params.add(new BasicNameValuePair("author", titleOrauthor));
				break;
			case R.id.search_popularity:
				Log.d(tag, "type = " + "search_popularity");
				params.add(new BasicNameValuePair("popularity", titleOrauthor));
				break;
			}
			JSONParser jsonParser = new JSONParser();
			JSONObject json = jsonParser.makeHttpRequest(
					Global.URL_search_article, "GET", params);
			Log.d(tag, "json = " + json.toString());
			pDialog.dismiss();
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			String msg = null;
			boolean success = false;
			JSONArray articles = null;
			ArrayList<Article> list = new ArrayList<Article>();
			try {
				success = result.getBoolean("success");
				msg = result.getString("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
			if (msg != null) {
			}
			if (success) {
				try {
					articles = result.getJSONArray("articles");
					for (int i = 0; i < articles.length(); i++) {
						JSONObject c = articles.getJSONObject(i);
						int pid = c.getInt("pid");
						String title = c.getString("title");
						String author = c.getString("author");
						int popularity = c.getInt("popularity");
						String content = c.getString("content");
						Article arc = new Article(pid, title, author,
								popularity, content);
						list.add(arc);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				Search_board.adpater.setList(new ArrayList<Article>());
				Search_board.adpater.setList(list);
			}

		}

	}

}
