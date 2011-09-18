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
	boolean[] include_flags;
	boolean[] include_flags_backup;
	boolean[] exclude_flags;
	boolean[] exclude_flags_backup;


	/**
	 * 一回のプレーで選択するコモンカードの種類
	 */
	private static final int CHOICE_CARD_KINDS_NUMBER = 10;


	/**
	 * デフォルトコンストラクタ。
	 */
	public HocRandomizerMainActivity(){
		include_flags        = new boolean[CARD_LIST.length];
		include_flags_backup = new boolean[CARD_LIST.length];
		exclude_flags        = new boolean[CARD_LIST.length];
		exclude_flags_backup = new boolean[CARD_LIST.length];
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 必須カード選択ボタンの設定を行う */
		Button button01 = (Button)findViewById(R.id.button_id_01);
		button01.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				include_flags_backup = include_flags.clone();
				new AlertDialog.Builder(HocRandomizerMainActivity.this)
				.setTitle("必ず使用するカードをチェックしてください")
				.setMultiChoiceItems(cardname_list, include_flags,
						new DialogInterface.OnMultiChoiceClickListener(){
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						include_flags[which] = isChecked;
					}
				})
				.setPositiveButton("決定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int checked_count = 0;
						for(boolean i : include_flags) {
							if(i) {
								checked_count++;
							}
						}
						if(checked_count > CHOICE_CARD_KINDS_NUMBER){
							new AlertDialog.Builder(HocRandomizerMainActivity.this)
							.setMessage("選択したカードの種類が" + CHOICE_CARD_KINDS_NUMBER + "を超えていました。\n選択した情報は失われます。")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
								}
							})
							.setCancelable(true)
							.show();
							include_flags = include_flags_backup.clone();
							return;
						}
						include_flags_backup = null;
					}
				})
				.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						include_flags = include_flags_backup.clone();
					}
				})
				.show();
			}
		});

		/* 除外カード選択ボタンの設定を行う */
		Button button02 = (Button)findViewById(R.id.button_id_02);
		button02.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				exclude_flags_backup = exclude_flags.clone();
				new AlertDialog.Builder(HocRandomizerMainActivity.this)
				.setTitle("絶対に使用しないカードをチェックしてください")
				.setMultiChoiceItems(cardname_list, exclude_flags,
						new DialogInterface.OnMultiChoiceClickListener(){
					public void onClick(DialogInterface dialog, int which, boolean isChecked) {
						exclude_flags[which] = isChecked;
					}
				})
				.setPositiveButton("決定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						int checked_count = 0;
						for(boolean i : exclude_flags) {
							if(i) {
								checked_count++;
							}
						}
						if(checked_count > CARD_LIST.length - CHOICE_CARD_KINDS_NUMBER){
							new AlertDialog.Builder(HocRandomizerMainActivity.this)
							.setMessage("除外したあとの残りのカード種類が" + CHOICE_CARD_KINDS_NUMBER + "未満でした。\n選択した情報は失われます。")
							.setPositiveButton("OK", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
								}
							})
							.setCancelable(true)
							.show();
							exclude_flags = exclude_flags_backup.clone();
							return;
						}
						exclude_flags_backup = null;
					}
				})
				.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						exclude_flags = exclude_flags_backup.clone();
					}
				})
				.show();
			}
		});

		/* デッキ生成ボタンの設定を行う */
		Button button03 = (Button)findViewById(R.id.button_id_03);
		button03.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				int choosen_count = 0;
				HoCCard result_cards[] = new HoCCard[CHOICE_CARD_KINDS_NUMBER];
				String result = "";

				ArrayList<HoCCard> card_list_work = new ArrayList<HoCCard>(Arrays.asList(CARD_LIST));
				for(int cnt=CARD_LIST.length-1; cnt>=0; cnt--) {
					if(include_flags[cnt]) {
						result_cards[choosen_count] = card_list_work.remove(cnt);
						choosen_count++;
						continue;
					}
					if(exclude_flags[cnt]) {
						card_list_work.remove(cnt);
						continue;
					}
				}
				java.util.Collections.shuffle(card_list_work);
				for(; choosen_count<CHOICE_CARD_KINDS_NUMBER; choosen_count++) {
					result_cards[choosen_count] = card_list_work.remove(0);
				}
				ArrayList<HoCCard> result_cards_list = new ArrayList<HoCCard>(Arrays.asList(result_cards));
				Collections.sort(result_cards_list, new Comparator<HoCCard>(){

					public int compare(HoCCard a, HoCCard b) {
						return a.cost - b.cost;
					}
				});
				for(HoCCard c : result_cards_list) {
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
	static String[] cardname_list;

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
				new HoCCard(5, "御用商人"),
				new HoCCard(5, "皇帝領"),
				new HoCCard(5, "呪詛の魔女"),
				new HoCCard(5, "冒険者"),
				new HoCCard(5, "錬金術師"),
				new HoCCard(6, "噂好きの公爵夫人"),
		};
		cardname_list = new String[CARD_LIST.length];
		for(int cnt=0; cnt<CARD_LIST.length; cnt++) {
			cardname_list[cnt] = CARD_LIST[cnt].getName();
		}
	}
}