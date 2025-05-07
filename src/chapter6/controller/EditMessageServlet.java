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
public class EditMessageServlet extends HttpServlet {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditMessageServlet() {
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
//
		// 打鍵テスト1回目：MessageIdが数値かどうか（正規表現） 、　MessageIdが削除されているかどうか（isBlank）
		if (!emId.matches("^[0-9]+$") || StringUtils.isBlank(emId)) {
			errorMessages.add("不正なパラメーターが入力されました") ;
			session.setAttribute("errorMessages", errorMessages);
            response.sendRedirect("./");
            return;
		}

		// MessageIdがDB上に存在しているか（int型に変換してからDB検索へ）
		int editMessageId = Integer.parseInt(emId);
		Message editMessage = new MessageService().searchId(editMessageId);

		Message editMessage = new MessageService().displayEdit(editMessageId);

		request.setAttribute("editMessage", editMessage);
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}



	// つぶやきを編集し、更新ボタンが押されるとDB上のtextを上書き（更新）し、top.jspで表示させたい
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		// 更新後のメッセージとメッセージID、更新前のメッセージを取得
		int updatedMessageId = Integer.parseInt(request.getParameter("updatedMessageId"));
		String updatedMessage = request.getParameter("updatedMessage");
		String beforUpdatedMessage = request.getParameter("beforUpdatedMessage");

		// 取得したものを引数にしてメッセージサービスに渡す
		new MessageService().update(updatedMessageId, updatedMessage);

		// バリデーションのため
        HttpSession session = request.getSession();
        List<String> errorMessages = new ArrayList<String>();

        if (!isValid(beforUpdatedMessage, updatedMessage, errorMessages)) {
            session.setAttribute("errorMessages", errorMessages);
            response.sendRedirect("./");
            return;
        }

		response.sendRedirect("./");
	}

	// バリデーションも追加
    private boolean isValid(String beforUpdatedMessage, String updatedMessage, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

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