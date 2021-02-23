package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;


class EmployeTest {

    /////////////////////////////////////////Test Unitaire
    @Test // un test basique
    void testGetNombreAnneeAncienneteAvecDateEmbaucheNull(){
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer duree = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(duree).isNull();
    }

    @Test // un test basique
    void testGetNombreAnneeAncienneteAvecDateEmbaucheInferieurNow(){
        // Given
        Employe employe = new Employe();
        employe.setNom("Doe");
        employe.setPrenom("John");
        employe.setMatricule("T12345");
        employe.setDateEmbauche(LocalDate.now().minusYears(6));
        employe.setSalaire(1500d);
        employe.setPerformance(1);
        employe.setTempsPartiel(1.0);

        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anneeAnciennete).isEqualTo(6);
    }


    @Test // un test basique
    void testGetNombreAnneeAncienneteAvecDateEmbaucheSuperieurNow(){
        // Given
        Employe employe = new Employe("Doe", "John", "T12345", LocalDate.now().plusYears(6), 1500d, 1, 1.0);

        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }


    @Test
    void testGetNbAnneeAncienneteDateEmbaucheNow(){
        //Given
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now(), 1500d, 1, 1.0);
        //When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();
        //Then
        Assertions.assertThat(anneeAnciennete).isZero();
    }


    //////////////////////////////////////Tests préparés
    //Pour la méthode getPrimeAnnuelle()
    //1) les entrées
    //date d'embauche (car la prime n'est pas une entrée en tant que tel)
    //indice manager
    //indice performance
    //temps partiel (% d'activité)

    //2) scénario faisant varier les entrées
    //employé simple sans ancienneté
    //employé simple avec ancienneté
    //employé avec performance
    //employé est un manager/technicien sont gérés de la même manière
    //employé est à temps partiel

    //3) écrire un TU simple qui teste un des scénarios

    //4) dupliquer et transformer le scénario en test préparé testant tous les scénarios


    @Test //version TU simple
    void testGetPrimeAnnuellePourUnEmployeATempsPartiel(){
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());
        employe.setTempsPartiel(0.5);

        Double prime = employe.getPrimeAnnuelle();

        //primme annuelle de base = 1000d
        Assertions.assertThat(prime).isEqualTo(500);

    }

    @Test // exemple correction de TU
    void testGetPrimeAnnuelle(){
        //Given
        Integer performance = 1; //voir dans la classe correspondant
        String matricule = "T12345";
        Double tauxActivite = 1.0;
        Long nbAnneeAnciennete = 0L;

        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneeAnciennete), 1500d, performance, tauxActivite);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Double primeAttendue = 1000D; //dans notre cas, on va la prime de base donc 1000 -> vu dans la classe
        Assertions.assertThat(prime).isEqualTo(primeAttendue);

    }

