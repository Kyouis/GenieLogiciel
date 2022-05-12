package miage.m1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connexion{
	private static Connection con ;
	
	//connection a la BDD
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
    
    
    //rechercher User avec son numero de membre 
    public void fetchUser(String num) {
    	try {
    		Statement stmt=con.createStatement();  
            ResultSet rs = stmt.executeQuery("SELECT * FROM Client WHERE num_membre="+num);
            while (rs.next()) {
            	System.out.println(rs.getString("nom"));
            }
		} catch (Exception e) {
			System.out.println(e);
		}
    }
    
    //ajout d'un nouvel utilisateur 
    public void addUser(String nom, String prenom, String adresse, String email, int num, Boolean contrat, String motif) {
    	try {
    		Statement stmt=con.createStatement();  
            int rs = stmt.executeUpdate("insert into Client (nom, prenom, adresse_postale, adresse_mail, num_tel, contrat, motif_contrat) values ('"
            		+nom+"','"+prenom+"','"+adresse+"','"+email+"','"+num+"',"+contrat+","+motif+""
            				+ ")");
            
            System.out.println("Creation OK");
		} catch (Exception e) {
			System.out.println(e);
		}
    }
    
    //verification de l'adresse email
    public boolean verifMail(String mail) {  
        String regx = "^[A-Za-z0-9+_.-]+@(.+)$";  
        Pattern pattern = Pattern.compile(regx);
        Matcher matcher = pattern.matcher(mail);
        return matcher.matches();
    }
    
    
}



