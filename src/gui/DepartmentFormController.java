package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable {
	
	private Department entity; // dependecia para departamento é a entidade relacionada ao formulario essa
	
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}
	
	
	
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		
	}
	
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	
	public void updateData() {
		if(entity == null) { // caso o programador esqueça de instanciar a variavel entity
		throw new IllegalStateException("Entity was null");
		}
 		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		
	}

}
