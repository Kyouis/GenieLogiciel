package miage.m1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connexion{
    public static void main(String[] args) {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GenieLogiciel","root","");
            Statement stmt=con.createStatement();  
            ResultSet rs = stmt.executeQuery("SELECT * FROM Borne");
            while (rs.next()) {
            	System.out.println(rs.getInt("id_borne"));
            }
            
            System.out.println("Connected");  
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }  
}