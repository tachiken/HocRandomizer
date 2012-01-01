package com.gmail.tachiken78.HocRandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class DeckGenerateClickListener implements OnClickListener {
	Context context;
	HistoryRegisterable register;
	LinkedHashMap<HoCCard, Boolean> includeFlags;
	LinkedHashMap<HoCCard, Boolean> excludeFlags;

	public DeckGenerateClickListener(HocRandomizerMainActivity parent, LinkedHashMap<HoCCard, Boolean> includeFlags, LinkedHashMap<HoCCard, Boolean> excludeFlags)
	{
		this.context = parent;
		this.register = parent;
		this.includeFlags = includeFlags;
		this.excludeFlags = excludeFlags;
	}

	public void onClick(View v) {
		int choosenCount = 0;
		HoCCard resultCards[] = new HoCCard[HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER];
		String result = "";

		ArrayList<HoCCard> cardListWork = new ArrayList<HoCCard>(Arrays.asList(HocRandomizerMainActivity.CARD_LIST));
		for(int cnt=HocRandomizerMainActivity.CARD_LIST.length-1; cnt>=0; cnt--) {
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
				return a.cost - b.cost;
			}
		});
		for(HoCCard c : resultCardsList) {
			result += c + "\n";
		}

		new AlertDialog.Builder(this.context)
		.setMessage(result)
		.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		})
		.setCancelable(true)
		.show();

		register.registHistory(result);
	}
}
