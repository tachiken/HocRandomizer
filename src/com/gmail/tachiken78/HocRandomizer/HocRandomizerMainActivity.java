package com.gmail.tachiken78.HocRandomizer;

import java.util.ArrayList;
import java.util.Arrays;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HocRandomizerMainActivity extends Activity {
	/** Called when the activity is first created. */
	String[] cardname_list = {
		"寄付",
		"早馬",
		"願いの泉",
		"城壁",
		"斥候",
		"召集令状",
		"焼き畑農業",
		"隠れ家",
		"交易船",
		"破城槌",
		"買収工作",
		"魔法の護符",
		"埋もれた財宝",
		"追いたてられた魔獣",
		"シノビ",
		"金貸し",
		"図書館",
		"星詠みの魔女",
		"都市開発",
		"歩兵大隊",
		"補給部隊",
		"魅了術の魔女",
		"近衛騎士団",
		"銀行",
		"御用商人",
		"皇帝領",
		"呪詛の魔女",
		"冒険者",
		"錬金術師",
		"噂好きの公爵夫人",
		};
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
		include_flags = new boolean[cardname_list.length];
		include_flags_backup = new boolean[cardname_list.length];
		exclude_flags = new boolean[cardname_list.length];
		exclude_flags_backup = new boolean[cardname_list.length];
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
        				if(checked_count > cardname_list.length - CHOICE_CARD_KINDS_NUMBER){
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
        		String result = "";

        		ArrayList<String> cardname_list_work = new ArrayList(Arrays.asList(cardname_list));
        		for(int cnt=cardname_list.length-1; cnt>=0; cnt--) {
        			if(include_flags[cnt]) {
        				choosen_count++;
        				result += cardname_list_work.get(cnt) + "\n";
        				cardname_list_work.remove(cnt);
        				continue;
        			}
        			if(exclude_flags[cnt]) {
        				cardname_list_work.remove(cnt);
        				continue;
        			}
        		}
        		java.util.Collections.shuffle(cardname_list_work);
        		for(int cnt=0; cnt<CHOICE_CARD_KINDS_NUMBER - choosen_count; cnt++) {
        			result += cardname_list_work.get(cnt) + "\n";
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
}