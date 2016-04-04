package com.esiee;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

@SuppressWarnings("serial")
public class TacheDeFondServlet extends HttpServlet {
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		Entity etudiant = new Entity("Ecole");
		etudiant.setProperty("nom", "Tintin");
		etudiant.setProperty("age", 20);
		dataStore.put(etudiant);
		
		System.out.println("Je suis la tache de fond");
		
	}
}