//    ////version Test paramétré
//    @ParameterizedTest
//    @CsvSource({
//            "1, 'T12345', 1.0, 0"
//    })
//    public void testGetPrimeAnnuelleVersionTestPrepare(
//            Integer performance, String matricule, Double tauxActivite, Long nbAnneeAnciennete){
//
//        //Given, When, Then
//        Employe employe = new Employe("Doe", "John", matricule,
//                LocalDate.now().minusYears(nbAnneeAnciennete), 1500d, performance, tauxActivite);
//        Double primeAttendue = 1000D;
//
//        Assertions.assertThat(employe.getPrimeAnnuelle()).isEqualTo(primeAttendue);
//    }


    ////version Test paramétré Corrigé ==> cas normaux
    @ParameterizedTest(name = "Perf {0}, matricule {1}, txActivite {2}, anciennete {3}, => prime {4}") //permet d'avoir une version nommée du test
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000",
            "1, 'T12345', 0.5, 0, 500",  //un temps partiel
            "2, 'T12345', 1.0, 0, 2300", //une meilleure performance
            "1, 'T12345', 1.0, 2, 1200",  //avec 2 ans d'anciennetée
            "1, 'M12345', 1.0, 0, 1700",  //on test un manager
            "2, 'T12345', 1.0, 1, 2400",  //pour tester le cas du code zombie (ligne 99)
            "1, 'M12345', 1.0, 3, 2000",  //pour le code zombie de la (ligne 103)
    })
    void testGetPrimeAnnuelleVersionTestPrepareCasNormaux(
            Integer performance, String matricule, Double tauxActivite, Long nbAnneeAnciennete, Double primeAttendue){

        //Given, When, Then
        Employe employe = new Employe("Doe", "John", matricule,
                LocalDate.now().minusYears(nbAnneeAnciennete), 1500d, performance, tauxActivite);

        Double prime = employe.getPrimeAnnuelle();

        Assertions.assertThat(prime).isEqualTo(primeAttendue);
    }

    ////Un test limite
    //Les tests limites doivent être plutôt géré en TU
    @Test
    void testGetPrimeAnnuelleMatriculeNull(){

        //Given, When, Then
        Employe employe = new Employe("Doe", "John", null,
                LocalDate.now().minusYears(0), 1500d, 1, 1.0);

        Double prime = employe.getPrimeAnnuelle();

        Assertions.assertThat(prime).isEqualTo(1000);
    }

    @Test
    void testGetPrimeAnnuellePerformanceNull(){

        //Given, When, Then
        Employe employe = new Employe("Doe", "John", "T12345",
                LocalDate.now().minusYears(0), 2000d, null, 1.0);

        Double prime = employe.getPrimeAnnuelle();

        Assertions.assertThat(prime).isEqualTo(1000); // = primeAnnuelle de Base
    }



    //////////////////////////////////Test de mutation
    //il s'agit du plugin pitest (dans le pom.xml)
    //désormais il est dans le goal maven
    //pour lancer aller dans l'onglet maven (à gauche)
    //dans plugins
    //puis pitest:mutationCoverage
    //aller dans target (arborescence à droite)
    //dans pit-reports
    //choisir le dernier dossier (avec un numéro bizzare)
    //et sur index.html
    //clique droit et open browser

    //pour nettoyer aller dans onglet maven
    //lifestyle
    //clean (supprime les anciens rapports)
    //install/compile (réinstalle le plugin, même si rouge faire un test pitest)
    //et relancer le plugin pitest ==> si tout ok message build ok


    ///////////////////////////////////////ZONE de l'évalVIVI///////////////////////////////////////////////////////////
    //TDD
    //
    //liste idée
    //simpleAugmentation
    //salaire : null ou 0
    //% : 0 ou négatif ou null
    //salaire2ChiffreApresVirgule
    //?? test paramétré avec des tests inférieurs salaire de base et % négatif
    //
    //
    @Test // simple augmentation 10% => 1673.342
    void testAugmenterSalaireSimpleAugmentation() throws EmployeException {
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1000.00); //entreprise.SALAIRE_BASE = 1521.22, augmentation 10% => 1673.342
        Double pourcentage = 0.1; // augmentation 10%

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1100);
    }

    // Tu : salaire = null
    @Test //entreprise.SALAIRE_BASE = 1521.22, augmentation 10% => 1673.342
    void testAugmenterSalaireSiSalaireNullFaireCalculAvecUnSalaireBaseEntreprise() throws EmployeException{
        // Given
        Employe employe = new Employe();
        employe.setSalaire(null);
        Double pourcentage = 0.1;

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1673.34);
    }

    // TU : salaire = zéro traitement idem que si null
    @Test //entreprise.SALAIRE_BASE = 1521.22, augmentation 10% => 1673.342
    void testAugmenterSalaireSiSalaireAZeroFaireCalculAvecUnSalaireBaseEntreprise() throws EmployeException{
        // Given
        Employe employe = new Employe();
        employe.setSalaire(0.0);
        Double pourcentage = 0.1;

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1673.34);
    }

    // TU : pourcentage = 0
    @Test //entreprise.SALAIRE_BASE = 1521.22
    void testAugmenterSalaireSiPourcentageAZero() throws EmployeException{
        // Given
        Employe employe = new Employe();
        employe.setSalaire(null);
        Double pourcentage = 0d;

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1521.22);
    }

    // TU : pourcentage = négatif => gestion d'une exception
    @Test //entreprise.SALAIRE_BASE = 1521.22, pourcentage -10% => 1369.098 ==> exception on peut pas faire une augmentation avec un % négatif
    void testAugmenterSalaireSiPourcentageNegatif() throws EmployeException{
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1000.0);
        Double pourcentage = -0.1;

        //Then
        Assertions.assertThatThrownBy(() -> employe.augmenterSalaire(pourcentage)).hasMessage("ERREUR : le pourcentage est négatif");
    }

    // TU : pourcentage = null => abandonné ... test non faisable

    // TU : salaire2ChiffreApresVirgule
    @Test //entreprise.SALAIRE_BASE = 1521.22, pourcentage 10% => 1673.34
    void testAugmenterSalaireSalaireDeuxChiffresMax() throws EmployeException{
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1521.22);
        Double pourcentage = 0.1;

        //When
        employe.augmenterSalaire(pourcentage);

        //Then
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1673.34);
    }


    //Test paramétré pour le NbRTT
    @ParameterizedTest(name = "Année {0}, = Nombre de RTT {1}")
    @CsvSource({
            "'2019', 8", // (365 - 218 - 104 - 25 - 10 = 8) * 1 => un temps plein
            "'2021', 10", // (365 - 218 - 104 - 25 - 7 = 11)
            "'2022', 10", // (365 - 218 - 105 - 25 - 7 = 10)
            "'2032', 11", // (366 - 218 - 104 - 25 - 7 = 12)
            "'2026', 9", // pour tester le jeudi
            "'2016', 9" // pour tester le vendredi (pour cela il a fallut ajouter la date de pâque dans Entreprise)
    })
    void testGetNbRtt(Integer dateReference, Integer nbRttAttendu){
        Employe employe = new Employe("Doe", "John", null,
                LocalDate.now(), 1500d, 1, 1.0);

        Integer nb = employe.getNbRtt(LocalDate.of(dateReference,1,1));
        Assertions.assertThat(nb).isEqualTo(nbRttAttendu);
    }

    //Ajoute d'un test pour Calculer le Nb de congé
    @Test
    void testGetNbConges(){
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now());

        //When
        Integer nb = employe.getNbConges();

        //Then
        Assertions.assertThat(nb).isEqualTo(25); //= nombre de congé de base
    }


}
