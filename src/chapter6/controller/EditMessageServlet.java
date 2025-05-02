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

		// requestから編集したいメッセージのIDを取得し、int型に変換
		int editMessageId = Integer.parseInt(request.getParameter("editMessageId"));

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

		// 更新後のメッセージとメッセージIDを取得
		int updatedMessageId = Integer.parseInt(request.getParameter("updatedMessageId"));
		String updatedMessage = request.getParameter("updatedMessage");

		// 取得したものを引数にしてメッセージサービスに渡す
		new MessageService().update(updatedMessageId, updatedMessage);

		// バリデーションのため
        HttpSession session = request.getSession();
        List<String> errorMessages = new ArrayList<String>();

        if (!isValid(updatedMessage, errorMessages)) {
            session.setAttribute("errorMessages", errorMessages);
            response.sendRedirect("./");
            return;
        }

		response.sendRedirect("./");
	}

	// バリデーションも追加
    private boolean isValid(String updatedMessage, List<String> errorMessages) {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        if (StringUtils.isBlank(updatedMessage)) {
            errorMessages.add("メッセージを入力してください");
        } else if (140 < updatedMessage.length()) {
            errorMessages.add("140文字以下で入力してください");
        } else if (updatedMessage == updatedMessage) {
        	errorMessages.add("つぶやき内容を変更しない場合は「戻る」ボタンでお戻りください");
        }

        if (errorMessages.size() != 0) {
            return false;
        }
        return true;
    }

}