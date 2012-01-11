package com.gmail.tachiken78.HocRandomizer;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnLongClickListener;

/**
 * 履歴ビューを長押しした際に実行されるクリックリスナーの実装クラス。
 * @author Tachiken
 *
 */
public class HistoryViewLongClickListener implements OnLongClickListener {
	private Activity parent;
	private List<History> historyData;

	public HistoryViewLongClickListener(Activity parent, List<History> history){
		this.parent = parent;
		this.historyData = history;
	}

	public boolean onLongClick(final View view) {
		final CharSequence[] items = {"Twitterでつぶやく", "その他のアプリと連携" };
		AlertDialog.Builder builder = new AlertDialog.Builder(parent)
			.setTitle("連携アプリ選択")
			.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Integer number = (Integer) view.getTag();
					String str = historyData.get(number).getStringForExternalApp();

					switch(which){
					// Twitterでつぶやく
					case 0:
						{
							Intent intent=new Intent("android.intent.action.VIEW",
									Uri.parse("http://twitter.com/intent/tweet?text=" + str + "&hashtags=hatokurandom"));
							parent.startActivity(intent);
						}
						break;
					// その他のアプリと連携
					case 1:
						{
							Intent intent = new Intent(android.content.Intent.ACTION_SEND);
							intent.setType("text/plain")
								.putExtra(Intent.EXTRA_TEXT, str);
							parent.startActivity(Intent.createChooser(intent, "カードセット情報の共有"));
						}
						break;
					}
				}
			});
		builder.create().show();
		return true;
	}
}
