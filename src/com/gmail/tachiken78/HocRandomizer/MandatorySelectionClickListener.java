package com.gmail.tachiken78.HocRandomizer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class MandatorySelectionClickListener implements OnClickListener {
	Context context;
	String cardnameList[];
	boolean includeFlags[];
	boolean includeFlagsBackup[];

	public MandatorySelectionClickListener(Context context, String[] cardnameList, boolean[] includeFlags)
	{
		this.context = context;
		this.cardnameList = cardnameList;
		this.includeFlags = includeFlags;
	}

	public void onClick(View v) {
		this.includeFlagsBackup = includeFlags.clone();
		new AlertDialog.Builder(this.context)
		.setTitle("必ず使用するカードをチェックしてください")
		.setMultiChoiceItems(cardnameList, includeFlags,
				new DialogInterface.OnMultiChoiceClickListener(){
			public void onClick(DialogInterface dialog, int which, boolean isChecked) {
				includeFlags[which] = isChecked;
			}
		})
		.setPositiveButton("決定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int checkedCount = 0;
				for(boolean i : includeFlags) {
					if(i) {
						checkedCount++;
					}
				}
				if(checkedCount > HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER){
					new AlertDialog.Builder(context)
					.setMessage("選択したカードの種類が" + HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER + "を超えていました。\n選択した情報は失われます。")
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
						}
					})
					.setCancelable(true)
					.show();
					includeFlags = includeFlagsBackup.clone();
					return;
				}
				includeFlagsBackup = null;
			}
		})
		.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				includeFlags = includeFlagsBackup.clone();
			}
		})
		.show();
	}
}
