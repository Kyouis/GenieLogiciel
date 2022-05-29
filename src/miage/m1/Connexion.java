package miage.m1;

import java.sql.*;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public void updateReserv(String nbReserv, String dateDebut, String dateFin) {
        try {
            Statement s = con.createStatement();
            s.executeUpdate("UPDATE TABLE RESERVATION SET date_deb = + " +dateDebut+" , date_fin = "+dateFin+" WHERE num_res = "+ nbReserv);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet listReservMembre(String idMembre) {
        Statement stmt= null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM RESERVATION WHERE num_membre = "+idMembre);
            return rs;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
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


    public void ajoutPlaqueImmat(){
        int num_membre;
        String immatriculation;
        String marque_vehicule;
        String modele_vehicule;
        System.out.println("Entrez votre numéro de membre.");
        num_membre= sc.nextInt();
        System.out.println("Entrez votre plaque d'immatriculation.");
        immatriculation= sc.next();
        System.out.println("Entrez la marque de votre véhicule.");
        marque_vehicule= sc.next();
        System.out.println("Entrez le modèle de votre véhicule.");
        modele_vehicule= sc.next();
        try{
            Statement stmt=con.createStatement();
            //Insère dans la table la plaque d'immatriculation
            stmt.execute("INSERT INTO vehicule (immatriculation, num_membre, marque, modele) values ('"+
                    immatriculation+"','"+num_membre +"','"+marque_vehicule+"','"+modele_vehicule+"')");
            System.out.println("Votre véhicule a été enregistré avec cette plaque : "+immatriculation);
            actionMenu();
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
                res_immat.add(rs.getString("immatriculation"));
                res_borne.add(rs.getString("id_borne"));

            }
            System.out.println("Notre système a trouvé cette plaque d'immatriculation : " + res_immat.get(0)+" affectée à cette borne : " + res_borne.get(0) +
                    "\nEst-ce la votre ?" +
                    "\n Oui : 1" +
                    "\n Non : 2");
            int choix = sc.nextInt();
            switch(choix){
                case 1:
                    System.out.println("Souhaitez-vous l'utiliser?" +
                            "\n Oui : 1" +
                            "\n Non : 2");
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
            ResultSet rs = stmt.executeQuery("SELECT num_membre, date_deb FROM Reservation WHERE num_membre = "+num+" AND date_fin > sysdate()");

            //Si aucune reservation n'est trouvée
            if (!rs.next()){
                System.out.println("Nous trouvons aucune reservation pour vous." +
                        "\nSouhaitez vous réserver une borne ?" +
                        "\n 1 : Oui" +
                        "\n 2 : Non");

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
                        //fontionne pas TODO
                        reserver(borne_dispo,num,date_deb,date_fin);

                    case 2:
                        break;
                    default:
                        System.out.println("Valeur incorrect");
                }
            }else{
                //Si une/des réservations sont trouvées
                rs = stmt.executeQuery("SELECT num_membre, date_deb FROM Reservation WHERE num_membre = "+num+" AND date_fin > sysdate()");

                System.out.println("\nNous avons trouvé une reservation pour le membre n°"+num+"\nVoici la lise des dates :");
                while (rs.next()){
                    System.out.println("    *"+ rs.getString("date_deb"));
                }
            }


        }catch (Exception e) {
            System.out.println(e);
        }
    }

//CLASSE BORNE
    //menu pour une personne en interne
    public void afficheDev(String num){

        try {
            Statement stmt= con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT estInterne FROM client WHERE num_membre= "+num);

            ArrayList res_estInterne = new ArrayList<>();

            while (rs.next()) {
                res_estInterne.add(rs.getInt("estInterne"));
            }
            System.out.println(res_estInterne.get(0));

            if (res_estInterne.get(0).equals(1)){
                System.out.println("Vous faites partie des gérants.\n"+
                        "En tant que tel souhaitez vous : \n" +
                        "   1 - Afficher les bornes disponibles ?\n" +
                        "   2 - Créer une nouvelle borne ?\n" +
                        "   3 - Modifier l'état d'une borne\n" +
                        "   4 - Modifier des paramètres\n" +
                        "   5 - Afficher le profil d'un client\n" +
                        "   6 - Ou bien aller au menu client ?");
                int choix = sc.nextInt();
                switch(choix){
                    case 1:
                        afficheBorneDispo();
                        break;
                    case 2:
                        creerUneBorne();
                        break;
                    case 3:
                        modEtatBorne();
                        break;
                    case 4:
                        modParam();
                        break;
                    case 5:
                        afficheProfilCli();
                        break;
                    case 6:
                        break;
                    default:
                        System.out.println("Entrez une valeur de la liste.");
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Affiche les bornes disponibles
    public void afficheBorneDispo() {
        try {
            System.out.println("Voici la liste des bornes disponibles :");
            String etat_borne =sc.nextLine();
            Statement stmt= con.createStatement();

            //Récupère la liste des bornes dispo
            ResultSet rs = stmt.executeQuery("SELECT id_borne FROM Borne WHERE etat= 'disponible'");
            //Récupère la colonne id_borne
            while (rs.next()) {
                System.out.println("    n°"+rs.getInt("id_borne"));
            }


        } catch (Exception e) {
            System.out.println(e);
        }
    }


    //Permet de créer une borne
    public void creerUneBorne() {
        try {
            System.out.println("Quel état souhaitez vous affecter à cette borne ? (Disponible, Occupée, Réservée, Indisponible)");
            String etat_borne = sc.next();

            Statement stmt= con.createStatement();
            stmt.executeUpdate("insert into Borne (etat) values ('"+etat_borne+"')");

            System.out.println("La borne a bien été créée !");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //Modifier les paramètres
    public void modEtatBorne(){
        System.out.println("Vous souhaitez modifier l'état d'une borne.");
        try {

            Statement stmt= con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_borne, etat FROM Borne");


            while (rs.next()) {
                System.out.println("    n°"+rs.getInt("id_borne")+" etat : "+rs.getString("etat"));
            }
            System.out.println("Indiquez quelle borne vous voulez modifier.");
            int id_borne = sc.nextInt();
            System.out.println("Entrez la valeur de l'état.");
            String etat_borne = sc.next();

            stmt.executeUpdate("UPDATE borne SET etat = '"+etat_borne+ "' WHERE id_borne = " +id_borne);
            System.out.println("La borne n°"+id_borne+" est maintenant "+etat_borne);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void changeReservationPartirPlusTot(String idR) {
        Statement stmt= null;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id_borne FROM RESERVATION WHERE num_res = "+idR);
            String b="";
            while (rs.next()) {
                b = rs.getString("id_borne");
            }
            stmt.executeUpdate("UPDATE borne SET etat = 'disponible' WHERE id_borne = " + b);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //Modifie les paramètres
    public void modParam(){
        System.out.println("Vous souhaitez changer des paramètres.");
        try {
            Statement stmt= con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT nom_param, valeur FROM Parametre");
            System.out.println("Voici la liste des paramètres :");
            while (rs.next()) {
                System.out.println("    nom : "+rs.getString("nom_param")+" = "+rs.getInt("valeur"));
            }
            System.out.println("Indiquez quel paramètre vous voulez modifier.");
            String nom_param = sc.next();
            System.out.println("Entrez la valeur de l'état.");
            int valeur = sc.nextInt();

            stmt.executeUpdate("UPDATE parametre SET valeur = "+valeur+" WHERE nom_param = '" + nom_param+"'");
            System.out.println("Le paramètre "+nom_param+" est maintenant réglé à "+valeur);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void afficheProfilCli() {
        try {
            Statement stmt= con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT num_membre, nom, prenom FROM Client");
            System.out.println("Voici la liste des clients :");
            while (rs.next()) {
                System.out.println("    - Membre n°"+rs.getString("num_membre")+" nom : "+rs.getString("nom")+" prénom : "+rs.getString("prenom"));
            }
            System.out.println("\nIndiquez le numéro du membre que vous voulez visualiser.");
            num_membre = sc.nextInt();
            rs = stmt.executeQuery("SELECT num_membre, nom, prenom, adresse_postale, adresse_mail, num_tel, contrat, motif_contrat, estInterne " +
                    "FROM Client " +
                    "WHERE num_membre = "+num_membre);
            System.out.println("Voici la fiche du client :");
            while (rs.next()) {
                System.out.println(" Membre n°"+rs.getString("num_membre") +"\n"
                        + " nom : "+rs.getString("nom") +"\n"
                        + " prénom : "+rs.getString("prenom")+"\n"
                        + " adresse postale : "+rs.getString("adresse_postale")+"\n"
                        + " adresse mail : "+rs.getString("adresse_mail")+"\n"
                        + " numéro de téléphone : "+rs.getString("num_tel")+"\n"
                        + " contrat : "+rs.getString("contrat")+"\n"
                        + " motif si contrat : "+rs.getString("motif_contrat")+"\n"
                        + " interne : "+rs.getString("estInterne"));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }



}



