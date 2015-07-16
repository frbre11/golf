/*
 * Partie.java
 * 
 * (C) 2013 Université Laval. Tous droits réservés.
 */
package ca.ulaval.dti.calculateurhandicapgolf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

public class Partie implements Comparable<Partie> {

    private static int dernierId = 0;
    private int id;
    private Date date;
    private String nomClub;
    private int score;
    private String scoreStr;
    private double evaluationTerrain;
    private String evaluationTerrainStr;
    private int evaluationSlope;
    private String evaluationSlopeStr;
    private static ArrayList<Partie> parties;

    public static final String NOM_CLUB_OBLIGATOIRE = "E001";
    public static final String SCORE_INVALIDE = "E002";
    public static final String EVALUATION_TERRAIN_INVALIDE = "E003";
    public static final String EVALUATION_SLOPE_INVALIDE = "E004";

    public Partie() {
        this.id = ++dernierId;
        this.date = new Date();
        this.nomClub = "";
        setScore(0);
        setEvaluationTerrain(0.0);
        setEvaluationSlope(0);
    }

    public Partie(int id, Date date, String nomClub, int score, double evaluationTerrain, int evaluationSlope) {
        this.id = ++dernierId;
        this.date = date;
        this.nomClub = nomClub;
        setScore(score);
        setEvaluationTerrain(evaluationTerrain);
        setEvaluationSlope(evaluationSlope);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @NotBlank(message = NOM_CLUB_OBLIGATOIRE)
    public String getNomClub() {
        return nomClub;
    }

    public void setNomClub(String nomClub) {
        this.nomClub = nomClub;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
        this.scoreStr = Integer.toString(score);
    }

    @Pattern(regexp = "^[1-9]\\d{0,2}$", message = SCORE_INVALIDE)
    public String getScoreStr() {
        return scoreStr;
    }

    public void setScoreStr(String scoreStr) {
        this.scoreStr = scoreStr;
        try {
            this.score = Integer.parseInt(scoreStr);
        } catch (NumberFormatException nfe) {
        }
    }

    public double getEvaluationTerrain() {
        return evaluationTerrain;
    }

    public void setEvaluationTerrain(double evaluationTerrain) {
        this.evaluationTerrain = evaluationTerrain;
        this.evaluationTerrainStr = Double.toString(evaluationTerrain);
    }

    @Pattern(regexp = "^[1-9]\\d{0,2}\\.\\d{1}$", message = EVALUATION_TERRAIN_INVALIDE)
    public String getEvaluationTerrainStr() {
        return evaluationTerrainStr;
    }

    public void setEvaluationTerrainStr(String evaluationTerrainStr) {
        this.evaluationTerrainStr = evaluationTerrainStr;
        try {
            this.evaluationTerrain = Double.parseDouble(evaluationTerrainStr);
        } catch (NumberFormatException nfe) {
        }
    }

    public int getEvaluationSlope() {
        return evaluationSlope;
    }

    public void setEvaluationSlope(int evaluationSlope) {
        this.evaluationSlope = evaluationSlope;
        this.evaluationSlopeStr = Integer.toString(evaluationSlope);
    }

    @Pattern(regexp = "^[1-9]\\d{0,2}$", message = EVALUATION_SLOPE_INVALIDE)
    public String getEvaluationSlopeStr() {
        return evaluationSlopeStr;
    }

    public void setEvaluationSlopeStr(String evaluationSlopeStr) {
        this.evaluationSlopeStr = evaluationSlopeStr;
        try {
            this.evaluationSlope = Integer.parseInt(evaluationSlopeStr);
        } catch (NumberFormatException nfe) {
        }
    }

    static {
        parties = new ArrayList<Partie>();
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 5, 15);
        parties.add(new Partie(1, cal.getTime(), "Royal Québec", 86, 72.0, 123));
        cal.set(2012, 5, 29);
        parties.add(new Partie(2, cal.getTime(), "Cap Rouge", 90, 72.0, 124));
        cal.set(2012, 7, 1);
        parties.add(new Partie(3, cal.getTime(), "Lévis", 87, 72.0, 128));
        cal.set(2012, 7, 10);
        parties.add(new Partie(4, cal.getTime(), "Mont Sainte Anne", 91, 70.1, 128));
        cal.set(2012, 8, 2);
        parties.add(new Partie(5, cal.getTime(), "Saint Nicolas", 85, 72.0, 123));
        cal.set(2012, 8, 9);
        parties.add(new Partie(6, cal.getTime(), "Stoneham", 81, 72.0, 128));
    }

    public static List<Partie> chargerParties() {
        return parties;
    }

    @Override
    public int compareTo(Partie o) {
        return this.date.compareTo(o.getDate());
    }
}
