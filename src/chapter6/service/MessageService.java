package chapter6.service;

import static chapter6.utils.CloseableUtil.*;
import static chapter6.utils.DBUtil.*;

import java.sql.Connection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Message;
import chapter6.beans.UserMessage;
import chapter6.dao.MessageDao;
import chapter6.dao.UserMessageDao;
import chapter6.logging.InitApplication;

public class MessageService {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageService() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	// つぶやく
	public void insert(Message message) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName()
		+ " : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			new MessageDao().insert(connection, message);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	// つぶやきの削除
	public void delete(int deleteMessageId) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName()
		+ " : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;

		try {
			connection = getConnection();
			new MessageDao().delete(connection, deleteMessageId);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	// つぶやきの編集画面を呼び出す(ログイン中のユーザーのつぶやきのみ)
	public Message select(int editMessageId, int userId) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName()
		+ " : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			Message editMessage = new MessageDao().select(connection, editMessageId, userId);
			commit(connection);

			return editMessage;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	// つぶやきを編集（更新）
	public void update(Message editMessage) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName()
		+ " : " + new Object() {}.getClass().getEnclosingMethod().getName());

		Connection connection = null;
		try {
			connection = getConnection();
			// さらにconnectionを一緒に引数として渡す
			new MessageDao().update(connection, editMessage);
			commit(connection);
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}

	// メッセージ一覧を取得するコード
	public List<UserMessage> select(String userId) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		final int LIMIT_NUM = 1000;

		Connection connection = null;
		try {
			connection = getConnection();

			// 実践課題②　78行目まで
			Integer id = null;
			if (!StringUtils.isEmpty(userId)) {
				id = Integer.parseInt(userId);
			}
			List<UserMessage> messages = new UserMessageDao().select(connection, id, LIMIT_NUM);
			commit(connection);
			return messages;
		} catch (RuntimeException e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} catch (Error e) {
			rollback(connection);
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw e;
		} finally {
			close(connection);
		}
	}
}