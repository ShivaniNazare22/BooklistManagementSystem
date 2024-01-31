package com.controller;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/BookServlet")
public class BookServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action.equals("addBook")) {
            try {
				addBook(request, response);
			} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else if (action.equals("listBooks")) {
            try {
				listBooks(request, response);
			} catch (ClassNotFoundException | ServletException | IOException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }

    private void addBook(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        String bookName = request.getParameter("bookName");
        String bookAuthor = request.getParameter("bookAuthor");
        int publishYear = Integer.parseInt(request.getParameter("publishYear"));
        Connection conn=null;
        String url="jdbc:mysql://localhost:3306/booklist";
		String user="root";
		String password="Gamma@2210";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(url,user,password);
        
            String query = "INSERT INTO booklib (bookname, bookauthor, year) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, bookName);
                preparedStatement.setString(2, bookAuthor);
                preparedStatement.setInt(3, publishYear);

                preparedStatement.executeUpdate();
            }
      

        response.sendRedirect("index.html");
    }

    private void listBooks(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, ClassNotFoundException, SQLException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        Connection conn=null;
        String url="jdbc:mysql://localhost:3306/booklist";
		String user="root";
		String password="Gamma@2210";
		Class.forName("com.mysql.cj.jdbc.Driver");
		conn = DriverManager.getConnection(url,user,password);
       
            String query = "SELECT * FROM booklib";
            try (PreparedStatement preparedStatement = conn.prepareStatement(query);
                    ResultSet resultSet = preparedStatement.executeQuery()) {

                out.println("<html><body><table border=\"1\">");
                out.println("<tr><th>Book Name</th><th>Book Author</th><th>Publish Year</th></tr>");

                while (resultSet.next()) {
                   // out.println("<tr><td>" + resultSet.getInt("book_id") + "</td>");
                    out.println("<td>" + resultSet.getString("bookname") + "</td>");
                    out.println("<td>" + resultSet.getString("bookauthor") + "</td>");
                    out.println("<td>" + resultSet.getInt("year") + "</td></tr>");
                }

                out.println("</table></body></html>");
            }
        } 
    }

