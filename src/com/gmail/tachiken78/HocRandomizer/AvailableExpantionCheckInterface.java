package com.gmail.tachiken78.HocRandomizer;

/**
 * 拡張（および基本）セットが１つ以上利用可能な状態にあるかどうかを確認するインタフェース。
 * @author Tachiken
 *
 */
public interface AvailableExpantionCheckInterface {
	/**
	 * 拡張（および基本）セットが１つ以上利用可能であるかどうかを返す。
	 * falseが返された場合、呼び出し元は直ちに処理を中断して元に戻ることが期待される。
	 * @return １つ以上利用可能である場合true
	 */
	boolean existsSelectedExpantion();
}
