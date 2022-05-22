package miage.m1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class Borne {

    int id_borne;
    String etat_borne;
    Scanner sc = new Scanner(System.in);
    private static Connection con ;

    public Borne(int id_borne, String etat_borne) {
        id_borne = this.id_borne;
        etat_borne = this.etat_borne;
    }

    public void creerUneBorne() {

        try {
            System.out.println("Quel état souhaitez vous affecter à cette borne ?");
            String etat_borne =sc.nextLine();
            Statement stmt= con.createStatement();


            int rs = stmt.executeUpdate("insert into Borne (etat) values ('"+etat_borne+"')");

            System.out.println("La borne a bien été créée !");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void affciheBorneDispo() {
        try {
            System.out.println("Voici la liste des bornes disponibles :");
            String etat_borne =sc.nextLine();
            Statement stmt= con.createStatement();

            //Récupere la listes des bornes dispo
            ResultSet borneDispo = stmt.executeQuery("SELECT * FROM Borne WHERE etat= 'disponible'");
            //récupere la colonne id_borne
            borneDispo.getString("id_borne");
            System.out.println(borneDispo);

            borneDispo.getInt("estInterne");


        } catch (Exception e) {
            System.out.println(e);
        }
    }
}

/*
System.out.println("Souhaitez-vous vous connecter ou vous inscrire ? \n1: Inscription\n2: Connexion");

String choix_deb =sc.nextLine();

switch(choix_deb){
		//INSCRIPTION
       case 1:
           System.out.println("Veuillez renseigner les informations suivantes : \n");
           System.out.println("Entrez votre adresse mail :");
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
           break;

   		//CONNEXION
       case 2:
           System.out.println("Entrez vos identifiants de connexion :\n");

			if (connexion.verifMail(mail)==true){
			    System.out.println("Veuillez entrez votre mot de passe :");
			    String mdp = sc.nextLine();

			    //récupère info du user dans variable rs


			    if (borneDispo.getInt("estInterne") == 1){

			    System.out.println("Que souhaitez vous faire ?/n1. Créer une borne ? 2.Reserver une borne ?");
			    String choix_dev = sc.nextLine();

			    switch(choix_dev) {
			    	case 1:
			    		creerUneBorne()
			    		break;

			    	case 2:

			    		break;

			    	default:
			           System.out.println("Choix incorrect");
			           break;
			    }


			}

			}
			else {
			    System.out.println("adresse non valide");
			}


           break;

       default:
           System.out.println("Choix incorrect");
           break;
   }


*/
