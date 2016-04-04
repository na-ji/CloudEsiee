package com.esiee;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();

		resp.setContentType("text/html");
		resp.getWriter().println("<h2>Cloud ESIEE</h2>");
		
		if (user != null) {
			resp.getWriter().println("Bonjour " + user.getNickname() + "<br />");
			resp.getWriter().println("<a href='" + userService.createLogoutURL(req.getRequestURI()) + "'> Déconnexion </a><br />");
			resp.getWriter().println("Available Servlets:<br />");
			resp.getWriter().println("<a href='cloudesiee'>Faire un calcul en tache de fond</a><br />");
			resp.getWriter().println("<a href='requete.html'>Chercher le résultat du calcul</a><br />");
		} else {
			resp.getWriter().println("<a href='" + userService.createLoginURL(req.getRequestURI()) + "'> Connexion </a>");
		}
	}
}
