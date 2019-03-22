package com.ipiecoles.java.java350.model;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetNombreAnneeAncienneteDateEmbaucheNull() {

        //Given
        Employe e = new Employe();
        e.setDateEmbauche(null);

        //When
        Integer nbAnneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, (int)nbAnneeAnciennete);
    }

    @Test
    public void testGetNombreAnneeAncienneteNow(){

        Employe e = new Employe();
        LocalDate date = LocalDate.now();
        e.setDateEmbauche(date);

        Integer nbAnneeAnciennete = e.getNombreAnneeAnciennete();

        Assertions.assertEquals(0, nbAnneeAnciennete.intValue());
    }

    @Test
    public void testGetNombreAnneeAncienneteNPlus2(){

        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().plusYears(2L));

        Integer nbAnneeAnciennete = e.getNombreAnneeAnciennete();

        Assertions.assertEquals(0, nbAnneeAnciennete.intValue());
    }

    @Test
    public void testGetNombreAnneeAncienneteNminus2(){
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));

        Integer nbAnneeAnciennete = e.getNombreAnneeAnciennete();

        Assertions.assertEquals(2, nbAnneeAnciennete.intValue());
    }

    // Manager -> avec ancienneté
    // Manager -> sans ancienneté
    // Manager selon sa performance
    // Employé autre avec performance de base
    // Employé autre avec performance
    // Employé en temps partiel
   @ParameterizedTest(name = "Employé de matricule {1}, perf {0}, ancienneté {2}, tps partiel : {3}")
   @CsvSource({
           "1, 'T12345', 0, 1.0, 1000.0",
           "1, 'T12345', 2, 0.5, 600.0",
           "1, 'T12345', 2, 1.0, 1200.0",
           "2, 'T12345', 0, 1.0, 2300.0",
           "2, 'T12345', 1, 1.0, 2400.0",
           "1, 'M12345', 0, 1.0, 1700.0",
           "1, 'M12345', 5, 1.0, 2200.0",
           "2, 'M12345', 0, 1.0, 1700.0",
           "2, 'M12345', 8, 1.0, 2500.0"
   })
   public void testGetPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnuelle){
        // Given
       Employe e = new Employe("nom", "prenom", matricule, LocalDate.now().minusYears(nbYearsAnciennete),Entreprise.SALAIRE_BASE, performance, tempsPartiel);

       // When
       Double prime = e.getPrimeAnnuelle();


       // Then
       Assertions.assertEquals(primeAnnuelle, prime);
   }

   @ParameterizedTest
   @CsvSource({
           "2019-01-01, 8", // cas nominal
           "2020-01-01, 10", // année bissextile
           "2022-01-01, 10", // année normale avec nombre de jours fériés différent
           "2021-01-01, 10" // année où jour de l'an est un vendredi
            // année bissextile où jour de l'an est un jeudi, vendredi ou samedi -> 2016 mais ne fonctionne pas vu que c'est une date antérieure, sinon 2028 mais on n'a pas les dates des jours feriés
   })
   public void testGetNbRtt(LocalDate date,Integer nbRttExpected){
       //GIVEN
       Employe e = new Employe();
       
       //WHEN
       Integer nbRtt = e.getNbRtt(date);
       
       //GIVEN
       Assertions.assertEquals(nbRtt, nbRttExpected);
       System.out.println("nb rtt : "+nbRtt);
   }
}
