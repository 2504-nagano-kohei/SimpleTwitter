package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();
	}

	// つぶやく
	public void insert(Connection connection, Message message) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // text
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	// つぶやきの削除
	public void delete(Connection connection, int deleteMessageId) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append(" DELETE FROM messages WHERE id = ?"); // message_id

			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, deleteMessageId);

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	// つぶやきの編集（編集画面の呼び出し）
	public Message select(Connection connection, int editMessageId, int userId) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM messages WHERE id = ? AND user_id = ?"); // message_id

			ps = connection.prepareStatement(sql.toString());
			ps.setInt(1, editMessageId);
			ps.setInt(2, userId);
			ResultSet rs = ps.executeQuery();

			List<Message> messages = toMessages(rs);
			if (messages.isEmpty()) {
				return null;
			}

			return messages.get(0);
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	// つぶやきの編集（更新）
	public void update(Connection connection, Message editMessage) {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE messages SET");
			sql.append("    text = ?,"); // message
			sql.append("    updated_date = CURRENT_TIMESTAMP "); // 1回目コードレビュー時に追加
			sql.append("WHERE");
			sql.append("    id = ?"); // id

			ps = connection.prepareStatement(sql.toString());
			ps.setString(1, editMessage.getText());
			ps.setInt(2, editMessage.getId());
			ps.executeUpdate();

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	private List<Message> toMessages(ResultSet rs) throws SQLException {

		log.info(new Object() {}.getClass().getEnclosingClass().getName() +
		" : " + new Object() {}.getClass().getEnclosingMethod().getName());

		List<Message> messages = new ArrayList<Message>();

		try {
			while (rs.next()) {
				Message message = new Message();
				message.setId(rs.getInt("id"));
				message.setId(rs.getInt("user_id"));
				message.setText(rs.getString("text"));
				message.setCreatedDate(rs.getTimestamp("created_date"));
				message.setUpdatedDate(rs.getTimestamp("updated_date"));

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}
	}
}