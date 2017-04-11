package smartmetropolis.smartlab.dao;


import java.util.ArrayList;
import java.util.List;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.AirConditioner;
import smartmetropolis.smartlab.model.Reserva;
import br.ufrn.NGSI_10Client.entities.Attribute;
import br.ufrn.NGSI_10Client.entities.ContextElement;
import br.ufrn.NGSI_10Client.entities.ContextElementRequest;
import br.ufrn.NGSI_10Client.entities.ContextElementsList;


public class AirConditionerDao extends GenericDaoOrion<AirConditioner> implements AirConditionerDaoInterface{


	private static final String type = "Ar_Condicionado";
	
	public AirConditionerDao() {
		super(type);
	}

	public ContextElement entityToContextElement(AirConditioner entity) {
		
		ContextElement context = new ContextElement(type,
				"Ar_Condicionado_"+entity.getId()+"_Sala_"+entity.getRoomId(), false);

		Attribute sala = new Attribute("Sala", "String", entity.getRoomId());
		context.addAttribute(sala);
		
		Attribute estado = new Attribute("Acionado", "Boolean", entity.getItsOn().toString());
		context.addAttribute(estado);
		
		Attribute ipAddres = new Attribute("Ip_Controlador", "String", entity.getIpaddressAirControl());
		context.addAttribute(ipAddres);


		return context;

	}

	public AirConditioner contextElementToEntity(ContextElement contextElement) {
		
		if (contextElement != null && contextElement.getAttributes() != null
				&& contextElement.getId() != null) {
			
			AirConditioner airC = new AirConditioner();
			
			airC.setId(contextElement.getId());
			
			airC.setIpaddressAirControl(getAttributeValue("Ip_Controlador", contextElement));
			
			airC.setRoomId(getAttributeValue("Sala", contextElement));
			
			boolean itsOn = Boolean.parseBoolean(getAttributeValue("Acionado", contextElement));
			
			airC.setItsOn(itsOn);
			
			return airC;
			
			
		}
		
		return null;
	}
	
	
	public List<AirConditioner> getAirConditionersByRoom(String RoomId)
			throws DAOException {
		
		List<AirConditioner> entities = null;
		
		List<AirConditioner> airConditioners = findAll();
		
		if(airConditioners != null && airConditioners.size() > 0){
			entities = new ArrayList<AirConditioner>();
			
			
			for(AirConditioner airC : airConditioners){
				if(airC.getRoomId().equalsIgnoreCase(RoomId)){
					entities.add(airC);
				}
			}
		}


		return entities;
	}

public static void main(String[] args) throws DAOException {
		
		AirConditionerDaoInterface dao = new AirConditionerDao();
		
		//System.out.println( dao.getAirConditionersByRoom("B318"));
		
		/*
		for(AirConditioner irc : dao.findAll()){
			System.out.println(irc);
		}*/
		
	/*	AirConditioner airC = new AirConditioner();
		
		airC.setId(2L);
		airC.setIpaddressAirControl("10.7.151.254");
		airC.setItsOn(false);
		
		airC.setRoomId("B3018");
		
		dao.save(airC);*/
	}

	

}
