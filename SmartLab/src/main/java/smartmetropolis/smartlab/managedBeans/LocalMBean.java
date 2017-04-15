package smartmetropolis.smartlab.managedBeans;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

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
	
	
	public String  saveLocal(){
		
		try {
			localController.saveLocal(local);
			
			listAllLocals();
			local = new Local();
			
		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Dado inválido: ", e.getMessage()));
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro persistir dados: ", e.getMessage()));
		}
		
		return null;
	}
	
	
	public String listAllLocals(){
		
		System.out.println("litando todos os predios");
		try {
			locals = localController.findAllLocals();
			
			for(Local l : locals){
				System.out.println(l);
			}
			
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}
		
		return null;
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
