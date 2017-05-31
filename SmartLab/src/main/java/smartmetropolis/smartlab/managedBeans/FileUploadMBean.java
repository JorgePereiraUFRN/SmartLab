package smartmetropolis.smartlab.managedBeans;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import javax.faces.application.FacesMessage;

@ManagedBean
@RequestScoped
public class FileUploadMBean {

	private UploadedFile imageFile;

	public UploadedFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(UploadedFile imageFile) {
		this.imageFile = imageFile;
	}

	public FileUploadMBean() {
		// TODO Auto-generated constructor stub
	}

	public void saveImage(){

		if (imageFile != null) {

			try {

				
				String fileName = "/home/jorge/upload/"+imageFile.getFileName();

				copyFile(fileName, imageFile.getInputstream());

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso",
								"Imagem do perfil atualizada com sucesso!"));

			} catch (IOException e) {
				System.out.println(e.getMessage());
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										"Erro",
										"Desculpe, erro ocorrido ao atualizar a imagem. Tente novamente por favor!"));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro",
							"Selecione uma imagempara upload!"));
		}

	}

	public void copyFile(String fileName, InputStream in) throws IOException {
		

			OutputStream out = new FileOutputStream(new File(fileName));

			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();
		
	}

	
	
	

}
