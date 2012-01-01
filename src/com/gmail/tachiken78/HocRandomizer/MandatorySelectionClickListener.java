package com.gmail.tachiken78.HocRandomizer;

import java.util.LinkedHashMap;

import android.content.Context;

public class MandatorySelectionClickListener extends CardSelectionClickListener {
	public MandatorySelectionClickListener(Context context, String[] cardnameList, LinkedHashMap<HoCCard, Boolean> includeFlags) {
		super(context, cardnameList, includeFlags);
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
