package com.learn.mycart.servlets;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import com.learn.mycart.dao.CategoryDao;
import com.learn.mycart.dao.ProductDao;
import com.learn.mycart.entities.Category;
import com.learn.mycart.entities.Product;
import com.learn.mycart.helper.FactoryProvider;

@MultipartConfig
public class ProductOperationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String op = request.getParameter("operation");

		if (op.trim().equals("addCategory")) {
			String title = request.getParameter("catTitle");
			String description = request.getParameter("catDescription");

			Category category = new Category();
			category.setCategoryTitle(title);
			category.setCategoryDescriptioin(description);

			// Category database save
			CategoryDao categoryDao = new CategoryDao(FactoryProvider.getFactory());
			int catId = categoryDao.saveCategory(category);

			HttpSession httpsession = request.getSession();
			httpsession.setAttribute("message", "Category added successfully: " + catId);
			response.sendRedirect("admin.jsp");
			return;
		} else if (op.trim().equals("addProduct")) {
			String pName = request.getParameter("pName");
			String pDesc = request.getParameter("pDesc");
			int pPrice = Integer.parseInt(request.getParameter("pPrice"));
			int pDiscount = Integer.parseInt(request.getParameter("pDiscount"));
			int pQuantity = Integer.parseInt(request.getParameter("pQuantity"));
			int catId = Integer.parseInt(request.getParameter("catId"));
			Part part = request.getPart("pPic");

			Product p = new Product();
			p.setpName(pName);
			p.setpDesc(pDesc);
			p.setpPrice(pPrice);
			p.setpDiscount(pDiscount);
			p.setpQuantity(pQuantity);

			// Get category by ID
			CategoryDao cdao = new CategoryDao(FactoryProvider.getFactory());
			Category category = cdao.getCategoryById(catId);
			p.setCategory(category);
			p.setpPhoto(extractFileName(part));


			// Product save
			ProductDao pdao = new ProductDao(FactoryProvider.getFactory());
			Boolean productId = pdao.saveProduct(p);

			// Pic upload
			// Find out the path to upload the photo with a unique filename
			try {
				String fileName = extractFileName(part); // Generate a unique filename for the uploaded image
				String path = request.getRealPath("img") + File.separator + "products" + File.separator + fileName;
				FileOutputStream fos = new FileOutputStream(path);

				InputStream is = part.getInputStream();

				byte[] data = new byte[is.available()];
				is.read(data);


				// Writing the data
				fos.write(data);
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			HttpSession httpsession = request.getSession();
			httpsession.setAttribute("message", "Product added successfully: " + productId);
			response.sendRedirect("admin.jsp");
			return;
		}
	}

	// Helper method to extract the filename from the Part
	private String extractFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		String[] items = contentDisp.split(";");

		for (String s : items) {
			if (s.trim().startsWith("filename")) {
				return s.substring(s.indexOf("=") + 2, s.length()- 1);
			}
		}

		return part.getName();
	}
}




//package com.learn.mycart.servlets;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//import javax.servlet.http.Part;
//
//import com.learn.mycart.dao.CategoryDao;
//import com.learn.mycart.dao.ProductDao;
//import com.learn.mycart.entities.Category;
//import com.learn.mycart.entities.Product;
//import com.learn.mycart.helper.FactoryProvider;
//
//
//@MultipartConfig
//public class ProductOperationServlet extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException {
//
//		String op = request.getParameter("operation");
//
//		if (op.trim().equals("addCategory")) {
//			String title = request.getParameter("catTitle");
//			String description = request.getParameter("catDescription");
//
//
//			Category category=new Category();
//			category.setCategoryTitle(title);
//			category.setCategoryDescriptioin(description);
//
//			//category database save;
//			CategoryDao categoryDao=new CategoryDao(FactoryProvider.getFactory());
//			int catId=categoryDao.saveCategory(category);
//
//			HttpSession httpsession= request.getSession();
//			httpsession.setAttribute("message","Category added successfully:"+catId);
//			response.sendRedirect("admin.jsp");
//			return;
//		}
//		else if(op.trim().equals("addProduct"))
//		{
//
//			//add product
//			String pName=request.getParameter("pName");
//			String pDesc=request.getParameter("pDesc");
//			int pPrice=Integer.parseInt(request.getParameter("pPrice"));
//			int pDiscount=Integer.parseInt(request.getParameter("pDiscount"));
//			int pQuantity=Integer.parseInt(request.getParameter("pQuantity"));
//			int catId=Integer.parseInt(request.getParameter("catId"));
//			Part part=request.getPart("pPic");
////			Part part=request.get;
//
//
//			Product p=new Product();
//			p.setpName(pName);
//			p.setpDesc(pDesc);
//			p.setpPrice(pPrice);
//			p.setpDiscount(pDiscount);
//			p.setpQuantity(pQuantity);
//			p.setpPhoto(part.getName());
//
//
//			//get category by id
//
//			CategoryDao cdao= new CategoryDao(FactoryProvider.getFactory());
//			Category category=cdao.getCategoryById(catId);
//			p.setCategory(category);
//
//
//			//product save
//			ProductDao pdao=new ProductDao(FactoryProvider.getFactory());
//			pdao.saveProduct(p);
//
//			//pic upload
//			//find out the path to uploaad photo
//			try {
//				String path = request.getRealPath("img") + File.separator + "products" + File.separator + part.getName();
////				String path = "img";
//				FileOutputStream fos= new FileOutputStream(path);
//
//				InputStream is= part.getInputStream();
//
//
//				byte []data=new byte[is.available()];
//				is.read(data);
//
//				//writing the data
//				fos.write(data);
//				fos.close();
//
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//
//			HttpSession httpsession= request.getSession();
//			httpsession.setAttribute("message","Product added successfully:");
//			response.sendRedirect("admin.jsp");
//			return;
//		}
//
//	}
//
//}
