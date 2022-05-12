
package miage.m1;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connexion connexion =new Connexion(); 
        connexion.openConnexion();
        
        /*
        System.out.println("Création de compte : Veuillez entrez votre mail :");
        String mail =sc.nextLine();
        if (connexion.verifMail(mail)==true){   
            System.out.println(" Veuillez entrez votre nom :");
            String nom = sc.nextLine();
            System.out.println("Veuillez entrez votre prenom :");
            String prenom =sc.nextLine();
            System.out.println("Veuillez entrez votre adresse :");
            String adresse =sc.nextLine();
            System.out.println("Veuillez entrez votre numéro de téléphone :");
            int tel =sc.nextInt();
            connexion.addUser(nom,prenom,adresse,mail,tel, false, null);
        }
        else {
            System.out.println("adresse non valide");
        }
        */
        //test recherche user 
        connexion.fetchUser("1");
        //test ajout user 
        //connexion.addUser("dede", "dede", "nancy", "ok", 02, false, null);
        
    }

}

