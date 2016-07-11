package smartmetropolis.smartlab.managedBeans;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import smartmetropolis.smartlab.model.Sensor;


@FacesConverter("SensorConverter")
public class SensorConverter implements Converter {

	public SensorConverter() {
		// TODO Auto-generated constructor stub
	}

	public Object getAsObject(FacesContext fc, UIComponent uic, String value) {
		if(value != null && value.trim().length() > 0) {
            try {
                SensorMBean service = (SensorMBean) fc.getExternalContext().getApplicationMap().get("SensorService");
                System.out.println("retornando sensor ");
                return service.getSensorsMap().get(Integer.parseInt(value));
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid Sensor."));
            }
        }
        else {
        	System.out.println("retornando objeto nulo");
            return null;
        }
	}

	public String getAsString(FacesContext fc, UIComponent uic, Object object) {
		if(object != null) {
			System.out.println("retornando sensor "+String.valueOf(((Sensor) object).getId()));
            return String.valueOf(((Sensor) object).getId());
        }
        else {
            return null;
        }
	}

}
