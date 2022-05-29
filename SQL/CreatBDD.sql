create table Client (
    num_membre integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    nom varchar(128) NOT NULL,
    prenom varchar(128) NOT NULL,
    adresse_postale varchar(128) NOT NULL,
    adresse_mail varchar(128) NOT NULL,
    num_tel varchar(20) NOT NULL,
    contrat boolean,
    motif_contrat ENUM('Professionel', 'Personnel'),
	estInterne BOOLEAN DEFAULT FALSE
);

create table Borne (
    id_borne integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    etat ENUM('disponible', 'indisponible', 'occupée', 'réservée') 

);

create table Parametre (
    nom_param varchar(128) PRIMARY KEY NOT NULL,
    valeur varchar(128)
    
);

create table Vehicule (
    immatriculation varchar(128) PRIMARY KEY NOT NULL,
    marque varchar(128) NOT NULL,
    modele varchar(128) NOT NULL,
    id_borne integer,
    num_membre integer NOT NULL,

    FOREIGN KEY (num_membre) REFERENCES Client(num_membre),
    FOREIGN KEY (id_borne) REFERENCES Borne(id_borne)
);

create table Reservation (
    num_res integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    date_deb datetime NOT NULL,
    date_fin datetime NOT NULL,
    date_fin_prolong datetime ,
    delai_retard integer,
    num_membre integer NOT NULL,
    id_borne integer NOT NULL,
    immatriculation varchar(9) NOT NULL,

    FOREIGN KEY (num_membre) REFERENCES Client(num_membre),
    FOREIGN KEY (id_borne) REFERENCES Borne(id_borne),
	FOREIGN KEY (immatriculation) REFERENCES Vehicule(immatriculation)

);


create table Transaction (
    id_transaction integer PRIMARY KEY NOT NULL AUTO_INCREMENT,
    montant_charge DECIMAL(10,2),
    montant_res DECIMAL(10,2),
    montant_penalite DECIMAL(10,2),
    num_membre integer NOT NULL,
    num_res integer NOT NULL,

    FOREIGN KEY (num_membre) REFERENCES Client(num_membre),
    FOREIGN KEY (num_res) REFERENCES Reservation(num_res)
);
commit;