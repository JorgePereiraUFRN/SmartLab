package smartmetropolis.smartlab.managedBeans;

import java.util.List;

import javax.faces.bean.ManagedBean;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;

@ManagedBean (name = "localB")
public class LocalMBean {
	
	private List<Local> locals;
	private Local local;
	
	private LocalController localController;

	public LocalMBean() {
		localController = LocalController.getInstance();
		local = new Local();
	}
	
	
	
	public void saveLocal(){
		
		try {
			localController.saveLocal(local);
			
			listAllLocals();
			local = new Local();
			
		} catch (validateDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void listAllLocals(){
		
		try {
			locals = localController.findAllLocals();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	public List<Local> getLocals() {
		return locals;
	}



	public void setLocals(List<Local> locals) {
		this.locals = locals;
	}



	public Local getLocal() {
		return local;
	}



	public void setLocal(Local local) {
		this.local = local;
	}
	
	

}
