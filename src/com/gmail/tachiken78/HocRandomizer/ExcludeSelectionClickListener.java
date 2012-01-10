package com.gmail.tachiken78.HocRandomizer;

import java.util.LinkedHashMap;

import android.content.Context;

/**
 * 除外カードの選択機能を実装するクラス。
 * @author Tachiken
 *
 */
public class ExcludeSelectionClickListener extends CardSelectionClickListener {
	public ExcludeSelectionClickListener(Context context, String[] cardnameList, LinkedHashMap<HoCCard, Boolean> excludeFlags) {
		super(context, cardnameList, excludeFlags);
	}

	protected String getTitle() {
		return "絶対に使用しないカードをチェックしてください";
	}

	protected boolean isCheckedCountVaild(int checkedCount) {
		return checkedCount > HoCCardFactory.getCardList().length - HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER;
	}

	protected String errorMessageForCheckedCount() {
		return "除外したあとの残りのカード種類が" + HocRandomizerMainActivity.CHOICE_CARD_KINDS_NUMBER + "未満でした。\n選択した情報は失われます。";
	}
}
