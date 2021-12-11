package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.annotation.WebServlet;

import util.DatabaseConnect;

@WebServlet(urlPatterns = "/addEvent")
public class addEvent extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        StringBuffer data = new StringBuffer();
        String line = null;

        // reading JSON from POST request
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                data.append(line);
            }
        } catch (Exception e) {
            resp.setStatus(500);
        }
        
        System.out.println(data);
        String query = "INSERT INTO eventUEF (name, host_time, description) VALUES (?,?,?);";

        try {
            DatabaseConnect DB = new DatabaseConnect();
            Connection conn = DB.getConnection();
            PreparedStatement st = conn.prepareStatement(query);
            JsonNode json = mapper.readTree(data.toString());

            st.setString(1, json.get("name").toString().split("\"")[1]);
            st.setString(2, json.get("host_time").toString().split("\"")[1]);
            st.setString(3, json.get("description").toString().split("\"")[1]);

            System.out.println(st);
            
            st.executeUpdate();
            DB.closeConnect();
        } catch (Exception e) {
        	resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	resp.getWriter().println("The Request is invalid");
            e.printStackTrace();
        }
    }
}
