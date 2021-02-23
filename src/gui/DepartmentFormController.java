package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exception.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity; // dependecia para departamento é a entidade relacionada ao formulario essa
	
	private DepartmentService service; // criado uma dependencia com a classe DepartmentService
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();//permimete objetos a se inscrever nesta lista e receberem eventos  
	
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
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void SubscribeDataChangeListener(DataChangeListener listener) {//qualquer objeto que implementem a Interface pode ser inscrito nesta lista para receber o evento
		dataChangeListeners.add(listener);
	}
	
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
		entity = gerFormData(); // responsavel por pegar dados das caixa de texto txtName e txtId e instanciar o obejeto departamento
		service.saveOrUpdate(entity);
		notifyDataChangeListeners();
		Utils.currentStage(event).close();// fecha a janela atual
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
		catch(ValidationException e) {
			setErrorMessage(e.getErrors());
		}
	}
	private void notifyDataChangeListeners() {
		for(DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();//fim da classe sobject classe que emite o evento
		}
		
	}

	private Department gerFormData() {
		Department obj = new Department();
		
		ValidationException exception = new ValidationException("Validation error");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		if(txtName.getText() == null || txtName.getText().trim().equals("") ) {//caso a string for vazia carrega a coleção map da classe de excessão personalizada com o nome do campa e erro gerado no caso vazia 
			exception.addErrors("name", "field can' t be empty ");
			}
		obj.setName(txtName.getText());
		
		if(exception.getErrors().size() > 0 ) {
			throw exception;		
			}
		
		return obj;
		
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();// fecha a janela atual
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
	
	private void setErrorMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
