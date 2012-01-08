package com.gmail.tachiken78.HocRandomizer;

import java.util.HashMap;
import java.util.Map;

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
