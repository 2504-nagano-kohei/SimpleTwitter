// 「OK」を選択したらリンク先に遷移するメソッド
function jumpConfirm(obj) {
	// イベントが設定されたタグの情報を引数「this」で渡しており、タグの情報を使える
	// 「https://www.alhinc.jp/ に遷移しますか?」と確認ダイアログが表示される
	if (confirm("本当 に削除しますか?")) {
		return true;
	}
	return false;
}