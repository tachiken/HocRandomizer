package com.gmail.tachiken78.HocRandomizer;

public interface HistoryRegisterable {
	/**
	 * 生成したデッキデータの履歴を登録する。
	 * @param history 履歴情報
	 * @param addDate 日時情報を新たに付加するかどうか
	 */
	void registHistory(String history, boolean addDate);
}
