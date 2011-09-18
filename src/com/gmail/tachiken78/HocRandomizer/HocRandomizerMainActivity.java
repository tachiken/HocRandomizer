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
	final static int CHOICE_CARD_KINDS_NUMBER = 10;


	/**
	 * デフォルトコンストラクタ
	 */
	public HocRandomizerMainActivity(){
		include_flags = new boolean[card_list.length];
		include_flags_backup = new boolean[card_list.length];
		exclude_flags = new boolean[card_list.length];
		exclude_flags_backup = new boolean[card_list.length];
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		/* 必須カード選択ボタンの設定を行う */
		Button button01 = (Button)findViewById(R.id.button_id_01);
		button01.setOnClickListener(new View.OnClickListener() {
			@Override
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
			@Override
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
						if(checked_count > card_list.length - CHOICE_CARD_KINDS_NUMBER){
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
			@Override
			public void onClick(View v) {
				int choosen_count = 0;
				HoCCard result_cards[] = new HoCCard[CHOICE_CARD_KINDS_NUMBER];
				String result = "";

				ArrayList<HoCCard> card_list_work = new ArrayList<HoCCard>(Arrays.asList(card_list));
				for(int cnt=card_list.length-1; cnt>=0; cnt--) {
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
					@Override
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

	static final HoCCard[] card_list;
	static String[] cardname_list;

	static {
		card_list = new HoCCard[]{
				new HoCCard("寄付", 2),
				new HoCCard("早馬", 2),
				new HoCCard("願いの泉", 2),
				new HoCCard("城壁", 2),
				new HoCCard("斥候", 2),
				new HoCCard("召集令状", 3),
				new HoCCard("焼き畑農業", 3),
				new HoCCard("隠れ家", 3),
				new HoCCard("交易船", 3),
				new HoCCard("破城槌", 3),
				new HoCCard("買収工作", 3),
				new HoCCard("魔法の護符", 3),
				new HoCCard("埋もれた財宝", 3),
				new HoCCard("追いたてられた魔獣", 4),
				new HoCCard("シノビ", 4),
				new HoCCard("金貸し", 4),
				new HoCCard("図書館", 4),
				new HoCCard("星詠みの魔女", 4),
				new HoCCard("都市開発", 4),
				new HoCCard("歩兵大隊", 4),
				new HoCCard("補給部隊", 4),
				new HoCCard("魅了術の魔女", 4),
				new HoCCard("近衛騎士団", 5),
				new HoCCard("銀行", 5),
				new HoCCard("御用商人", 5),
				new HoCCard("皇帝領", 5),
				new HoCCard("呪詛の魔女", 5),
				new HoCCard("冒険者", 5),
				new HoCCard("錬金術師", 5),
				new HoCCard("噂好きの公爵夫人", 6),
		};
		cardname_list = new String[card_list.length];
		for(int cnt=0; cnt<card_list.length; cnt++) {
			cardname_list[cnt] = card_list[cnt].getName();
		}
	}
}