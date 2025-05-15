<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="./css/style.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="./js/jQuery.min.js"></script>
<title>簡易Twitter</title>
</head>
<body>
	<div class="main-contents">
		<%-- ヘッダー --%>
		<div class="header">
			<c:if test="${ empty loginUser }">
				<a href="login">ログイン</a>
				<a href="signup">登録する</a>
			</c:if>
			<c:if test="${ not empty loginUser }">
				<a href="./">ホーム</a>
				<a href="setting">設定</a>
				<a href="logout">ログアウト</a>
			</c:if>
		</div>

		<%-- 絞り込みボタン --%>
		<div>
			<form action="./" method="get">
				日付
				<input type="date" name="startDate" value="${startDate}">
				～
				<input type="date" name="endDate" value="${endDate}">
				<input type="submit" value="絞り込み">
			</form>
		</div>

	<%-- プロフィール --%>
		<c:if test="${ not empty loginUser }">
			<div class="profile">
				<div class="name">
					<h2>
						<c:out value="${loginUser.name}" />
					</h2>
				</div>
				<div class="account">
					<c:out value="${loginUser.account}" />
				</div>
				<div class="description">
					<c:out value="${loginUser.description}" />
				</div>
			</div>
		</c:if>
		<%-- エラーメッセージエリア --%>
		<c:if test="${ not empty errorMessages }">
			<div class="errorMessages">
				<ul>
					<c:forEach items="${errorMessages}" var="errorMessage">
						<li><c:out value="${errorMessage}" />
					</c:forEach>
				</ul>
			</div>
			<c:remove var="errorMessages" scope="session" />
		</c:if>

		<%-- つぶやきフォーム --%>
		<div class="form-area">
			<c:if test="${ isShowMessageForm }">
				<form action="message" method="post">
					いま、どうしてる？<br />
					<textarea name="text" cols="100" rows="5" class="tweet-box"></textarea>
					<br /> <input type="submit" value="つぶやく">（140文字まで）
				</form>
			</c:if>
		</div>

		<%-- つぶやきの一覧 --%>
		<div class="messages">
			<c:forEach items="${messages}" var="message">
				<div class="message">
					<div class="account-name">
						<span class="account">
							<a href="./?user_id=<c:out value="${message.userId}"/> ">
								<c:out	value="${message.account}" />
							</a>
						</span>
						<span class="name">
							<c:out value="${message.name}" />
						</span>
					</div>
					<div class="text">
						<pre><c:out value="${message.text}" /></pre>
					</div>
					<div class="date">
						<fmt:formatDate value="${message.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
					</div>
					<%-- つぶやきの編集 --%>
					<div>
						<c:if test="${loginUser.id == message.userId}">
							<form action="edit" method="get">
								<input name="editMessageId" value="${message.id}" type="hidden">
								<input type="submit" value="編集">
							</form>
							<%-- つぶやきの削除 --%>
							<form action="deleteMessage" method="post">
								<input name="deleteMessageId" value="${message.id}" type="hidden">
								<input type="submit" value="削除"><br />
							</form>
						</c:if>
					</div>
				</div>

				<%-- つぶやきの返信 --%>
				<div class="form-area">
					<c:if test="${ not empty loginUser }">
						<form action="comment" method="post">
							<textarea name="comment" cols="100" rows="5" class="tweet-box"></textarea>
							<input name="messageId" value="${message.id}" type="hidden">
							<br /> <input type="submit" value="返信">（140文字まで）
						</form>
					</c:if>
				</div>

				<%-- 返信の一覧 --%>
				<div class="comments">
					<c:forEach items="${comments}" var="comment">
						<c:if test="${ comment.messageId == message.id}">
							<div class="comment">
								<div class="account-name">
									<span class="account">
										<c:out	value="${comment.account}" />
									</span>
									<span class="name">
										<c:out value="${comment.name}" />
									</span>
								</div>
								<div class="text">
									<pre><c:out value="${comment.text}" /></pre>
								</div>
								<div class="date">
									<fmt:formatDate value="${comment.createdDate}" pattern="yyyy/MM/dd HH:mm:ss" />
								</div>
							</div>
						</c:if>
					</c:forEach>
				</div>

			</c:forEach>
		</div>
	</div>

	<div class="copyright">
		Copyright(c)Kohei_Nagano
	</div>
</body>
</html>