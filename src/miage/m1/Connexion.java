package miage.m1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Connexion{
	private static Connection con ;
	
	
    public static void openConnexion() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GenieLogiciel","root","");
            
            System.out.println("Connected");  
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public void fetchUser() {
    	try {
    		Statement stmt=con.createStatement();  
            ResultSet rs = stmt.executeQuery("SELECT * FROM Client WHERE ");
            while (rs.next()) {
            	System.out.println(rs.getInt("id_borne"));
            }
		} catch (Exception e) {
			// TODO: handle exception
		}
    }
    
    
}