package miage.m1;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static miage.m1.Main.actionMenu;

public class Connexion{
	private static Connection con ;
    static Scanner sc = new Scanner(System.in);


    //connection a la BDD
    public static void openConnexion() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:8889/GenieLogiciel","root","root");
            
            System.out.println("Connected");  
        }
        catch(Exception e){
            System.out.println("Non connecté");
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
        System.out.println("Renseignez votre plaque d'immatriculation.");
        String immatriculation= sc.nextLine();
        try {
            Statement s = con.createStatement();
            String q = "INSERT INTO RESERVATION (immatriculation, date_deb, date_fin, num_membre, id_borne) VALUES ('"+immatriculation+"','"+dateDebut+"','"+dateFin+"',"+user+","+numborne+")";
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

    int num_membre;
    String immatriculation;
    String marque_vehicule;
    String modele_vehicule;
    public void ajoutPlaqueImmat(){
        System.out.println("Entrez votre numéro de membre.");
        num_membre= sc.nextInt();
        System.out.println("Entrez votre plaque d'immatriculation.");
        immatriculation= sc.next();
        System.out.println("Entrez la marque de votre véhicule.");
        marque_vehicule= sc.next();
        System.out.println("Entrez le modele de votre véhicule.");
        modele_vehicule= sc.next();
        try{
            Statement stmt=con.createStatement();
            //Insère dans la table la plaque d'immatriculation
            stmt.execute("INSERT INTO vehicule (immatriculation, num_membre, marque, modele) values ('"+
                    immatriculation+"','"+num_membre +"','"+marque_vehicule+"','"+modele_vehicule+"')");

        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    //Permet de rechercher une reservation avec num_membre et la durée
    public void rechResNumMbDuree(){
        int duree;
        System.out.println("Veuillez entrer votre numéro de membre.");
        num_membre = sc.nextInt();
        System.out.println("Entrez maintenant la durée de votre réservation en minutes.");
        duree = sc.nextInt();
        try {
            Statement stmt=con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT immatriculation, id_borne FROM reservation WHERE num_membre = "+num_membre+" and TIMESTAMPDIFF(MINUTE,date_deb,date_fin) = "+duree+" and date_deb > sysdate() ORDER BY date_deb ASC");

            ArrayList res_immat = new ArrayList<>();
            ArrayList res_borne = new ArrayList<>();

            while (rs.next()) {
                //COMMENT RECUPERER LE PREMIER DE LA LISTE???
                //TODO
                res_immat.add(rs.getString("immatriculation"));
                res_borne.add(rs.getString("id_borne"));

            }
            System.out.println("Notre système a trouvé cette plaque d'immatriculation : " + res_immat.get(0)+" affectée à cette borne : " + res_borne.get(0) +
                    "\nEst-ce la votre ?" +
                    "\nOui : 1" +
                    "\nNon : 2");
            int choix = sc.nextInt();
            switch(choix){
                case 1:
                    System.out.println("Souhaitez-vous l'utiliser?" +
                            "\nOui : 1" +
                            "\nNon : 2");
                    choix = sc.nextInt();
                    switch(choix){
                        case 1:
                            stmt.executeUpdate("UPDATE BORNE SET etat = 'Occupée' WHERE id_borne = "+res_borne.get(0));
                            System.out.println("La borne "+res_borne.get(0)+" vous attend.");
                            break;

                        case 2:
                            actionMenu();
                            break;
                        default:
                            System.out.println("La valeur renseignée n'est pas la bonne.");

                    }
                    break;
                case 2:
                    System.out.println("Nous vous invitons à réserver une place si vous ne l'avez pas fait ou recommencer");
                    actionMenu();
                    break;
                default:
                    System.out.println("La valeur renseignée n'est pas la bonne.");
            }
        }catch (Exception e){
            System.out.println("Votre réservation n'a pas été trouvé");
            System.out.println(e);
        }

    }

    public void propReservation(String num){
        try {
            Statement stmt=con.createStatement();
            stmt.executeQuery("");

            ResultSet rs = stmt.executeQuery("SELECT * FROM Reservation WHERE num_membre = "+num+" AND date_fin > sysdate()");
            System.out.println("Nous avons trouvé une reservation pour ce membre : " + rs.getInt("num_membre")
                    +"\nà cette date de début : "+ rs.getDate("date_debut"));


        }catch (Exception e){

        System.out.println("Nous trouvons aucune reservation pour vous." +
                "\nSouhaitez vous réserver une borne ?" +
                "\n1 : Oui" +
                "\n2 : Non");

        int choix = sc.nextInt();
        switch (choix){
            case 1:
                System.out.println("Vous êtes sur le point de réserver une borne." +
                        "\nVeuillez renseigner la date de début au format YYYY-MM-JJ HH:MM:SS.");
                String date_deb=sc.nextLine();
                System.out.println("Veuillez renseigner la date de fin au format YYYY-MM-JJ HH:MM:SS.");
                String date_fin=sc.nextLine();
                System.out.println("Vos dates sont enregistrées.");
                String borne_dispo = String.valueOf(firstBorneDispo());
                System.out.println("date deb = " +date_deb+ "datefin : "+ date_fin);
                reserver(borne_dispo,num,date_deb,date_fin);

            case 2:
                actionMenu();
                break;
            default:
                System.out.println("Valeur incorrect");
        }

        }
    }
    public static void test(String num) {
        try {
            Statement stmt=con.createStatement();
            stmt.executeQuery("");

            ResultSet rs = stmt.executeQuery("SELECT * FROM Reservation WHERE num_membre = "+num+" AND date_fin > sysdate()");
            System.out.println("Res : " + rs.getInt("num_membre"));


        }catch (Exception e){
            System.out.println("Valeur non trouvée");
        }
    }
}



