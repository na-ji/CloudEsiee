package com.esiee;

import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;

@SuppressWarnings("serial")
public class RequeteServlet extends HttpServlet {
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		DatastoreService dataStore = DatastoreServiceFactory.getDatastoreService();
		
		// recuperer ce qui vient du formulaire html
		String nom = req.getParameter("nom");
		
		Query query = new Query("Ecole");
		query.addFilter("nom", Query.FilterOperator.EQUAL, nom);
		PreparedQuery pq = dataStore.prepare(query);
		for(Entity entity: pq.asIterable()){
			long age = (long) entity.getProperty("age");
			System.out.println(age);
		}
	}
}
