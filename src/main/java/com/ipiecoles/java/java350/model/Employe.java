package com.ipiecoles.java.java350.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Employe {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nom;

    private String prenom;

    private String matricule;

    private LocalDate dateEmbauche;

    private Double salaire = Entreprise.SALAIRE_BASE;

    private Integer performance = Entreprise.PERFORMANCE_BASE;

    private Double tempsPartiel = 1.0;

    public Employe() {
    }

    public Employe(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateEmbauche = dateEmbauche;
        this.salaire = salaire;
        this.performance = performance;
        this.tempsPartiel = tempsPartiel;
    }

    public Integer getNombreAnneeAnciennete() {
        if (dateEmbauche != null){
            if(dateEmbauche.getYear() > LocalDate.now().getYear()){
                return 0;
            }
            return LocalDate.now().getYear() - dateEmbauche.getYear();
        }


        return 0;
    }

    public Integer getNbConges() {
        return Entreprise.NB_CONGES_BASE + this.getNombreAnneeAnciennete();
    }

    public Integer getNbRtt(){
        return getNbRtt(LocalDate.now());
    }

    /**
     * Calcul du nombre de RTT
     * nbJoursAnnee : dépend de si l'année est bissextile ou non
     * nbJoursReposHebdomadaire : nombre de samedi et dimanche dans l'année.
     * Ce nombre est modifié si le 1er janvier tombe un jeudi, un vendredi (et si l'année est bissextile ou non), un samedi.
     * nbJoursFeries : nombre de jours fériés dans l'année qui tombe un jour de la semaine (lundi au vendredi)
     * Entreprise.NB_JOURS_MAX_FORFAIT : le nombre de jours travaillés dans l'année par les salariés de l'entreprise
     * Entreprise.NB_CONGES_BASE : nombre de jours de congés de base des salariés (cadre légal ou conventionnel)
     * On obtient le nombre de RTT en partant du nombre de jours dans l'année auquel on soustrait les variables précédemment définies,
     * on multiplie ensuite cela par l'attribut temps partiel de l'employé (s'il est à temps partiel, il n'aura pas le même nombre de jours de Rtt)
     * @param d (date actuelle)
     * @return le nombre de jours de Rtt de l'employé
     */
    public Integer getNbRtt(LocalDate d){
        int nbJoursAnnee = d.isLeapYear() ? 366 : 365;
        int nbJoursReposHebdomadaire = 104;
        switch (LocalDate.of(d.getYear(),1,1).getDayOfWeek()){
            case THURSDAY: if(d.isLeapYear()) nbJoursReposHebdomadaire =  nbJoursReposHebdomadaire + 1; break;
            case FRIDAY: if(d.isLeapYear()) nbJoursReposHebdomadaire =  nbJoursReposHebdomadaire + 2; else nbJoursReposHebdomadaire =  nbJoursReposHebdomadaire + 1; break;
            case SATURDAY: nbJoursReposHebdomadaire = nbJoursReposHebdomadaire + 1; break;
        }
        int nbJoursFeries = (int) Entreprise.joursFeries(d).stream().filter(localDate -> localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue()).count();
        return (int) Math.ceil((nbJoursAnnee - Entreprise.NB_JOURS_MAX_FORFAIT - nbJoursReposHebdomadaire - Entreprise.NB_CONGES_BASE - nbJoursFeries) * tempsPartiel);
    }

    /**
     * Calcul de la prime annuelle selon la règle :
     * Pour les managers : Prime annuelle de base bonnifiée par l'indice prime manager
     * Pour les autres employés, la prime de base plus éventuellement la prime de performance calculée si l'employé
     * n'a pas la performance de base, en multipliant la prime de base par un l'indice de performance
     * (égal à la performance à laquelle on ajoute l'indice de prime de base)
     *
     * Pour tous les employés, une prime supplémentaire d'ancienneté est ajoutée en multipliant le nombre d'année
     * d'ancienneté avec la prime d'ancienneté. La prime est calculée au pro rata du temps de travail de l'employé
     *
     * @return la prime annuelle de l'employé en Euros et cents
     */
    public Double getPrimeAnnuelle(){
        //Calcule de la prime d'ancienneté
        Double primeAnciennete = Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete();
        Double prime;
        //Prime du manager (matricule commençant par M) : Prime annuelle de base multipliée par l'indice prime manager
        //plus la prime d'anciennté.
        if(matricule != null && matricule.startsWith("M")) {
            prime = Entreprise.primeAnnuelleBase() * Entreprise.INDICE_PRIME_MANAGER + primeAnciennete;
        }
        //Pour les autres employés en performance de base, uniquement la prime annuelle plus la prime d'ancienneté.
        else if (this.performance == null || Entreprise.PERFORMANCE_BASE == this.performance){
            prime = Entreprise.primeAnnuelleBase() + primeAnciennete;
        }
        //Pour les employés plus performance, on bonnifie la prime de base en multipliant par la performance de l'employé
        // et l'indice de prime de base.
        else {
            prime = Entreprise.primeAnnuelleBase() * (this.performance + Entreprise.INDICE_PRIME_BASE) + primeAnciennete;
        }
        //Au pro rata du temps partiel.
        return prime * this.tempsPartiel;
    }


    public void augmenterSalaire(Integer pourcentage){

        if (this.getSalaire() == 0) {}
        else if(pourcentage <= 0){}
        else {
            this.setSalaire(this.getSalaire() + (this.getSalaire() * pourcentage / 100));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * @param matricule the matricule to set
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * @return the dateEmbauche
     */
    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    /**
     * @param dateEmbauche the dateEmbauche to set
     */
    public void setDateEmbauche(LocalDate dateEmbauche) {
        if (dateEmbauche == null){
            this.dateEmbauche = LocalDate.now();
        }else {
            this.dateEmbauche = dateEmbauche;
        }
    }

    /**
     * @return the salaire
     */
    public Double getSalaire() {
            return salaire;
    }

    /**
     * @param salaire the salaire to set
     */
    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Integer getPerformance() {
        return performance;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    public Double getTempsPartiel() {
        return tempsPartiel;
    }

    public void setTempsPartiel(Double tempsPartiel) {
        this.tempsPartiel = tempsPartiel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employe)) return false;
        Employe employe = (Employe) o;
        return Objects.equals(id, employe.id) &&
                Objects.equals(nom, employe.nom) &&
                Objects.equals(prenom, employe.prenom) &&
                Objects.equals(matricule, employe.matricule) &&
                Objects.equals(dateEmbauche, employe.dateEmbauche) &&
                Objects.equals(salaire, employe.salaire) &&
                Objects.equals(performance, employe.performance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, matricule, dateEmbauche, salaire, performance);
    }
}
