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
<title>つぶやきの編集</title>
</head>
<body>

	<div class="main-contents">

		<div class="header">
			<a href="./">ホーム</a>
			<a href="setting">設定</a>
			<a href="logout">ログアウト</a>
		</div>


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


		<div class="form-area">
			<form action="edit" method="post">
				<textarea name="updatedMessage" cols="100" rows="5" class="tweet-box"><c:out value="${editMessage.text}" /></textarea><br/>
				<input name="updatedMessageId" value="${editMessage.id}" type="hidden">
				<input type="submit" value="更新">（140文字まで）
			</form>
		</div>
		<a href="./">戻る</a>

		<div class="copyright">Copyright(c)Kohei Nagano</div>

	</div>

</body>
</html>