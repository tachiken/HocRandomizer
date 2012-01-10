package com.gmail.tachiken78.HocRandomizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * カードセット生成の履歴を表すデータクラス。
 * @author cosax
 *
 */
public class History implements Serializable {

	/** シリアルバージョン */
	private static final long serialVersionUID = 7301334895531459458L;

	/** 空履歴インスタンス */
	public static final History EMPTY = new History();

	private List<HoCCard> cardList;
	private String date;

	public History() {
		this.date = "";
		this.cardList = new ArrayList<HoCCard>(0);
	}

	public History(List<HoCCard> cardList) {
		this.date = "";
		this.cardList = cardList;
	}

	public History(String date, List<HoCCard> cardList) {
		this.date = date;
		this.cardList = cardList;
	}

	public void setCardList(List<HoCCard> cardList) {
		this.cardList = cardList;
	}

	public List<HoCCard> getCardList() {
		return cardList;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDate() {
		return date;
	}

	public String getStringForExternalApp() {
		return getString(",", false);
	}

	public String getStringForView() {
		return getString("\n", true);
	}

	private String getString(String delimiter, boolean addDate) {

		if (this == EMPTY) {
			return "no history!";
		}
		StringBuilder sb = new StringBuilder();
		if (addDate) {
			sb.append(date);
			sb.append(delimiter);
		}
		for (HoCCard card : cardList) {
			if (card == null) {
				return "";
			}
			sb.append(card.getName());
			sb.append(delimiter);
		}
		// 最終データの後ろにつけたデリミタは削除する
		sb.delete(sb.length()-delimiter.length(), sb.length());
		return sb.toString();
	}
}
