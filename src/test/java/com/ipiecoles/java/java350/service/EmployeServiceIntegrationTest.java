package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployeServiceIntegrationTest {

    //Problème, les services ont des dépendances extérieurs,
    // on fait donc des mocks pour simuler le fonctionnement des dépendances

    //Le temps d'un test pour la compréhension
    //injection du service et du repo pour faire un exemple de test d'intégration
    @Autowired
    private EmployeService employeService;

    @Autowired
    private EmployeRepository employeRepository;

    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        //When
        //la méthode renvoie une exception donc on ajoute le throws à la méthode
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        //On vérifie que l'employe est bien créé
        //mais pour que le test ne plante pas on crée une liste d'employé
        //List<Employe> employes = employeRepository.findAll();
        //Assertions.assertThat(employes).hasSize(1);

        //On fait le test d'intégration
        //Employe employe = employeRepository.findAll().get(0);
        //Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        //=>on peut utiliser le repo autrement avec
        Employe employe = employeRepository.findByMatricule("T00001");
        Assertions.assertThat(employe).isNotNull();

        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    // On test un commercial qui a une performance = 2,
    // mais qui a réussi ses objectifs (cas3: caTraite = objectifCa) sa performance reste la même (on n'a pas la performance de base)
    @Test
    void testCalculPerformanceCommercial() throws EmployeException{
        //Given
        String nom = "Doe";
        String prenom = "John";
        Double tempsPartiel = 1.0;
        Integer performance = 2;
        String matricule = "C00002";

        //pour pouvoir tester ce TI de façon indépendante, j'ai dû faire un enregistrement indépendant de cet employe
        Employe employe = new Employe(nom, prenom, matricule, LocalDate.now(), Entreprise.SALAIRE_BASE, performance, tempsPartiel);
        employeRepository.save(employe);

        employeService.calculPerformanceCommercial(matricule, 50l, 50l);

        //Test d'intégration
        employe = employeRepository.findByMatricule(matricule);

        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getPerformance()).isEqualTo(2);
    }


}