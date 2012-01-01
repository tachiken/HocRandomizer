package com.gmail.tachiken78.HocRandomizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class ExcludeSelectionClickListener implements OnClickListener {
	Context context;
	String[] cardnameList;
	boolean[] excludeFlags;
	boolean[] excludeFlagsBackup;

	public ExcludeSelectionClickListener(Context context, String[] cardnameList, boolean[] excludeFlags)
	{
		this.context = context;
		this.cardnameList = cardnameList;
		this.excludeFlags = excludeFlags;
	}

	public void onClick(View v) {
		excludeFlagsBackup = excludeFlags.clone();

		new AlertDialog.Builder(this.context)
		.setTitle("絶対に使用しないカードをチェックしてください")
		.setMultiChoiceItems(cardnameList, excludeFlags,
				new DialogInterface.OnMultiChoiceClickListener(){
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				excludeFlags[which] = isChecked;
			}
		})
		.setPositiveButton("決定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int checkedCount = 0;
				for(boolean i : excludeFlags) {
					if(i) {
						checkedCount++;
					}
				}
				if(checkedCount > HocRandomizerMainActivity.CARD_LIST.length - HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER){
					new AlertDialog.Builder(context)
					.setMessage("除外したあとの残りのカード種類が" + HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER + "未満でした。\n選択した情報は失われます。")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					})
					.setCancelable(true)
					.show();
					excludeFlags = excludeFlagsBackup.clone();
					return;
				}
				excludeFlagsBackup = null;
			}
		})
		.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				excludeFlags = excludeFlagsBackup.clone();
			}
		})
		.show();
	}
}
