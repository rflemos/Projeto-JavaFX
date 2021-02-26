package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable,DataChangeListener{//impletação Classe Observer que se inscreve no evento
	
	private DepartmentService service;
	
	@FXML
	private TableView<Department> tableViewDepartment; 
	
	@FXML
	private TableColumn<Department, Integer> tableColumId;
	
	@FXML
	private TableColumn<Department, String> tableColumName;
	
	@FXML
	private TableColumn<Department, Department> tableColumnEdit;
	
	@FXML
	private Button btNew;
	
	private ObservableList<Department> obsList;
	
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department obj = new Department();
		createDialogForm(obj,"/gui/DepartmentForm.fxml", parentStage );
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeNodes();
		
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	private void initializeNodes() {
		tableColumId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		//padrão javaFX iniciar o comportamento das colunas;
		
		
		//faz o Table View Acompanhar a altura da janela
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
		
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();//busca no banco de dados quando o metodo é chamado
		obsList = FXCollections.observableArrayList(list); // instacia obsList usando os dados da lista
		tableViewDepartment.setItems(obsList);
		initEditButtons();
	}
	
	
	private void createDialogForm(Department obj, String absoluteName , Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load(); // carrega a minha view
			
			//injeção de dependencia
			DepartmentFormController controller = loader.getController(); 
			controller.setDepartment(obj);
			controller.setDepartmentService(new DepartmentService());
			controller.SubscribeDataChangeListener(this); // se increvendo para receber o evento que ira atualizar a tabela esta amarrado na classe derpartmentForm
			controller.updateData();
			
			
			//para carregar janela de dialogo modal e necessario instaciar um outro stage
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter Department data");// configurando stage
			dialogStage.setScene(new Scene(pane));// passo minha view como uma nova scene
			dialogStage.setResizable(false);//janela não pode ser redimensionada
			dialogStage.initOwner(parentStage);//Stage pai dessa janela
			dialogStage.initModality(Modality.WINDOW_MODAL);// equanto a janela não e fechado não se pode acessar outra.
			dialogStage.showAndWait();
			
			
		}
		catch(IOException e) {
			Alerts.showAlert("IO Exception","Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
		
	}

	private void initEditButtons() {
		tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEdit.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/DepartmentForm.fxml", Utils.currentStage(event)));
			}
		});
	}

}
