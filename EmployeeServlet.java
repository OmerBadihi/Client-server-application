package com.srccodes;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpClient;
import java.sql.SQLException;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.srccodes.examples.Currency;
import com.srccodes.examples.Employee;
import com.srccodes.examples.GetInformation;
import com.srccodes.examples.PrimaryKey;
import com.srccodes.examples.Status;



/**
 * Servlet implementation class EmployeeServlet
 */
public class EmployeeServlet extends HttpServlet {
	String apiKey = "17e0439a3227da4193bf";
	private ObjectMapper objectMapper;
	private Status status;
	private static final long serialVersionUID = 1L;
    private SQLCRUD docrud;
    private final int OK = 200;
    private final int CREATED = 201;
    private final int NOTFOUND = 404;
	
    public EmployeeServlet() {
        super();
        try {
			docrud = new SQLCRUD("jdbc:mysql://localhost:3306/Employees", "root", "Gunrcshjh","EmpDetail");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
        objectMapper = new ObjectMapper()
        		.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        status = new Status(200);
        HttpClient.newHttpClient();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		GetInformation getInformation = new GetInformation();
		String json = request.getReader().lines().collect(Collectors.joining());//read body
		getInformation = objectMapper.readValue(json, GetInformation.class);
		
		response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        String responseFromDB = docrud.read(getInformation.getPrimaryKey()); 
        
        if(responseFromDB.length() == 0) {
        	status.setStatus(NOTFOUND);
        	printWriter.println(objectMapper.writeValueAsString(status));
        }
        else {
        	Employee employee = new Employee(responseFromDB);
        	status.setStatus(OK);
        	printWriter.println(objectMapper.writeValueAsString(employee));
        	printWriter.println(objectMapper.writeValueAsString(new Currency(getInformation.getCurrency(),employee)));
        	printWriter.println(objectMapper.writeValueAsString(status));
        	//need to change: Currency get only employee, and find the currency by the Country
        }
        
        response.setStatus(status.getStatus());
	}
	
	protected void 	doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Employee employee = new Employee();
		String json = request.getReader().lines().collect(Collectors.joining());//read body
		
		employee = objectMapper.readValue(json, Employee.class);
		
		response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        
		if(docrud.read(employee.primaryKey()).length() == 0) {
			printWriter.println(objectMapper.
            		writeValueAsString(new PrimaryKey(docrud.create(employee.Details()))));
        	status.setStatus(CREATED);
        	printWriter.println(objectMapper.writeValueAsString(status));
		}
		else {
			docrud.update(employee.primaryKey(), employee.Details());
			status.setStatus(OK);
        	printWriter.println(objectMapper.writeValueAsString(status));
		}
		
		response.setStatus(status.getStatus());
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Employee employee = new Employee();
		String json = request.getReader().lines().collect(Collectors.joining());
		
		employee = objectMapper.readValue(json, Employee.class);
		
		response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        
        if(docrud.read(employee.primaryKey()).length() == 0) {
        	printWriter.println(objectMapper.
            		writeValueAsString(new PrimaryKey(docrud.create(employee.Details()))));
        	status.setStatus(CREATED);
        	printWriter.println(objectMapper.writeValueAsString(status));
		}
        else {
        	printWriter.println(objectMapper.
            		writeValueAsString(new PrimaryKey(employee.primaryKey())));
        	status.setStatus(OK);
        	printWriter.println(objectMapper.writeValueAsString(status));
        	
        }
        response.setStatus(status.getStatus());
        
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrimaryKey primaryKey = new PrimaryKey();
		String json = request.getReader().lines().collect(Collectors.joining());
		
		primaryKey = objectMapper.readValue(json, PrimaryKey.class);
		
		response.setContentType("application/json");
        PrintWriter printWriter = response.getWriter();
        
        if(docrud.read(String.valueOf(primaryKey.getPrimaryKey())).length() == 0) {
        	status.setStatus(NOTFOUND);
        	printWriter.println(objectMapper.writeValueAsString(status));
        }
        else {
        	docrud.delete(String.valueOf(primaryKey.getPrimaryKey()));
        	status.setStatus(OK);
        	printWriter.println(objectMapper.writeValueAsString(status));
        }
        
        response.setStatus(status.getStatus());
	}
}
