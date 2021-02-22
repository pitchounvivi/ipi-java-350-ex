package com.ipiecoles.java.java350.repository;


import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

//@ExtendWith(SpringExtension.class) //Junit 5
//@DataJpaTest  // ou @SpringBootTest ==> cette ligne suffit pour tester le repo tout seul (mais il faut aussi celle du dessus)
//@ContextConfiguration(classes = {Java350Application.class}) //Pour définir le contexte de l'appli (spécifique pour l'appli (car il y a son nom))

@SpringBootTest //==> dans le cas ou on utilise Spring Boot seule cette ligne suffit (pour définir le context)
class EmployeRepositoryTest {

    //on importe la classe pour la tester
    @Autowired
    EmployeRepository employeRepository;


//
//    //En exemple, toutes les méthodes (commande @) qui s'effectuent avant ou après les tests :
//
//        @BeforeAll // Junit 5
//        public void setUp(){//Nom setUp arbitraire
//            //Appelé une seule fois avant l'exécution des tests
//        }
//        @BeforeEach // Junit 5
//        public void before(){//Nom before arbitraire
//            //Appelé avant chaque test
//        }
//        @AfterEach //Junit 5
//        public void after(){//Nom after arbitraire
//            //Appelé après chaque test
//        }
//        @AfterAll // Junit 5
//        public void tearDown(){//Nom tearDown arbitraire
//            //Appelé une fois que tous les tests sont passés
//        }
//

    //Pour régler le problème d'étanchéité
    @BeforeEach //à faire avant chaque tests
    @AfterEach //a faire après chaque tests
    void purgeBDD(){//Nom setUp arbitraire
            employeRepository.deleteAll();
        }
    //==> contrairement à ceux en All qui se font avant/après avoir effectués tous les tests


    //TU (<= même si on fait un peu d'insertion)  pour tester le dernier numéro de matricule enregistré en bdd
    @Test
    void testFindLastMatriculeUnEmploye(){
        //given
        //insert des données en base
        employeRepository.save(new Employe("Doe", "John", "T012345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "T2345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "T345", LocalDate.now(), 1500d, 1, 1.0));

        //when
        //execute la requête dans la bdd
        String lastMatricule = employeRepository.findLastMatricule(); //la méthode enlève la lettre pour ne donner que le nombre

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("345");
    }


    //TU : ATTENTION PROBLEME D'ETANCHEITE DES TESTS car si on teste les 2 méthodes la deuxième plante car pour lui il y a des employé en bdd
    //il y a une sorte d'héritage des méthodes précédentes
    @Test
    void testFindLastMatricule0Employe(){
        //given
        //insert des données en base

        //when
        //execute la requête dans la bdd
        String lastMatricule = employeRepository.findLastMatricule(); //la méthode enlève la lettre pour ne donner que le nombre

        //Then
        Assertions.assertThat(lastMatricule).isNull(); //car on test 0
    }

    //test avec des matricule très différentes permet de vérifier que l'on se base que sur la partie numérique
    @Test
    void testFindLastMatriculeNEmploye(){
        //given
        //insert des données en base
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "J", "M42345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jan", "T2345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jim", "C01345", LocalDate.now(), 1500d, 1, 1.0));

        //when
        //execute la requête dans la bdd
        String lastMatricule = employeRepository.findLastMatricule(); //la méthode enlève la lettre pour ne donner que le nombre

        //Then
        Assertions.assertThat(lastMatricule).isEqualTo("42345");
    }

}