package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class EmployeRepositoryTest {

    @Autowired
    private EmployeRepository employeRepository;

    // le BeforeAll est exécuté avant le contexte Spring, on ne peut donc pas utiliser de méthode des repository
    // Si à la place de @SpringBootTest on met @DataJpaTest, on n'a pas besoin de mettre ce qui suit, JUnit videra les données avant chaque test
    @BeforeEach
    @AfterEach
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void testFindLastMatriculeNull(){

        //Given

        //When
        String lastMatricule = employeRepository.findLastMatricule();

        //Then
        assertNull(lastMatricule);
    }

    @Test
    public void testFindLastMatriculeSingle(){

        Employe e = new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);
        employeRepository.save(e);

        String lastMatricule = employeRepository.findLastMatricule();

        assertEquals("12345", lastMatricule);
    }

    @Test
    public void testFindLastMatriculeMultiple(){

        Employe e = new Employe("Doe", "John", "T12345", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);
        employeRepository.save(e);
        Employe f = new Employe("Spacey", "Kevin", "C45678", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);
        employeRepository.save(f);
        Employe g = new Employe("Philippe", "Philippe", "M23456", LocalDate.now() ,Entreprise.SALAIRE_BASE, 1, 1.0);
        employeRepository.save(g);

        String lastMatricule = employeRepository.findLastMatricule();

        assertEquals("45678", lastMatricule);
    }
}
