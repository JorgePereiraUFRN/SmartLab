package smartmetropolis.smartlab.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.HttpClientBuilder;

import br.ufrn.NGSI_10Client.Controller.NGSI_10;
import br.ufrn.NGSI_10Client.entities.Attribute;
import br.ufrn.NGSI_10Client.entities.ContextElement;
import br.ufrn.NGSI_10Client.entities.ContextElementRequest;
import br.ufrn.NGSI_10Client.entities.ContextElementsList;
import br.ufrn.NGSI_10Client.entities.Metadata;
import br.ufrn.NGSI_10Client.entities.UpdateAction;
import br.ufrn.NGSI_10Client.entities.UpdateContext;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Local;

public abstract class GenericDaoOrion<T> implements
		GenericDaoInterface<T, String> {
	
	private final String entityType;
	
	public GenericDaoOrion(String entityType){
		this.entityType = entityType;
	}

	public T findById(String id) throws DAOException {

		try {

			Client client = Client.create();

			WebResource webResource = client.resource(orionURI
					+ "/v1/contextEntities/" + id);

			ContextElementRequest contextRequest = webResource.accept(
					"application/json").get(ContextElementRequest.class);

			return contextElementToEntity(contextRequest.getContextElement());

		} catch (Exception e) {

			throw new DAOException(e.getMessage());

		}

	}

	public List<T> findAll() throws DAOException {

		List<T> entities = null;

		Client client = Client.create();

		WebResource webResource = client.resource(orionURI
				+ "/v1/contextEntityTypes/"+entityType);

		ContextElementsList list = webResource.accept("application/json").get(
				ContextElementsList.class);

		if (list != null && list.getContextResponses() != null
				&& list.getContextResponses().size() > 0) {

			entities = new ArrayList<T>();

			for (ContextElementRequest req : list.getContextResponses()) {

				ContextElement element = req.getContextElement();

				entities.add(contextElementToEntity(element));
			}
		}

		return entities;
	}

	public T save(T entity) throws DAOException {

		try {
			ContextElement contextElement = entityToContextElement(entity);

			UpdateContext updateContext = new UpdateContext();
			updateContext.setUpdateAction(UpdateAction.APPEND);
			updateContext.addContextElement(contextElement);

			NGSI_10 ngsi_10 = new NGSI_10(orionURI, null);

			ngsi_10.updateContext(updateContext);

		} catch (ClientProtocolException e) {
			throw new DAOException(e.getMessage());
		} catch (IOException e) {
			throw new DAOException(e.getMessage());
		}

		return entity;
	}

	public T update(T entity) throws DAOException {
		try {
			ContextElement contextElement = entityToContextElement(entity);

			UpdateContext updateContext = new UpdateContext();
			updateContext.setUpdateAction(UpdateAction.APPEND);
			updateContext.addContextElement(contextElement);

			NGSI_10 ngsi_10 = new NGSI_10(orionURI, null);

			ngsi_10.updateContext(updateContext);

		} catch (ClientProtocolException e) {
			throw new DAOException(e.getMessage());
		} catch (IOException e) {
			throw new DAOException(e.getMessage());
		}

		return entity;
	}

	public void delete(String id) throws DAOException {

		try {
			HttpClient client = HttpClientBuilder.create().build();
			HttpDelete delete = new HttpDelete(orionURI
					+ "/v1/contextEntities/" + id);

			HttpResponse response = client.execute(delete);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new DAOException("Operation returned an status code: "
						+ response.getStatusLine().getStatusCode());
			}

		} catch (ClientProtocolException e) {
			throw new DAOException(e.getMessage());

		} catch (IOException e) {
			throw new DAOException(e.getMessage());
		}

	}

	protected String getAttributeMetadataValue(String attribute,
			String metadata, ContextElement contextElement) {
		{
			
			if (contextElement != null && contextElement.getAttributes() != null
					&& contextElement.getId() != null) {

				for (Attribute at : contextElement.getAttributes()) {

					if (at.getName().equalsIgnoreCase(attribute)) {
						
						
						if(at.getMetadatas() != null && at.getMetadatas().size() > 0){
							
							for(Metadata mt: at.getMetadatas()){
								
								if(mt.getName().equals(metadata)){
									return mt.getValue();
								}
							}
						}
						
					}

				}
			}

			return null;
		}

	}

	protected String getAttributeValue(String attribute,
			ContextElement contextElement) {

		if (contextElement != null && contextElement.getAttributes() != null
				&& contextElement.getId() != null) {

			for (Attribute at : contextElement.getAttributes()) {

				if (at.getName().equalsIgnoreCase(attribute)) {
					return at.getValue();
				}

			}
		}

		return null;
	}

	abstract ContextElement entityToContextElement(T entity);

	abstract T contextElementToEntity(ContextElement contextElement);

}
