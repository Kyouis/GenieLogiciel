
package miage.m1;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static Connexion connexion =new Connexion();
    static String userConnected = "-1";
    public static void main(String[] args) {

        Connexion.openConnexion();
        System.out.println("Bienvenue ! Veuillez choisir l'action à réaliser : \n");
        Startmenu();

    }

    public static void Startmenu() {
        System.out.println("1 - Inscription à l'application\n" +
                "2 - Connexion");
        String choix = sc.nextLine();
        switch (choix) {
            case "1":
                signin();
                break;
            case "2":
                login();
                break;
            default:
                System.out.println("Veuillez rentrer un choix valide");
                Startmenu();
        }
    }

    public static void signin() {
        //TODO
    	 System.out.println("Cr�ation de compte : Veuillez entrez votre mail :");
         String mail =sc.nextLine();
         if (connexion.verifMail(mail)==true){   
             System.out.println(" Veuillez entrez votre nom :");
             String nom = sc.nextLine();
             System.out.println("Veuillez entrez votre prenom :");
             String prenom =sc.nextLine();
             System.out.println("Veuillez entrez votre adresse :");
             String adresse =sc.nextLine();
             System.out.println("Veuillez entrez votre num�ro de t�l�phone :");
             int tel =sc.nextInt();
             connexion.addUser(nom,prenom,adresse,mail,tel, false, null);
         }
         else {
             System.out.println("adresse non valide");
         }
    }

    public static void login() {
        System.out.println("Veuillez rentrer votre numéro de membre");
        String num = sc.nextLine();
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
    	
    	//ajouter utilisier une borne 
        System.out.println("Choississez l'action à réaliser");
        System.out.println("1 - Réserver une borne \n" +
                "2 - Utiliser une borne sans réservation \n"
                + "3 - Realiser une r�servation permanente ");
        String choix = sc.nextLine();
        switch (choix) {
            case "1":
                reservationBorne(true);
                break;
            case "2":
                reservationBorne(false);
                break;
            case "3": 
            	reservationPermanente();
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
                System.out.println("Veuillez rentrer une date de début de la réservation au format YYYY-MM-DD HH:MM:SS");
                datedebut = sc.nextLine(); 
            } else {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                datedebut = dateFormat.format(date);
            }
            System.out.println("Vous avez été assigné à la borne n°"+connexion.firstBorneDispo());
            
            System.out.println("Choississez une date de fin au format YYYY-MM-DD HH:MM:SS");
            String d = sc.nextLine();
            
            connexion.useBorne(String.valueOf(connexion.firstBorneDispo()), userConnected, datedebut, d);

        }
        actionMenu();
    }

	private static void reservationPermanente() {
		String datedebut;
        System.out.println("Veuillez rentrer une date de début de la réservation permanente au format YYYY-MM-DD");
        datedebut = sc.nextLine();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        datedebut = dateFormat.format(date);
        
        System.out.println("Choississez une date de fin au format YYYY-MM-DD");
        String d = sc.nextLine();
        
        System.out.println("Vous avez été assigné à la borne n°"+connexion.firstBorneDispo());
        connexion.useBorne(String.valueOf(connexion.firstBorneDispo()), userConnected, datedebut, d);
		
	}


}

