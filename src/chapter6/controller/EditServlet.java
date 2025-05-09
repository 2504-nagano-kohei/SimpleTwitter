package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	// 編集ボタンが押されたつぶやきの編集画面を呼び出したい
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<String>();

		// requestから編集したいメッセージのIDを取得
		String emId = request.getParameter("editMessageId");

		// URLを手動で入力した場合、MessageIdが数値かどうか（正規表現）、MessageIdが削除されていないかどうか（isBlank）
		if ((StringUtils.isBlank(emId)) || (!emId.matches("^[0-9]+$"))) {
			errorMessages.add("不正なパラメーターが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		// MessageIdがDB上に存在しているか（編集画面を呼び出す処理に追加）
		int editMessageId = Integer.parseInt(emId);
		Message editMessage = new MessageService().select(editMessageId);

		if (editMessage == null) {
			errorMessages.add("不正なパラメーターが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./");
			return;
		}

		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	// つぶやきを編集し、更新ボタンが押されるとDB上のtextを上書き（更新）し、top.jspで表示させたい
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		// 更新後のメッセージとメッセージIDを取得
		int updatedMessageId = Integer.parseInt(request.getParameter("updatedMessageId"));
		String updatedMessage = request.getParameter("updatedMessage");

		Message editMessage = new Message();
		editMessage.setText(updatedMessage);
		editMessage.setId(updatedMessageId);

		// メッセージのバリデーションのため
		List<String> errorMessages = new ArrayList<String>();

		if (!isValid(updatedMessage, errorMessages)) {
			request.setAttribute("errorMessages", errorMessages);
			request.setAttribute("editMessage", editMessage);
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return;
		}

		// 取得したものを引数にしてMessageServiceに渡す
		new MessageService().update(editMessage);

		response.sendRedirect("./");
	}

	// バリデーションも追加
	private boolean isValid(String updatedMessage, List<String> errorMessages) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		if (StringUtils.isBlank(updatedMessage)) {
			errorMessages.add("メッセージを入力してください");
		} else if (updatedMessage.length() > 140) {
			errorMessages.add("140文字以下で入力してください");
		}

		if (errorMessages.size() != 0) {
			return false;
		}
		return true;
	}

}