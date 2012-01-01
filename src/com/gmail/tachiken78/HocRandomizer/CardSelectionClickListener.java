package com.gmail.tachiken78.HocRandomizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public abstract class CardSelectionClickListener implements OnClickListener {
	Context context;
	String cardnameList[];
	boolean flags[];
	boolean flagsBackup[];

	public CardSelectionClickListener(Context context, String[] cardnameList, boolean[] flags)
	{
		this.context = context;
		this.cardnameList = cardnameList;
		this.flags = flags;
	}

	abstract protected String getTitle();

	abstract protected boolean isCheckedCountVaild(int checkedCount);

	abstract protected String errorMessageForCheckedCount();

	public void onClick(View v) {
		this.flagsBackup = flags.clone();
		new AlertDialog.Builder(this.context)
		.setTitle(getTitle())
		.setMultiChoiceItems(cardnameList, flags,
				new DialogInterface.OnMultiChoiceClickListener(){
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				flags[which] = isChecked;
			}
		})
		.setPositiveButton("決定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int checkedCount = 0;
				for(boolean i : flags) {
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
					flags = flagsBackup.clone();
					return;
				}
				flagsBackup = null;
			}
		})
		.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				flags = flagsBackup.clone();
			}
		})
		.show();
	}
}
