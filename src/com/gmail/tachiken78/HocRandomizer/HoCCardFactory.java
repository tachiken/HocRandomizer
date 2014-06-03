package com.gmail.tachiken78.HocRandomizer;

import java.util.HashMap;
import java.util.Map;

/**
 * カード一覧の管理および名称によるカード検索機能を提供するクラス。
 * @author cosax
 *
 */
public class HoCCardFactory {


	private static final HoCCard[] CARD_LIST = new HoCCard[] {
		new HoCCard(2, "寄付"),
		new HoCCard(2, "早馬"),
		new HoCCard(2, "願いの泉"),
		new HoCCard(2, "城壁"),
		new HoCCard(2, "斥候"),
		new HoCCard(3, "召集令状"),
		new HoCCard(3, "焼き畑農業"),
		new HoCCard(3, "隠れ家"),
		new HoCCard(3, "交易船"),
		new HoCCard(3, "破城槌"),
		new HoCCard(3, "買収工作"),
		new HoCCard(3, "魔法の護符"),
		new HoCCard(3, "埋もれた財宝"),
		new HoCCard(3, "御用商人"),
		new HoCCard(4, "追いたてられた魔獣"),
		new HoCCard(4, "シノビ"),
		new HoCCard(4, "金貸し"),
		new HoCCard(4, "図書館"),
		new HoCCard(4, "星詠みの魔女"),
		new HoCCard(4, "都市開発"),
		new HoCCard(4, "歩兵大隊"),
		new HoCCard(4, "補給部隊"),
		new HoCCard(4, "魅了術の魔女"),
		new HoCCard(5, "近衛騎士団"),
		new HoCCard(5, "銀行"),
		new HoCCard(5, "皇帝領"),
		new HoCCard(5, "呪詛の魔女"),
		new HoCCard(5, "冒険者"),
		new HoCCard(5, "錬金術師"),
		new HoCCard(6, "噂好きの公爵夫人"),
		new HoCCard(2, "お金好きの妖精", Expantion.SECOND),
		new HoCCard(3, "伝書鳩", Expantion.SECOND),
		new HoCCard(3, "貿易商人", Expantion.SECOND),
		new HoCCard(3, "弓兵隊", Expantion.SECOND),
		new HoCCard(3, "課税", Expantion.SECOND),
		new HoCCard(4, "クノイチ", Expantion.SECOND),
		new HoCCard(4, "サムライ", Expantion.SECOND),
		new HoCCard(4, "鉱山都市", Expantion.SECOND),
		new HoCCard(4, "港町", Expantion.SECOND),
		new HoCCard(4, "見習い魔女", Expantion.SECOND),
		new HoCCard(5, "結盟", Expantion.SECOND),
		new HoCCard(5, "割り符", Expantion.SECOND),
		new HoCCard(2, "ケットシー", Expantion.THIRD),
		new HoCCard(2, "幸運の銀貨", Expantion.THIRD),
		new HoCCard(3, "洗礼", Expantion.THIRD),
		new HoCCard(3, "呪いの人形", Expantion.THIRD),
		new HoCCard(3, "名馬", Expantion.THIRD),
		new HoCCard(4, "宮廷闘争", Expantion.THIRD),
		new HoCCard(4, "ドワーフの宝石職人", Expantion.THIRD),
		new HoCCard(4, "エルフの狙撃手", Expantion.THIRD),
		new HoCCard(5, "地方役人", Expantion.THIRD),
		new HoCCard(5, "豪商", Expantion.THIRD),
		new HoCCard(5, "貴族の一人娘", Expantion.THIRD),
		new HoCCard(6, "工業都市", Expantion.THIRD),
		new HoCCard(6, "独占", Expantion.THIRD),
		new HoCCard(2, "家守の精霊", Expantion.FOURTH),
		new HoCCard(2, "巡礼", Expantion.FOURTH),
		new HoCCard(2, "伝令", Expantion.FOURTH),
		new HoCCard(2, "春風の妖精", Expantion.FOURTH),
		new HoCCard(2, "密偵", Expantion.FOURTH),
		new HoCCard(3, "ギルドマスター", Expantion.FOURTH),
		new HoCCard(3, "司書", Expantion.FOURTH),
		new HoCCard(3, "祝福", Expantion.FOURTH),
		new HoCCard(3, "旅芸人", Expantion.FOURTH),
		new HoCCard(3, "星巫女の託宣", Expantion.FOURTH),
		new HoCCard(3, "リーフフェアリー", Expantion.FOURTH),
		new HoCCard(4, "石弓隊", Expantion.FOURTH),
		new HoCCard(4, "行商人", Expantion.FOURTH),
		new HoCCard(4, "検地役人", Expantion.FOURTH),
		new HoCCard(4, "御料地", Expantion.FOURTH),
		new HoCCard(4, "商船団", Expantion.FOURTH),
		new HoCCard(4, "大農園", Expantion.FOURTH),
		new HoCCard(4, "辻占い師", Expantion.FOURTH),
		new HoCCard(4, "ニンフ", Expantion.FOURTH),
		new HoCCard(4, "氷雪の精霊", Expantion.FOURTH),
		new HoCCard(4, "ブラウニー", Expantion.FOURTH),
		new HoCCard(5, "合併", Expantion.FOURTH),
		new HoCCard(5, "鬼族の戦士", Expantion.FOURTH),
		new HoCCard(5, "交易都市", Expantion.FOURTH),
		new HoCCard(5, "執事", Expantion.FOURTH),
		new HoCCard(5, "収穫祭", Expantion.FOURTH),
		new HoCCard(5, "聖堂騎士", Expantion.FOURTH),
		new HoCCard(5, "徴税人", Expantion.FOURTH),
		new HoCCard(5, "メイド長", Expantion.FOURTH),
		new HoCCard(6, "裁判官", Expantion.FOURTH),
		new HoCCard(2, "漁村", Expantion.FIFTH),
		new HoCCard(3, "いたずら妖精", Expantion.FIFTH),
		new HoCCard(3, "女学院", Expantion.FIFTH),
		new HoCCard(3, "へそくり", Expantion.FIFTH),
		new HoCCard(4, "開発命令", Expantion.FIFTH),
		new HoCCard(5, "学術都市", Expantion.FIFTH),
		new HoCCard(5, "十字軍", Expantion.FIFTH),
		new HoCCard(5, "転売屋", Expantion.FIFTH),
		new HoCCard(5, "独立都市", Expantion.FIFTH),
		new HoCCard(5, "ニンジャマスター", Expantion.FIFTH),
		new HoCCard(5, "砲兵部隊", Expantion.FIFTH),
		new HoCCard(4, "まじない師", Expantion.FIFTH),
		new HoCCard(4, "魔法のランプ", Expantion.FIFTH),
		new HoCCard(5, "免罪符", Expantion.FIFTH),
		new HoCCard(5, "傭兵団", Expantion.FIFTH),
		new HoCCard(12, "大公爵", Expantion.FIFTH),
	};

	private static final Map<String, HoCCard> cardNameMap = new HashMap<String, HoCCard>();
	static {
		for (HoCCard card : CARD_LIST) {
			cardNameMap.put(card.getName(), card);
		}
	}

	public static HoCCard get(String cardName) {
		return cardNameMap.get(cardName);
	}

	public static HoCCard[] getCardList() {
		return CARD_LIST;
	}
}
