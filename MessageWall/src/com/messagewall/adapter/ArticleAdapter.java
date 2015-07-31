package com.messagewall.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.messagewall.R;
import com.messagewall.db.Article;
import com.messagewall.db.Msg;

public class ArticleAdapter extends BaseAdapter {

	// private final static String tag = "ArticleAdapter.class";

	private LayoutInflater myInflater;
	private Article mArticle;
	private ArrayList<Msg> mMsg;

	public ArticleAdapter(Context ctxt, Article mArticle) {
		myInflater = LayoutInflater.from(ctxt);
		this.mArticle = mArticle;
		this.mMsg = new ArrayList<Msg>();
	}

	public void setItem(Article mArticle) {
		this.mArticle = mArticle;
		notifyDataSetChanged();
	}
	
	public void setMsg(ArrayList<Msg> list){
		this.mMsg = list;
		notifyDataSetChanged();
	}
	
	public void init(){
		this.mArticle = new Article();
		this.mMsg = new ArrayList<Msg>();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return 2 + mMsg.size();
	}

	public Article getArticle() {
		return mArticle;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewTag viewTag = null;
		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.articl_content, null);
			viewTag = new ViewTag((TextView) convertView.findViewById(R.id.article_stream));
			convertView.setTag(viewTag);
		} else {
			viewTag = (ViewTag) convertView.getTag();
		}
		if (position == 0) {
			viewTag.content.setBackgroundResource(R.color.article_title);
			viewTag.content.setTextColor(android.graphics.Color.WHITE);
			viewTag.content.setText(mArticle.getTitle() + "\n作者: " + mArticle.getAuthor());
		} else if (position == 1) {
			viewTag.content.setBackgroundResource(R.color.black);
			viewTag.content.setTextColor(android.graphics.Color.WHITE);
			viewTag.content.setText("\n" + mArticle.getContent() + "\n\n");
		} else {
			viewTag.content.setBackgroundResource(R.color.black);
			viewTag.content.setTextColor(android.graphics.Color.YELLOW);
			viewTag.content.setText(mMsg.get(position-2).getAuthor()+": " + mMsg.get(position-2).getMsg());
		}
		return convertView;
	}

	class ViewTag {
		TextView content;

		public ViewTag(TextView content) {
			this.content = content;
		}
	}

}
