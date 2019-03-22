package com.ipiecoles.java.java350;

import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class Java350Application {

    public static void main(String[] args) {
        SpringApplication.run(Java350Application.class, args);
        Employe e = new Employe();
        LocalDate date = LocalDate.now().plusYears(2L);
        System.out.println(e.getNbRtt(date));
    }

}