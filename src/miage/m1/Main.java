package miage.m1;

import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class Main {

    static Scanner sc = new Scanner(System.in);
    static Connexion connexion = new Connexion();
    static Borne borne = new Borne();
    static String userConnected = "-1";
    public static void main(String[] args) {

        Connexion.openConnexion();

        //test recherche user 
        //connexion.fetchUser("1");
        //test ajout user 
        //connexion.addUser("dede", "dede", "nancy", "ok", 02, false, null);

        System.out.println("Bienvenue ! Veuillez choisir l'action à réaliser : \n");
        Startmenu();

    }

    public static void Startmenu() {
        System.out.println("1 - Inscription à l'application\n" +
                "2 - Connexion\n" +
                "3 - Déconnexion");
        String choix = sc.nextLine();
        switch (choix) {
            case "1":
                signin();
                break;
            case "2":
                login();
                break;
            case "3":
                deco();
                break;
            case "4":
                System.out.println("/!\\Case de tests de fonctions");

                break;
            default:
                System.out.println("Veuillez rentrer un choix valide");
                Startmenu();
        }
    }

    public static void signin() {
        System.out.println("Création de compte : Veuillez entrez votre mail :");
        String mail =sc.nextLine();
        if (connexion.verifMail(mail)){
            System.out.println("Veuillez entrez votre nom :");
            String nom = sc.nextLine();
            System.out.println("Veuillez entrez votre prenom :");
            String prenom =sc.nextLine();
            System.out.println("Veuillez entrez votre adresse :");
            String adresse =sc.nextLine();
            System.out.println("Veuillez entrez votre numero de telephone :");
            int tel =sc.nextInt();
            connexion.addUser(nom,prenom,adresse,mail,tel, false, null);
        }
        else {
            System.out.println("adresse non valide");
        }
        Startmenu();
    }
    static String num;
    public static void login() {
        System.out.println("Veuillez rentrer votre numéro de membre");
        num = sc.nextLine();
        connexion.fetchUser(num);
        userConnected = num;
        System.out.println("Connection réussie");
        actionMenu();
    }

    public static void deco(){
        userConnected = "-1";
        System.out.println("Au revoir");
        Startmenu();
    }

    public static void actionMenu() {
        connexion.afficheDev(num);
        connexion.propReservation(num);
        System.out.println("Choisissez l'action à réaliser");
        System.out.println("   1 - Réserver une borne \n" +
                "   2 - Utiliser une borne sans réservation \n" +
                "   3 - Prolonger ma réservation\n" +
                "   4 - Rechercher une reservation par votre numéro de membre et la durée de charge\n" +
                "   5 - Ajouter une plaque d'immatriculation à votre compte");
        String choix = sc.nextLine();
        switch (choix) {
            case "1":
                reservationBorne(true);
                break;
            case "2":
                reservationBorne(false);
                break;
            case "3":
                prolongation();
                break;
            case "4":
                connexion.rechResNumMbDuree();
                break;
            case "5":
                connexion.ajoutPlaqueImmat();
                break;
            default:
                System.out.println("Veuillez rentrer un choix valide");
                actionMenu();
        }
    }

    public static void reservationBorne(Boolean b) {
        if (connexion.firstBorneDispo() == -1) {
            System.out.println("Il n'y a aucune borne disponible pour le moment");
        } else {
            String datedebut;
            if (b) {
                System.out.println("Veuillez rentrer une date de début de la réservation");
                datedebut = sc.nextLine();
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                datedebut = dateFormat.format(date);
            }
            System.out.println("Vous avez été assigné à la borne n°"+connexion.firstBorneDispo());

            System.out.println("Choisissez une date de fin au format YYYY-MM-DD");
            String d = sc.nextLine();
            connexion.useBorne(String.valueOf(connexion.firstBorneDispo()), userConnected, datedebut, d);

        }
        actionMenu();
    }

    public static void prolongation() {
        System.out.println("Indiquez le numero de la borne qui est assignée à la reservation à prolonger");
        String nBorne = sc.nextLine();
        System.out.println("Indiquez la date de fin de la réservation à prolonger");
        String datefin = sc.nextLine();
        if (connexion.checkProlongationPossible(nBorne, datefin, "30")) {
            System.out.println("votre prolongation est faite");
            try {
                java.sql.Date datefinparse= (java.sql.Date) new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(datefin);
                connexion.reserver(nBorne, userConnected, datefin, new java.sql.Date(datefinparse.getTime()+((long) 30 *60*1000)).toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("il n'est pas possible de prolonger");
        }
        actionMenu();
    }

}

