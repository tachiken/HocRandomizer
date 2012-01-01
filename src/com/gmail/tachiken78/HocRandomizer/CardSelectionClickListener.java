package com.gmail.tachiken78.HocRandomizer;

import java.util.LinkedHashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class CardSelectionClickListener implements OnClickListener {
	Context context;
	String cardnameList[];
	LinkedHashMap<HoCCard, Boolean> flags;
	LinkedHashMap<HoCCard, Boolean> flagsBackup;

	public CardSelectionClickListener(Context context, String[] cardnameList, LinkedHashMap<HoCCard, Boolean> flags)
	{
		this.context = context;
		this.cardnameList = cardnameList;
		this.flags = flags;
	}

	abstract protected String getTitle();

	abstract protected boolean isCheckedCountVaild(int checkedCount);

	abstract protected String errorMessageForCheckedCount();

	@SuppressWarnings("unchecked")
	public void onClick(View v) {
		this.flagsBackup = (LinkedHashMap<HoCCard, Boolean>)flags.clone();
		final boolean[] flagsContainer = new boolean[flags.size()];
		int i=0;
		for(HoCCard card : flags.keySet()){
			flagsContainer[i] = flags.get(card);
			i++;
		}
		new AlertDialog.Builder(this.context)
		.setTitle(getTitle())
		.setMultiChoiceItems(cardnameList, flagsContainer,
				new DialogInterface.OnMultiChoiceClickListener(){
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				flagsContainer[which] = isChecked;
			}
		})
		.setPositiveButton("決定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int checkedCount = 0;
				for(boolean i : flagsContainer) {
					if(i) {
						checkedCount++;
					}
				}
				if(isCheckedCountVaild(checkedCount)){
					new AlertDialog.Builder(context)
					.setMessage(errorMessageForCheckedCount())
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					})
					.setCancelable(true)
					.show();
					flags.clear();
					flags.putAll(flagsBackup);
					flagsBackup = null;
					return;
				}
				int i=0;
				for(HoCCard card : flags.keySet()){
					flags.put(card, flagsContainer[i]);
					i++;
				}
				flagsBackup = null;
			}
		})
		.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				flags.clear();
				flags.putAll(flagsBackup);
				flagsBackup = null;
			}
		})
		.show();
	}
}
