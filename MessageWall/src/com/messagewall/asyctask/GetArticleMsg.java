package com.messagewall.asyctask;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.messagewall.core.Global;
import com.messagewall.db.Lmsgs;
import com.messagewall.json.JSONParser;
import com.messagewall.main.Aticle_board;


/**
 * 
 * @author Leo
 * 取得文章底下所有留言
 *
 */
public class GetArticleMsg extends AsyncTask<String, String, JSONObject> {
	
	private final static String tag = "GetArticleMsg.class thread";

	private ProgressDialog pDialog;
	private Context mctxt;
	private String article_pid;

	public GetArticleMsg(Context mctxt, String article_pid) {
		Log.d(tag, "article_pid = " + article_pid);
		this.mctxt = mctxt;
		this.article_pid = article_pid;
	}

	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(mctxt);
		pDialog.setMessage("Loading..., Please wait...");
		pDialog.setCancelable(false);
		pDialog.show();
	}

	@Override
	protected JSONObject doInBackground(String... param) {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("article_pid", article_pid));
//		params.add(new BasicNameValuePair("author", author));
		JSONParser jsonParser = new JSONParser();
		JSONObject json = jsonParser.makeHttpRequest(Global.URL_get_article_lmsg, "GET", params);
		Log.d(tag, "json = " + json.toString());
		pDialog.dismiss();
		return json;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		String msg = null;
		boolean success = false;
		JSONArray Mmsg = null;
		ArrayList<Lmsgs> list = new ArrayList<Lmsgs>();
		try {
			success = result.getBoolean("success");
			msg = result.getString("message");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (msg != null) {
			Toast.makeText(mctxt, "" + msg, Toast.LENGTH_SHORT).show();
		}
		
		//抽取回傳的資料串
		if (success) {
			try {
				Mmsg = result.getJSONArray("lmsgss");
				for (int i = 0; i < Mmsg.length(); i++) {
					JSONObject c = Mmsg.getJSONObject(i);
					int pid = c.getInt("pid");
					int article_pid = c.getInt("article_pid");
					String author = c.getString("author");
					String LMSG = c.getString("msg");
					Lmsgs mm = new Lmsgs(pid, article_pid, author, LMSG);
					list.add(mm);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			Aticle_board.adapter.setMsg(list);
		}
	}

}
