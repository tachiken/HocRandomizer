package com.gmail.tachiken78.HocRandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HocRandomizerMainActivity extends Activity {
	boolean[] includeFlags;
	boolean[] includeFlagsBackup;
	boolean[] excludeFlags;
	boolean[] excludeFlagsBackup;


	/**
	 * 一回のプレーで選択するコモンカードの種類
	 */
	public static final int CHOICE_CARD_KINDS_NUMBER = 10;


	/**
	 * デフォルトコンストラクタ
	 */
	public HocRandomizerMainActivity(){
		includeFlags       = new boolean[CARD_LIST.length];
		includeFlagsBackup = new boolean[CARD_LIST.length];
		excludeFlags       = new boolean[CARD_LIST.length];
		excludeFlagsBackup = new boolean[CARD_LIST.length];
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 必須カード選択ボタンの設定を行う */
		Button button01 = (Button)findViewById(R.id.button_id_01);
		button01.setOnClickListener(new MandatorySelectionClickListener(this, cardnameList, includeFlags));

		/* 除外カード選択ボタンの設定を行う */
		Button button02 = (Button)findViewById(R.id.button_id_02);
		button02.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				excludeFlagsBackup = excludeFlags.clone();

				new AlertDialog.Builder(HocRandomizerMainActivity.this)
				.setTitle("絶対に使用しないカードをチェックしてください")
				.setMultiChoiceItems(cardnameList, excludeFlags,
						new DialogInterface.OnMultiChoiceClickListener(){
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						excludeFlags[which] = isChecked;
					}
				})
				.setPositiveButton("決定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int checkedCount = 0;
						for(boolean i : excludeFlags) {
							if(i) {
								checkedCount++;
							}
						}
						if(checkedCount > CARD_LIST.length - CHOICE_CARD_KINDS_NUMBER){
							new AlertDialog.Builder(HocRandomizerMainActivity.this)
							.setMessage("除外したあとの残りのカード種類が" + CHOICE_CARD_KINDS_NUMBER + "未満でした。\n選択した情報は失われます。")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
								}
							})
							.setCancelable(true)
							.show();
							excludeFlags = excludeFlagsBackup.clone();
							return;
						}
						excludeFlagsBackup = null;
					}
				})
				.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						excludeFlags = excludeFlagsBackup.clone();
					}
				})
				.show();
			}
		});

		/* デッキ生成ボタンの設定を行う */
		Button button03 = (Button)findViewById(R.id.button_id_03);
		button03.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int choosenCount = 0;
				HoCCard resultCards[] = new HoCCard[CHOICE_CARD_KINDS_NUMBER];
				String result = "";

				ArrayList<HoCCard> cardListWork = new ArrayList<HoCCard>(Arrays.asList(CARD_LIST));
				for(int cnt=CARD_LIST.length-1; cnt>=0; cnt--) {
					if(includeFlags[cnt]) {
						resultCards[choosenCount] = cardListWork.remove(cnt);
						choosenCount++;
						continue;
					}
					if(excludeFlags[cnt]) {
						cardListWork.remove(cnt);
						continue;
					}
				}
				java.util.Collections.shuffle(cardListWork);
				for(; choosenCount<CHOICE_CARD_KINDS_NUMBER; choosenCount++) {
					resultCards[choosenCount] = cardListWork.remove(0);
				}
				ArrayList<HoCCard> resultCardsList = new ArrayList<HoCCard>(Arrays.asList(resultCards));
				Collections.sort(resultCardsList, new Comparator<HoCCard>(){

					public int compare(HoCCard a, HoCCard b) {
						return a.cost - b.cost;
					}
				});
				for(HoCCard c : resultCardsList) {
					result += c + "\n";
				}

				new AlertDialog.Builder(HocRandomizerMainActivity.this)
				.setMessage(result)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})
				.setCancelable(true)
				.show();
			}
		});
	}

	static final HoCCard[] CARD_LIST;
	static String[] cardnameList;

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
		cardnameList = new String[CARD_LIST.length];
		for(int cnt=0; cnt<CARD_LIST.length; cnt++) {
			cardnameList[cnt] = CARD_LIST[cnt].getName();
		}
	}
}