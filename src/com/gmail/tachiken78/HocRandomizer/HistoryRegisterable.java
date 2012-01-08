package com.gmail.tachiken78.HocRandomizer;

import java.util.List;

public interface HistoryRegisterable {
	/**
	 * 生成したデッキデータの履歴を登録する。
	 * @param cardList カードリスト
	 * @param addDate 日時情報を新たに付加するかどうか
	 */
	void registHistory(List<HoCCard> cardList, boolean addDate);
}
