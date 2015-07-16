/*
 * CalculateurHandicapgolfGolfApplication.java
 * 
 * (C) 2013 Université Laval. Tous droits réservés.
 */
package ca.ulaval.dti.calculateurhandicapgolf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import ca.ulaval.dti.calculateurhandicapgolf.Partie;
import ca.ulaval.dti.calculateurhandicapgolf.TextField;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.UserError;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
@PreserveOnRefresh
@Theme("validation")
public class CalculateurHandicapGolfUI extends UI {

    private Table tblParties;
    private VerticalLayout vtlPrincipal;
    private HorizontalLayout hrlAjouterSupprimer;
    private HorizontalLayout hrlHandicap;
    private HorizontalLayout hrlMoyenne;
    private Button btnAjouter;
    private Button btnSupprimer;
    private Button btnCalculerHandicap;
    private Button btnCalculerMoyenne;
    private Label lblHandicap;
    private Label lblMoyenne;
    private javax.validation.Validator validateur;
    private BeanItemContainer<Partie> cntParties;
    private HashMap<String, String> messagesErreur;
    {
        messagesErreur = new HashMap<String, String>();
        messagesErreur.put(Partie.NOM_CLUB_OBLIGATOIRE, "Le nom du club est obligatoire");
        messagesErreur.put(Partie.SCORE_INVALIDE, "Le score est invalide (NNN)");
        messagesErreur.put(Partie.EVALUATION_TERRAIN_INVALIDE, "L'évaluation du terrain est invalide (NNN.N)");
        messagesErreur.put(Partie.EVALUATION_SLOPE_INVALIDE, "L'évaluation du slope est invalide (NNN)");
    }
    private HashMap<Object, HashMap<String, AbstractField<?>>> fields;

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Calculateur de Handicap au Golf");
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validateur = factory.getValidator();
        vtlPrincipal = new VerticalLayout();
        vtlPrincipal.setSpacing(true);
        setContent(vtlPrincipal);
        vtlPrincipal.addComponent(new Label("<h1><center>Calculateur de Handicap au Golf</center></h1>",
                ContentMode.HTML));
        tblParties = new Table();
        cntParties = new BeanItemContainer<Partie>(Partie.class);
        cntParties.addAll(Partie.chargerParties());
        tblParties.setContainerDataSource(cntParties);
        tblParties.setVisibleColumns(new Object[] { "id", "date", "nomClub", "scoreStr", "evaluationTerrainStr",
                "evaluationSlopeStr" });
        tblParties.setColumnHeaders(new String[] { "Id", "Date", "Nom du club", "Score", "Évaluation du terrain",
                "Évaluation du slope" });
        tblParties.setEditable(true);
        tblParties.setImmediate(true);
        tblParties.setSelectable(true);
        tblParties.setColumnWidth("id", 50);
        fields = new HashMap<Object, HashMap<String, AbstractField<?>>>();
        tblParties.setTableFieldFactory(new DefaultFieldFactory() {

            @Override
            public Field<?> createField(Container container, Object itemId, Object propertyId, Component uiContext) {
                Field<?> field = null;
                if (propertyId.equals("id") || propertyId.equals("nomClub") || propertyId.equals("scoreStr")
                        || propertyId.equals("evaluationTerrainStr") || propertyId.equals("evaluationSlopeStr")) {
                    field = new TextField();
                    if (propertyId.equals("id")) {
                        field.setReadOnly(true);
                    }
                } else {
                    field = super.createField(container, itemId, propertyId, uiContext);
                }
                if (field instanceof AbstractField) {
                    if (fields.get(itemId) == null) {
                        fields.put(itemId, new HashMap<String, AbstractField<?>>());
                    }
                    fields.get(itemId).put((String) propertyId, (AbstractField<?>) field);
                }
                return field;
            }
        });
        tblParties.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(ValueChangeEvent event) {
                activerDesactiverControles();
            }
        });
        vtlPrincipal.addComponent(tblParties);
        hrlAjouterSupprimer = new HorizontalLayout();
        hrlAjouterSupprimer.setSpacing(true);
        hrlAjouterSupprimer.setSpacing(true);
        vtlPrincipal.addComponent(hrlAjouterSupprimer);
        btnAjouter = new Button("Ajouter");
        hrlAjouterSupprimer.addComponent(btnAjouter);
        btnSupprimer = new Button("Supprimer");
        hrlAjouterSupprimer.addComponent(btnSupprimer);
        btnAjouter.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                cntParties.addBean(new Partie());
                tblParties.setPageLength(cntParties.size());
            }
        });
        btnSupprimer.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (tblParties.getValue() != null) {
                    cntParties.removeItem(tblParties.getValue());
                    tblParties.setPageLength(cntParties.size());
                    activerDesactiverControles();
                }
            }
        });
        hrlHandicap = new HorizontalLayout();
        hrlHandicap.setSpacing(true);
        hrlHandicap.setSpacing(true);
        vtlPrincipal.addComponent(hrlHandicap);
        btnCalculerHandicap = new Button("Calculer Handicap");
        hrlHandicap.addComponent(btnCalculerHandicap);
        hrlHandicap.addComponent(new Label("Handicap:"));
        lblHandicap = new Label("0");
        hrlHandicap.addComponent(lblHandicap);
        hrlMoyenne = new HorizontalLayout();
        hrlMoyenne.setSpacing(true);
        hrlMoyenne.setSpacing(true);
        vtlPrincipal.addComponent(hrlMoyenne);
        btnCalculerMoyenne = new Button("Calculer Moyenne");
        hrlMoyenne.addComponent(btnCalculerMoyenne);
        hrlMoyenne.addComponent(new Label("Moyenne:"));
        lblMoyenne = new Label("0");
        hrlMoyenne.addComponent(lblMoyenne);
        btnCalculerHandicap.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (validerPartie(cntParties)) {
                    lblHandicap.setValue(Double.toString(calculerHandicap()));
                }
            }
        });
        btnCalculerMoyenne.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                if (validerPartie(cntParties)) {
                    lblMoyenne.setValue(Integer.toString(calculerMoyenne()));
                }
            }
        });

        activerDesactiverControles();
        tblParties.setPageLength(cntParties.size());
    }

    private void activerDesactiverControles() {
        boolean itemSelectionne = (tblParties.getValue() != null);
        btnSupprimer.setEnabled(itemSelectionne);
    }

    private double calculerHandicap() {
        double handicap = 0.0;
        ArrayList<Partie> parties = new ArrayList<Partie>();
        for (Partie partie : cntParties.getItemIds()) {
            if (partie.getScore() != 0 && partie.getEvaluationTerrain() != 0.0 && partie.getEvaluationSlope() != 0
                    && partie.getDate() != null) {
                parties.add(partie);
            }
            if (parties.size() >= 20) {
                break;
            }
        }
        if (parties.size() >= 5) {
            double differentiel = 0.0;
            Collections.sort(parties);
            ArrayList<Double> differentiels = new ArrayList<Double>();
            for (Partie partie : cntParties.getItemIds()) {
                differentiel = ((((double) partie.getScore() - partie.getEvaluationTerrain()) * 113) / (double) partie.getEvaluationSlope());
                differentiels.add(differentiel);
            }
            Collections.sort(differentiels);
            int n = 0;
            switch (differentiels.size()) {
                case 5:
                case 6:
                    n = 1;
                    break;
                case 7:
                case 8:
                    n = 2;
                    break;
                case 9:
                case 10:
                    n = 3;
                    break;
                case 11:
                case 12:
                    n = 4;
                    break;
                case 13:
                case 14:
                    n = 5;
                    break;
                case 15:
                case 16:
                    n = 6;
                    break;
                case 17:
                    n = 7;
                    break;
                case 18:
                    n = 8;
                    break;
                case 19:
                    n = 9;
                    break;
                case 20:
                    n = 10;
                    break;
            }
            double sommeDifferentiels = 0.0;
            for (int i = 0; i < n; i++) {
                sommeDifferentiels += differentiels.get(0);
            }
            handicap = (sommeDifferentiels / n) * 0.96;
        }
        return (double) ((int) (handicap * 10)) / 10.0;
    }

    private int calculerMoyenne() {
        double somme = 0.0;
        int n = 0;
        for (Partie partie : cntParties.getItemIds()) {
            if (partie.getScore() != 0 && partie.getEvaluationTerrain() != 0.0 && partie.getEvaluationSlope() != 0
                    && partie.getDate() != null) {
                somme += partie.getScore();
                n++;
            }
        }
        return (int) (n == 0 ? 0.0 : (somme / n));
    }

    private boolean validerPartie(BeanItemContainer<Partie> cntParties) {
        boolean valide = true;
        for (Partie partie : cntParties.getItemIds()) {
            fields.get(partie).get("id").setComponentError(null);
            fields.get(partie).get("nomClub").setComponentError(null);
            fields.get(partie).get("scoreStr").setComponentError(null);
            fields.get(partie).get("evaluationTerrainStr").setComponentError(null);
            fields.get(partie).get("evaluationSlopeStr").setComponentError(null);
        }
        for (Partie partie : cntParties.getItemIds()) {
            Set<ConstraintViolation<Partie>> violations = validateur.validate(partie);
            if (violations.size() > 0) {
                valide = false;
                for (ConstraintViolation<Partie> violation : violations) {
                    if (violation.getMessage().equals(Partie.NOM_CLUB_OBLIGATOIRE)) {
                        fields.get(partie).get("nomClub").setComponentError(
                                new UserError(messagesErreur.get(Partie.NOM_CLUB_OBLIGATOIRE)));
                    } else if (violation.getMessage().equals(Partie.SCORE_INVALIDE)) {
                        fields.get(partie).get("scoreStr").setComponentError(
                                new UserError(messagesErreur.get(Partie.SCORE_INVALIDE)));
                    } else if (violation.getMessage().equals(Partie.EVALUATION_TERRAIN_INVALIDE)) {
                        fields.get(partie).get("evaluationTerrainStr").setComponentError(
                                new UserError(messagesErreur.get(Partie.EVALUATION_TERRAIN_INVALIDE)));
                    } else if (violation.getMessage().equals(Partie.EVALUATION_SLOPE_INVALIDE)) {
                        fields.get(partie).get("evaluationSlopeStr").setComponentError(
                                new UserError(messagesErreur.get(Partie.EVALUATION_SLOPE_INVALIDE)));
                    }
                }
            }
        }
        return valide;
    }
}
