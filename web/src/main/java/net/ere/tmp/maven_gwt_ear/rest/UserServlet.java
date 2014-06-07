package net.ere.tmp.maven_gwt_ear.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "userServlet", urlPatterns = { "/rest/users" }, //
initParams = { @WebInitParam(name = "simpleInitParam", value = "simpleInitParamValue") })
public class UserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		String simpleParam = getServletConfig().getInitParameter("simpleInitParam");
		String param1 = req.getParameter("param1");

		if (param1 != null && !param1.isEmpty()) {
			out.println("Hello World '" + simpleParam + "' : " + param1);
		} else {
			out.println("{");
			out.println("{\"id\":12934563,\"name\":\"Bill Smith\",\"email\":\"bill.smith@web.com\",\"phoneNumber\":\"32125551212\"}\",");
			out.println("{\"id\":12934564,\"name\":\"Bill Smith 2\",\"email\":\"bill.smith2@web.com\",\"phoneNumber\":\"321255512142\"}\"");
			out.println("}");
		}

		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String name = req.getParameter("name");
		String name2 = URLDecoder.decode(name, "UTF-8");

		System.out.println("----> name: '" + name + "'; name2='" + name2 + "'");

		String email = req.getParameter("email");
		String number = req.getParameter("phoneNumber");
		resp.setContentType("application/json; charset=UTF-8");
		PrintWriter out = resp.getWriter();

		out.println("{\"name\":\"" + name2 + "\",\"email\":\"" + email + "\",\"phoneNumber\":\"" + number + "\"}\"");
		out.close();
	}
}