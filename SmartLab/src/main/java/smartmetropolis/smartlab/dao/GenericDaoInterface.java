package smartmetropolis.smartlab.dao;


import java.io.Serializable;
import java.util.List;

import br.ufrn.NGSI_10Client.entities.ContextElement;
import smartmetropolis.smartlab.exceptions.DAOException;


public interface GenericDaoInterface<T, ID extends Serializable> {
	
	static final String orionURI = "http://177.20.147.208";
	
	public T findById(ID id) throws DAOException;
	 
	public List<T> findAll()throws DAOException;
 
	public T save(T entity)throws DAOException;
 
	public T update(T entity)throws DAOException;
        
	public void delete(ID id)throws DAOException;

}
