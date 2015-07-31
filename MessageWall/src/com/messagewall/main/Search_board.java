package com.messagewall.main;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.messagewall.R;
import com.messagewall.adapter.KanBanAdapter;
import com.messagewall.db.Article;

public class Search_board extends Fragment {

//	private final static String tag = "Search_board.class";

	private ListView search_final;
	public static KanBanAdapter adpater;
	public static TextView text;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.final_search, container, false);
		search_final = (ListView) view.findViewById(R.id.search_final);
		adpater = new KanBanAdapter(getActivity(), new ArrayList<Article>());
		search_final.setAdapter(adpater);
		text = (TextView) view.findViewById(R.id.search_title);
		return view;
	}

}
