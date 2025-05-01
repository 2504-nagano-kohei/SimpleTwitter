package chapter6.controller;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        int editMessageId = Integer.parseInt(request.getParameter("editMessageId"));

        Message message = new Message();
        message.setId(editMessageId);
        message.getId();

        new MessageService().displayEdit(editMessageId);
//        request.setAttribute(message, message);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }


    // つぶやきを編集し、更新ボタンが押されるとDB上のtextを上書き（更新）し、top.jspで表示させたい
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

	  log.info(new Object(){}.getClass().getEnclosingClass().getName() +
        " : " + new Object(){}.getClass().getEnclosingMethod().getName());

        int deleteMessageId = Integer.parseInt(request.getParameter("deleteMessageId"));

        Message message = new Message();
        message.setId(deleteMessageId);
        message.getId();

        new MessageService().delete(deleteMessageId );
        response.sendRedirect("./");
    }
}