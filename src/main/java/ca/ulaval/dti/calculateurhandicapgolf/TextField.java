/*
 * TextField.java
 * 
 * (C) 2013 Universit� Laval. Tous droits r�serv�s.
 */
package ca.ulaval.dti.calculateurhandicapgolf;

import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;

@SuppressWarnings("serial")
public class TextField extends com.vaadin.ui.TextField {

    public TextField() {
        super();
    }

    public TextField(String caption) {
        super(caption);
    }

    public TextField(@SuppressWarnings("rawtypes") Property dataSource) {
        super(dataSource);
    }

    public TextField(String caption, @SuppressWarnings("rawtypes") Property dataSource) {
        super(caption, dataSource);
    }

    public TextField(String caption, String value) {
        super(caption, value);
    }

    @Override
    public void setComponentError(ErrorMessage componentError) {
        super.setComponentError(componentError);
        this.setStyleName((componentError == null ? "v-textfield" : "redborder"));
    }
}
