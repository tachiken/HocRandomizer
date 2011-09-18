package com.gmail.tachiken78.HocRandomizer;

import java.util.Comparator;

public class HoCCard implements Comparator<HoCCard> {
	String name;
	int cost;

	/**
	 * コンストラクタ。
	 * @param name カード名称
	 * @param cost コスト
	 */
	public HoCCard(String name, int cost) {
		this.name = name;
		this.cost = cost;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return name;
	}

	public int compare(HoCCard arg0, HoCCard arg1) {
		return (arg0.cost - arg1.cost);
	}
}
