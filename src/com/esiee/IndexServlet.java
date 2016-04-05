package com.esiee;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.Transform;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import java.util.Collections;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;


@SuppressWarnings("serial")
public class IndexServlet extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    private UserService userService = UserServiceFactory.getUserService();
    private ImagesService imagesService = ImagesServiceFactory.getImagesService();
    
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		User user = userService.getCurrentUser();

		resp.setContentType("text/html");
		resp.getWriter().println("<h2>Cloud ESIEE</h2>");
		
		// if user logged in
		if (user != null) {
			resp.getWriter().println("Bonjour " + user.getNickname() + "<br />");
			resp.getWriter().println("<a href='" + userService.createLogoutURL(req.getRequestURI()) + "'> Déconnexion </a><br />");
			resp.getWriter().println("Available Servlets:<br />");
			resp.getWriter().println("<a href='cloudesiee'>Faire un calcul en tache de fond</a><br />");
			resp.getWriter().println("<a href='requete.html'>Chercher le résultat du calcul</a><br /><br /><br />");
			
			resp.getWriter().println(
				"<form action='" + blobstoreService.createUploadUrl("/upload") + "' method='post' enctype='multipart/form-data'>"
					+ "<input type='file' name='image' accept='image/*'>"
			        + "<input type='submit' value='Uploader'>"
		        + "</form><br /><br />"
			);
		} else {
			// if not
			resp.getWriter().println("<a href='" + userService.createLoginURL(req.getRequestURI()) + "'> Connexion </a>");
		}
		
        Cache cache;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
            String key = "data";
            byte[] value = "CloudESIEE".getBytes();
            
            if (cache.get(key) == null) {
                // Put the value into the cache.
                cache.put(key, value);
            }

            // Get the value from the cache.
            String message = new String((byte[]) cache.get(key));
            resp.getWriter().println("Message en cache : " + message + "<br />");
        } catch (CacheException e) {
            // BLC
        }
		
		// images
		if (req.getParameter("blob-key") != null) {
	        BlobKey blobKey = new BlobKey(req.getParameter("blob-key"));
			resp.getWriter().println("<img src='" + imagesService.getServingUrl(blobKey) + "' /><br />");
	        
	        Image oldImage = ImagesServiceFactory.makeImageFromBlob(blobKey);
	        
	        Transform rotate = ImagesServiceFactory.makeRotate(90);
	        Image newImage = imagesService.applyTransform(rotate, oldImage);
	        
	        //resp.getWriter().println("<img src='" + imagesService.getServingUrl(newImage.getBlobKey()) + "' /><br />");
		}
	}
}
