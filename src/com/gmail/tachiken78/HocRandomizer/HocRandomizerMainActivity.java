package com.gmail.tachiken78.HocRandomizer;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import com.gmail.tachiken78.HocRandomizer.R.id;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HocRandomizerMainActivity extends Activity implements HistoryRegisterable {
	LinkedHashMap<HoCCard, Boolean> includeFlags = new LinkedHashMap<HoCCard, Boolean>();
	LinkedHashMap<HoCCard, Boolean> excludeFlags = new LinkedHashMap<HoCCard, Boolean>();
	String[] cardnameList;
	ArrayBlockingQueue<String> historyData = new ArrayBlockingQueue<String>(DEFAULT_HISTORY_MAX);
	Queue<String> historyQueue = historyData;
	SharedPreferences pref;

	/**
	 * 一回のプレーで選択するコモンカードの種類
	 */
	public static final int CHOICE_CARD_KINDS_NUMBER = 10;

	/**
	 * デフォルトの履歴保持サイズ
	 */
	private static final int DEFAULT_HISTORY_MAX = 10;

	private static final String HISTORY_PREFIX = "history";
	private static final String DEFAULT_HISTORY_MESSAGE = "no history";
	private static final int HISTORY_VIEW_WIDTH = 160;
	private static final int HISTORY_VIEW_HEIGHT = 200;

	public HocRandomizerMainActivity(){
		for(HoCCard card : CARD_LIST){
			includeFlags.put(card, false);
			excludeFlags.put(card, false);
		}
		// 履歴データの初期化
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			historyQueue.add("");
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
		prepareTextViewsForHistory();
		LoadHistoryFromLocal();
		refreshHistory();
		refreshClickListener();
	}

	public void onStop(){
		super.onStop();
		SaveHistoryToLocal();
	}

	public boolean onCreateOptionsMenu(android.view.Menu menu){
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch(item.getItemId()){
		case R.id.menu_dialog_id_01:
			onDeleteMenuSelected();
			break;
		case R.id.menu_dialog_id_02:
			Intent intent=new Intent("android.intent.action.VIEW",
				Uri.parse("http://twitter.com/intent/user?screen_name=HeartOfCrown"));
			startActivity(intent);
			break;
		}
		return true;
	}

	private void onDeleteMenuSelected(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("履歴の削除")
		.setMessage("全ての生成履歴を削除してよろしいですか？")
		.setPositiveButton("削除する", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				DeleteHistory();
			}
		})
		.setNegativeButton("キャンセル", null)
		.create()
		.show();
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

		/* 必須カードリセットボタンの設定を行う */
		Button button04 = (Button)findViewById(R.id.button_id_04);
		button04.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				for(HoCCard card : includeFlags.keySet()){
					includeFlags.put(card, false);
				}
			}
		});

		/* 除外カードリセットボタンの設定を行う */
		Button button05 = (Button)findViewById(R.id.button_id_05);
		button05.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				for(HoCCard card : excludeFlags.keySet()){
					excludeFlags.put(card, false);
				}
			}
		});
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

	public void registHistory(String history, boolean addDate) {
		historyQueue.poll();
		String entry = history;
		if(addDate){
			entry = getDate() + "\n" + history;
		}
		historyQueue.add(entry);

		// 履歴ビューに最新の生成結果を設定
		refreshHistory();
	}

	private String getDate(){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);

		return(String.format("%04d/%02d/%02d %02d:%02d:%02d",
				year, (month + 1), day, hour, minute, second));
	}

	private void refreshHistory(){
		HorizontalScrollView parent = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view_id_01);
		LinearLayout layout = (LinearLayout)parent.getChildAt(0);
		int tag = DEFAULT_HISTORY_MAX - 1;
		for(String s : historyData){
			TextView view = (TextView)layout.findViewWithTag(tag);
			if(null != view){
				view.setText(s);
			}
			tag--;
		}
	}

	private void preparePreference(){
		if(null == pref){
			pref = getPreferences(MODE_PRIVATE);
		}
	}

	private void SaveHistoryToLocal(){
		preparePreference();
		Editor editor = pref.edit();
		int i=0;
		for(String s : historyData){
			editor.putString(HISTORY_PREFIX + i, s);
			i++;
		}
		editor.commit();
	}

	private void LoadHistoryFromLocal(){
		preparePreference();
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			String s = pref.getString(HISTORY_PREFIX + i, DEFAULT_HISTORY_MESSAGE);
			registHistory(s, false);
		}
	}

	private void DeleteHistory(){
		preparePreference();
		Editor editor = pref.edit();
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			editor.putString(HISTORY_PREFIX + i, DEFAULT_HISTORY_MESSAGE);
		}
		editor.commit();
		LoadHistoryFromLocal();
	}

	private void prepareTextViewsForHistory(){
		HorizontalScrollView parent = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view_id_01);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		parent.addView(layout);
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			final TextView textView = new TextView(this);
			textView.setWidth(HISTORY_VIEW_WIDTH);
			textView.setHeight(HISTORY_VIEW_HEIGHT);
			textView.setTag(i);
			textView.setOnLongClickListener(new OnLongClickListener() {
				public boolean onLongClick(View view) {
					Intent intent = new Intent(android.content.Intent.ACTION_SEND);
					CharSequence str = textView.getText();
					intent.setType("text/plain")
						.putExtra(Intent.EXTRA_TEXT, str);
					startActivity(Intent.createChooser(intent, "デッキ情報の共有"));
					return true;
				}
			});
			layout.addView(textView);
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
