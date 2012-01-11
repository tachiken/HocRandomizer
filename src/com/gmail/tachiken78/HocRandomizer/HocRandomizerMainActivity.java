package com.gmail.tachiken78.HocRandomizer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gmail.tachiken78.HocRandomizer.R.id;

/**
 * 本アプリケーションのメインアクティビティクラス。
 * 各GUI部品の初期化などを行う。
 * @author Tachiken
 *
 */
public class HocRandomizerMainActivity extends Activity implements HistoryRegisterable {
	LinkedHashMap<HoCCard, Boolean> includeFlags = new LinkedHashMap<HoCCard, Boolean>();
	LinkedHashMap<HoCCard, Boolean> excludeFlags = new LinkedHashMap<HoCCard, Boolean>();
	String[] cardnameList;
	List<History> historyData = new ArrayList<History>(DEFAULT_HISTORY_MAX);
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
	private static final int HISTORY_VIEW_WIDTH = 320;
	private static final int HISTORY_VIEW_HEIGHT = 400;

	public HocRandomizerMainActivity(){
		for(HoCCard card : HoCCardFactory.getCardList()){
			includeFlags.put(card, false);
			excludeFlags.put(card, false);
		}
		// 履歴データの初期化
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			historyData.add(History.EMPTY);
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
				for(HoCCard card : HoCCardFactory.getCardList()){
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
		case R.id.menu_dialog_id_03:
			onHelpMenuSelected();
			break;
		case R.id.menu_dialog_id_04:
			onAboutMenuSelected();
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

	private void onHelpMenuSelected(){
		ViewGroup root = (ViewGroup)findViewById(R.id.layout_help_root);
		View layout = getLayoutInflater().inflate(R.layout.help, root);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("ヘルプ")
		.setView(layout)
		.setPositiveButton("閉じる", null)
		.create()
		.show();
	}

	private void onAboutMenuSelected(){
		ViewGroup root = (ViewGroup)findViewById(R.id.layout_about_root);
		View layout = getLayoutInflater().inflate(R.layout.about, root);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("このアプリについて")
		.setView(layout)
		.setPositiveButton("作者Twitter", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent=new Intent("android.intent.action.VIEW",
						Uri.parse("http://twitter.com/intent/user?screen_name=CoolTachiken"));
				startActivity(intent);
			}
		})
		.setNegativeButton("閉じる", null)
		.create()
		.show();

		// バージョン情報の設定
		PackageInfo info = null;
		try{
			info = getPackageManager().getPackageInfo("com.gmail.tachiken78.HocRandomizer", PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e){
			e.printStackTrace();
		}
		TextView view = (TextView)layout.findViewById(R.id.version_container);
		if(null != view){
			view.setText(info.versionName);
		}
	}

	private void refreshClickListener() {
		refreshCardnameList();

		/* 必須カード選択ボタンの設定を行う */
		Button button01 = (Button)findViewById(R.id.button_id_01);
		button01.setOnClickListener(new MandatorySelectionClickListener(this, cardnameList, includeFlags));

		/* 除外カード選択ボタンの設定を行う */
		Button button02 = (Button)findViewById(R.id.button_id_02);
		button02.setOnClickListener(new ExcludeSelectionClickListener(this, cardnameList, excludeFlags));

		/* カードセット生成ボタンの設定を行う */
		Button button03 = (Button)findViewById(R.id.button_id_03);
		button03.setOnClickListener(new CardSetGenerateClickListener(this, includeFlags, excludeFlags));

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

	public void registHistory(List<HoCCard> cardList, String date) {
		historyData.remove(0);
		History history = createHistory(cardList);
		if (date != null) {
			history.setDate(date);
		}
		historyData.add(history);

		// 履歴ビューに最新の生成結果を設定
		refreshHistory();
	}

	private void refreshHistory(){
		HorizontalScrollView parent = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view_id_01);
		LinearLayout layout = (LinearLayout)parent.getChildAt(0);
		int tag = 0;
		for(History history : historyData){
			TextView view = (TextView)layout.findViewWithTag(tag);
			if(null != view){
				view.setText(history.getStringForView());
			}
			tag++;
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
		int i = 0;
		for(History history : historyData){
			int k = 0;
			editor.putString(HISTORY_PREFIX + i + "_date", history.getDate());
			for (HoCCard card : history.getCardList()) {
				editor.putString(HISTORY_PREFIX + i + "_" + k, card.getName());
				k++;
			}
			i++;
		}
		editor.commit();
	}

	private void LoadHistoryFromLocal(){
		preparePreference();
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			List<HoCCard> cardList = new ArrayList<HoCCard>(CHOICE_CARD_KINDS_NUMBER);

			String date = pref.getString(HISTORY_PREFIX + i + "_date", null);
			for (int k = 0; k < CHOICE_CARD_KINDS_NUMBER; k++) {
				String cardName = pref.getString(HISTORY_PREFIX + i + "_" + k, DEFAULT_HISTORY_MESSAGE);
				cardList.add(HoCCardFactory.get(cardName));
			}
			registHistory(cardList, date);
		}
	}

	private void DeleteHistory(){
		preparePreference();
		Editor editor = pref.edit();
		for(int i=0; i<DEFAULT_HISTORY_MAX; i++){
			for (int k = 0; k < CHOICE_CARD_KINDS_NUMBER; k++) {
				editor.remove(HISTORY_PREFIX + i + "_" + k);
			}
		}
		editor.commit();
		LoadHistoryFromLocal();
	}

	private void prepareTextViewsForHistory(){
		HorizontalScrollView parent = (HorizontalScrollView)findViewById(R.id.horizontal_scroll_view_id_01);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		parent.addView(layout);
		for (int i = DEFAULT_HISTORY_MAX - 1; i >= 0; i--) {
			final TextView textView = new TextView(this);
			textView.setWidth(HISTORY_VIEW_WIDTH);
			textView.setHeight(HISTORY_VIEW_HEIGHT);
			textView.setTag(i);
			textView.setOnLongClickListener(new HistoryViewLongClickListener(this, historyData));
			layout.addView(textView);
		}
	}

	private History createHistory(List<HoCCard> cardList) {
		if (cardList == null || cardList.isEmpty() || cardList.contains(null)) {
			return History.EMPTY;
		}
		return new History(cardList);
	}
}
