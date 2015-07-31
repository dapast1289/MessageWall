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

/**
 * @author Leo
 * @description 看板畫面的Adapater
 */

public class KanBanAdapter extends BaseAdapter {

	private final static String tag = "KanBanAdapter.class";

	private LayoutInflater myInflater;
	private ArrayList<Article> mKanbanList;

	public KanBanAdapter(Context ctxt, ArrayList<Article> mKanbanList) {
		myInflater = LayoutInflater.from(ctxt);
		this.mKanbanList = mKanbanList;
	}

	public void setList(ArrayList<Article> mKanbanList) {
		this.mKanbanList = mKanbanList;
		notifyDataSetChanged();
	}

	public ArrayList<Article> getList() {
		return mKanbanList;
	}

	@Override
	public int getCount() {
		return mKanbanList.size();
	}

	public Article getItem(int position) {
		return mKanbanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Log.d(tag, "getView(int " + position + ")...");
		ViewTag viewTag = null;
		if (convertView == null) {
			convertView = myInflater.inflate(R.layout.kanban_adapter, null);
			// 建構view
			viewTag = new ViewTag(
					(TextView) convertView
							.findViewById(R.id.kanban_adapter_title),
					(TextView) convertView
							.findViewById(R.id.kanban_adapter_authur),
					(TextView) convertView
							.findViewById(R.id.kanban_adapter_popularity));
			// 設置容器內容
			convertView.setTag(viewTag);
		} else {
			viewTag = (ViewTag) convertView.getTag();
		}
		viewTag.title.setText("" + getItem(position).getTitle());
		viewTag.authur.setText(getItem(position).getPid() + "  "
				+ getItem(position).getAuthor());
		viewTag.popularity.setText("" + getItem(position).getPopularity());
		return convertView;
	}

	class ViewTag {
		TextView title;
		TextView authur;
		TextView popularity;

		public ViewTag(TextView title, TextView authur, TextView popularity) {
			this.title = title;
			this.authur = authur;
			this.popularity = popularity;
		}
	}

}
