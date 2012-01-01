package com.gmail.tachiken78.HocRandomizer;

import java.util.LinkedHashMap;

import com.gmail.tachiken78.HocRandomizer.R.id;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class HocRandomizerMainActivity extends Activity {
	LinkedHashMap<HoCCard, Boolean> includeFlags = new LinkedHashMap<HoCCard, Boolean>();
	LinkedHashMap<HoCCard, Boolean> excludeFlags = new LinkedHashMap<HoCCard, Boolean>();
	String[] cardnameList;

	/**
	 * 一回のプレーで選択するコモンカードの種類
	 */
	public static final int CHOICE_CARD_KINDS_NUMBER = 10;

	public HocRandomizerMainActivity(){
		for(HoCCard card : CARD_LIST){
			includeFlags.put(card, false);
			excludeFlags.put(card, false);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 拡張セット選択用チェックボックスの設定を行う */
		CheckBox checkBox = (CheckBox)findViewById(id.checkbox_id_01);
		checkBox.setText("極東辺境領を使用する");
		checkBox.setChecked(true);
		checkBox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				CheckBox me = (CheckBox)view;
				boolean checked = me.isChecked();
				for(HoCCard card : CARD_LIST){
					if(card.getExpantion() == Expantion.SECOND){
						if(checked){
							includeFlags.put(card, false);
							excludeFlags.put(card, false);
						} else {
							includeFlags.remove(card);
							excludeFlags.remove(card);
						}
					}
				}
				refreshClickListener();
			}
		});
		refreshClickListener();
	}

	private void refreshClickListener() {
		refreshCardnameList();

		/* 必須カード選択ボタンの設定を行う */
		Button button01 = (Button)findViewById(R.id.button_id_01);
		button01.setOnClickListener(new MandatorySelectionClickListener(this, cardnameList, includeFlags));

		/* 除外カード選択ボタンの設定を行う */
		Button button02 = (Button)findViewById(R.id.button_id_02);
		button02.setOnClickListener(new ExcludeSelectionClickListener(this, cardnameList, excludeFlags));

		/* デッキ生成ボタンの設定を行う */
		Button button03 = (Button)findViewById(R.id.button_id_03);
		button03.setOnClickListener(new DeckGenerateClickListener(this, includeFlags, excludeFlags));
	}

	private void refreshCardnameList() {
		// カード名リストの再生成を行う
		// COMMENT: 現状、必須カードリストと除外カードリストに含まれるカードは同一なため、その片方を
		// COMMENT: ベースにしてカード名リストを生成している。
		cardnameList = new String[includeFlags.size()];
		int cnt=0;
		for(HoCCard card : includeFlags.keySet()){
			cardnameList[cnt] = card.getName();
			cnt++;
		}
	}

	public static final HoCCard[] CARD_LIST;

	static {
		CARD_LIST = new HoCCard[]{
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
	}
}
