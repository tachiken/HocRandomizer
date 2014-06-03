package com.gmail.tachiken78.HocRandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import com.gmail.tachiken78.HocRandomizer.util.Utility;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * カードセット生成ボタンを押した際の動作を実装するクラス。
 * @author Tachiken
 *
 */
public class CardSetGenerateClickListener implements OnClickListener {
	Context context;
	HistoryRegisterable register;
	LinkedHashMap<HoCCard, Boolean> includeFlags;
	LinkedHashMap<HoCCard, Boolean> excludeFlags;
	AvailableExpantionCheckInterface expantionCheck;

	public CardSetGenerateClickListener(HocRandomizerMainActivity parent, LinkedHashMap<HoCCard, Boolean> includeFlags, LinkedHashMap<HoCCard, Boolean> excludeFlags, AvailableExpantionCheckInterface checkInterface)
	{
		this.context = parent;
		this.register = parent;
		this.includeFlags = includeFlags;
		this.excludeFlags = excludeFlags;
		this.expantionCheck = checkInterface;
	}

	public void onClick(View v) {
		// 拡張が１つも選択されていない場合、何もしない
		if(!expantionCheck.existsSelectedExpantion()) return;

		int choosenCount = 0;
		HoCCard resultCards[] = new HoCCard[HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER];

		ArrayList<HoCCard> cardListWork = new ArrayList<HoCCard>(includeFlags.keySet());
		for(int cnt=includeFlags.size()-1; cnt>=0; cnt--) {
			if(includeFlags.get(cardListWork.get(cnt))) {
				resultCards[choosenCount] = cardListWork.remove(cnt);
				choosenCount++;
				continue;
			}
			if(excludeFlags.get(cardListWork.get(cnt))) {
				cardListWork.remove(cnt);
				continue;
			}
		}
		java.util.Collections.shuffle(cardListWork);
		for(; choosenCount<HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER; choosenCount++) {
			resultCards[choosenCount] = cardListWork.remove(0);
		}
		ArrayList<HoCCard> resultCardsList = new ArrayList<HoCCard>(Arrays.asList(resultCards));
		Collections.sort(resultCardsList, new Comparator<HoCCard>(){

			public int compare(HoCCard a, HoCCard b) {
				return a.getCost() - b.getCost();
			}
		});

		register.registHistory(resultCardsList, Utility.getDate());
		Toast.makeText(context, "カードセット情報を生成しました", Toast.LENGTH_SHORT).show();
	}
}
