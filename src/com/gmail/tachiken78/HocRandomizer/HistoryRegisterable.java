package com.gmail.tachiken78.HocRandomizer;

import java.util.List;

public interface HistoryRegisterable {
	/**
	 * 生成したデッキデータの履歴を登録する。
	 * @param cardList カードリスト
	 * @param date 日付文字列
	 */
	void registHistory(List<HoCCard> cardList, String date);
}
