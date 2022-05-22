------------------------
--JeuDonnées
------------------------

insert into Borne (id_borne, etat) values
        (1, 'occupée'),
        (2, 'réservée'),
        (3, 'disponible'),
        (4, 'indisponible');
commit;

insert into Parametre (nom_param, valeur)
values  ('periode_att', 15),
        ('nb_limit_prolong', 3);
commit;

insert into Client (num_membre, nom, prenom, adresse_postale, adresse_mail, num_tel, contrat, motif_contrat, estInterne) values
        (1, 'Gonzalez', 'Théo', '12 Rue Stanislas 54000 Nancy', 'theo.g@gl.com', '0601010101', true, 'Professionel', true),
        (2, 'Repetti', 'Alexandra', '14 Rue Stanislas 54000 Nancy', 'alex.r@gl.com', '0602020202', true, 'Personnel', true),
        (3, 'Mathieu', 'Jean', '16 Rue Stanislas 54000 Nancy', 'jean.m@gl.com', '0603030303', false, null, true),
        (4, 'Pirus', 'Marine', '18 Rue Stanislas 54000 Nancy', 'marine.p@gl.com', '0604040404', true, 'Personnel', false);
commit;

insert into Vehicule (immatriculation, marque, modele, id_borne, num_membre)
values  ('AA-111-BB', 'Tesla', '3', 3, 1),
        ('AB-112-BC', 'Citroën', 'DS4', 2, 2),
        ('AC-113-BD', 'BMW', 'i7', 3, 1),
        ('AD-114-BE', 'Toyota', 'bZ4x', null, 3);
commit;

insert into Reservation (num_res, date_deb, date_fin, delai_retard, num_membre, id_borne, immatriculation) values
        (1, '2022-05-08 11:30:00', '2022-05-08 12:30:00', null, 2, 3, 'AA-111-BB'),
        (2, '2022-05-11 11:30:00', '2022-05-11 12:30:00', null, 2, 3, 'AB-112-BC'),
        (3, '2022-05-14 11:30:00', '2022-05-14 12:30:00', null, 2, 3, 'AC-113-BD'),
        (4, '2022-05-15 11:30:00', '2022-05-15 12:30:00', null, 2, 3, 'AD-114-BE');
commit;

insert into Transaction (id_transaction, montant_charge, montant_res, montant_penalite, num_membre, num_res)
values  (1, 10.3 , 13.4, 11.3, 1, 2),
        (2, 11.3 , 10.4, 15.3, 2, 1),
        (3, 15.3 , 16.4, 13.3, 3, 3);
commit;


