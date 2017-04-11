package smartmetropolis.smartlab.dao;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Local;
import br.ufrn.NGSI_10Client.entities.Attribute;
import br.ufrn.NGSI_10Client.entities.ContextElement;

public class LocalDao extends GenericDaoOrion<Local> implements
		LocalDaoInterface {


	private static final String type = "Predio";
	
	public LocalDao() {
		super(type);
		
	}

	public ContextElement entityToContextElement(Local entity) {

		ContextElement contextElement = new ContextElement(type,
				entity.getLocalName(), false);

		Attribute att = new Attribute("localizacao", "geo:point",
				entity.getLatitude() + ", " + entity.getLongitude());

		contextElement.addAttribute(att);

		return contextElement;
	}

	public Local contextElementToEntity(ContextElement contextElement) {

		Local local = new Local();

		if (contextElement != null && contextElement.getAttributes() != null
				&& contextElement.getId() != null) {

			local.setLocalName(contextElement.getId());

			String location = getAttributeValue("localizacao", contextElement);

			if (location != null) {
				String[] lat_long = location.split(",");

				local.setLatitude(Double.parseDouble(lat_long[0]));
				local.setLongitude(Double.parseDouble(lat_long[0]));
			}

		}

		return local;
	}

	public static void main(String[] args) throws DAOException {

		Local local = new Local();

		local.setLatitude(-5.8321399);
		local.setLongitude(-35.3076653);
		local.setLocalName("Setor_9");

		LocalDao localDao = new LocalDao();

		localDao.save(local);

		
		 for (Local l : localDao.findAll()) { System.out.println(l); }
		

	}

}
