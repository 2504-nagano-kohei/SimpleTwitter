package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(urlPatterns={"/setting","/edit"})
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		// 型変換
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse resp = (HttpServletResponse)response;

		List<String> errorMessages = new ArrayList<String>();

		if (req.getSession().getAttribute("loginUser") != null) {
			// サーブレットを実行　リクエストのあった画面に遷移する。
			chain.doFilter(req, resp);
		} else {
			// ログインしてなければログイン画面に遷移してエラーメッセージを表示させる
			HttpSession session = req.getSession();
			errorMessages.add("ログインしてください");
			session.setAttribute("errorMessages", errorMessages);
			resp.sendRedirect("./login");
			return;
		}
	}


	@Override
	public void init(FilterConfig config) {
	}

	@Override
	public void destroy() {
	}


}
