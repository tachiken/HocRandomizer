package com.gmail.tachiken78.HocRandomizer;

import java.util.LinkedHashMap;

import android.content.Context;
import android.widget.Button;

/**
 * 必須カードの選択機能を実装するクラス。
 * @author Tachiken
 *
 */
public class MandatorySelectionClickListener extends CardSelectionClickListener {
	public MandatorySelectionClickListener(Context context, Button resetButton, String[] cardnameList, LinkedHashMap<HoCCard, Boolean> includeFlags) {
		super(context, resetButton, cardnameList, includeFlags);
	}

	protected String getTitle() {
		return "必ず使用するカードをチェックしてください";
	}

	protected boolean isCheckedCountVaild(int checkedCount) {
		return checkedCount > HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER;
	}

	protected String errorMessageForCheckedCount() {
		return "選択したカードの種類が" + HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER + "を超えていました。\n選択した情報は失われます。";
	}
}
