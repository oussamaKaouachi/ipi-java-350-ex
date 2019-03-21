package com.ipiecoles.java.java350.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeTest {

    @Test
    public void testGetNombreAnneeAncienneteDateEmbaucheNull(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(null);

        //When
        Integer nbAnneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, (int)nbAnneeAnciennete);
    }

    @Test
    public void testGetNombreAnneeAncienneteNminus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(2,anneeAnciennete.intValue());
    }

    @Test
    public void testGetNombreAnneeAncienneteNplus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0,anneeAnciennete.intValue());
    }

    @ParameterizedTest(name = "immat {0} est valide : {1}")
    @CsvSource({
            "'XXXXXX',false",
            "'AA-123-BB',true'"
    })
    void testCheckBadImmatriculation(String immat, Boolean result){
        //Given
        Employe e = new Employe();

        //When
        String matricule = e.getMatricule();

        //Then
        Assertions.assertEquals(4,matricule);
    }

    @ParameterizedTest(name = "Employé de matricule {1}, perf {0}, ancienneté {2}, tps partiel : {3}, => prime{4}")
    @CsvSource({
            "'1',T12345,,1.0,1000.0",
            "'1',M12345,,1.0,1700.0",
    })
    public void getPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnulle){
        //Given
        Employe employe = new Employe("Doe","John",matricule,LocalDate.now().minusYears(nbYearsAnciennete),Entreprise.SALAIRE_BASE,performance,tempsPartiel);

        //When
        Double PrimeAnnuelle = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertEquals(primeAnnulle,PrimeAnnuelle);
    }

}