package miage.m1;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connexion{
    private static Connection con ;
    static Scanner sc = new Scanner(System.in);


    //connection a la BDD
    public static void openConnexion() {
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/GenieLogiciel","root","");

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

    public boolean checkProlongationPossible(String numBorne,String datefin, String dureeProl) {
        boolean res = true;
        try {
            Date datefinparse= (Date) new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datefin);

            Statement s = con.createStatement();
            String q = "SELECT * FROM RESERVATION WHERE id_borne="+numBorne;
            ResultSet r = s.executeQuery(q);
            while (r.next()) {
                if (r.getDate("date_fin").after( datefinparse) && r.getDate("date_fin").before(new Date(datefinparse.getTime()+((long) Integer.parseInt(dureeProl) *60*1000)))) {
                    res = false;
                }
            }
        } catch (SQLException | ParseException throwables) {
            throwables.printStackTrace();
        }
        return res;
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
            ResultSet rs = stmt.executeQuery("SELECT immatriculation FROM reservation WHERE num_membre = "+num_membre+" and TIMESTAMPDIFF(MINUTE,date_deb,date_fin) = "+duree);

            while (rs.next()) {
                //COMMENT RECUPERER LE PREMIER DE LA LISTE???
                //TODO
                System.out.println(rs.getString("immatriculation"));
            }
            /*System.out.println("Notre système à trouvé cette plaque d'immatriculation : " + rs.getString("immatriculation")+"" +
                    "\nEst-ce la votre ?" +
                    "\nOui : 1" +
                    "\nNon : 2");
            int choix = sc.nextInt();
            switch(choix){
                case 1:
                    //TODO
                    break;
                case 2:
                    System.out.println("Nous vous invitons à réserver une place si vous ne l'avez pas fait ou recommencer");
            }*/
        }catch (Exception e){
            System.out.println("Votre réservation n'a pas été trouvé");
            System.out.println(e);
        }

    }

    public void propReservation(int num_membre){
        System.out.println("Nous trouvons aucune reservation pour vous." +
                "\nSouhaitez vous réserver une borne ?" +
                "\n1 : Oui" +
                "\n2 : Non");

        int choix = sc.nextInt();
        switch (choix){
            case 1:
                System.out.println("Vous êtes sur le point de réserver une borne." +
                        "\nVeuillez renseigner la durée de charge en minutes.");
                int duree = sc.nextInt();

                try {
                    Statement stmt=con.createStatement();
                    stmt.executeQuery("INSERT INTO reservation (num_membre, id_brone, date_deb, date_fin) values ('"+
                            num_membre+"','"+firstBorneDispo()+"',sysdate(),date_add(sysdate(),INTERVAL "+ duree +" MINUTE)')");

                    ResultSet rs = stmt.executeQuery("SELECT date_fin FROM reservation WHERE num_membre = "+num_membre+" and TIMESTAMPDIFF(MINUTE,date_deb,date_fin) = "+duree);
                    Date date_fin = rs.getDate("date_fin");
                    System.out.println("La réservation a été prise en compte." +
                            "\nVous pouvez recharger votre véhicule jusqu'au : " + date_fin);


                }catch (Exception e){
                    System.out.println("La réservation a échouée");
                }

        }
    }
}



