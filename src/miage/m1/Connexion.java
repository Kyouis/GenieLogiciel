package miage.m1;

import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connexion{
	private static Connection con ;
	
	//connection a la BDD
    public static void openConnexion() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GL_Borne","root","");
            
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
    //vérification borne dispo
    public int firstBorneDispo() {
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM BORNE WHERE etat = 'disponible'");
            while (rs.next()) {
                return rs.getInt("id_borne");
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return -1;
    }

    //utilisation d'une borne
    public void useBorne(String num, String user, String datedebut, String dateFin) {
        try {
            Statement s = con.createStatement();
            int t = s.executeUpdate("UPDATE BORNE SET etat = 'Occupée' WHERE id_borne = "+num);
            reserver(num, user, datedebut, dateFin);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void reserver(String numborne, String user, String dateDebut, String dateFin) {
        try {
            Statement s = con.createStatement();
            String q = "INSERT INTO RESERVATION (date_deb, date_fin, num_membre, id_borne) VALUES ('"+dateDebut+"','"+dateFin+"','"+user+"','"+numborne+"')";
            System.out.println(q);
             int t = s.executeUpdate(q);
            System.out.println("Reservation ok");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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



