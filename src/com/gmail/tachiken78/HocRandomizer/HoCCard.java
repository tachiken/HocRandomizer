package com.gmail.tachiken78.HocRandomizer;

import java.util.Comparator;

public class HoCCard implements Comparator<HoCCard> {
	private String name;
	private int cost;
	private Expantion expantion;

	/**
	 * コンストラクタ。
	 * @param cost コスト
	 * @param name カード名称
	 */
	public HoCCard(int cost, String name) {
		this(cost, name, Expantion.BASIC);
	}

	/**
	 * コンストラクタ。
	 * @param cost コスト
	 * @param name カード名称
	 * @param expantion 所属拡張セット
	 */
	public HoCCard(int cost, String name, Expantion expantion) {
		this.name = name;
		this.cost = cost;
		this.expantion = expantion;
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
	public Expantion getExpantion() {
		return expantion;
	}

	@Override
	public String toString() {
		return name;
	}

	public int compare(HoCCard arg0, HoCCard arg1) {
		return (arg0.cost - arg1.cost);
	}
}
