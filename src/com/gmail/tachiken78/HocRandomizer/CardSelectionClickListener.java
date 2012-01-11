package com.gmail.tachiken78.HocRandomizer;

import java.util.LinkedHashMap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * カード選択機能を実装する抽象クラス。
 * カードリストとチェックボックスを並べて表示し、それらを通した
 * 選択状態の変更を司る。
 * @author Tachiken
 *
 */
public abstract class CardSelectionClickListener implements OnClickListener {
	Context context;
	Button resetButton;
	String cardnameList[];
	LinkedHashMap<HoCCard, Boolean> flags;
	LinkedHashMap<HoCCard, Boolean> flagsBackup;

	public CardSelectionClickListener(Context context, Button resetButton, String[] cardnameList, LinkedHashMap<HoCCard, Boolean> flags)
	{
		this.context = context;
		this.resetButton = resetButton;
		this.cardnameList = cardnameList;
		this.flags = flags;
		refreshEnablity();
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
				refreshEnablity();
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

	private void refreshEnablity(){
		// １つでも選択状態にある場合、リセットボタンを有効にする
		for(Boolean b : flags.values()){
			if(b){
				resetButton.setEnabled(true);
				return;
			}
		}
		resetButton.setEnabled(false);
	}
}
