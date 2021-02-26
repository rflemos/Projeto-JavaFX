package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return dao.findAll();
		/*List<Department> list = new ArrayList();
		list.add(new Department (1,"Bokks"));
		list.add(new Department (2,"Computers"));
		list.add(new Department (3,"Eletronics"));
		return list;*/ // implementação mockada.
	}
		
		public void saveOrUpdate( Department obj) {
			
			if(obj.getId() == null) { 
				// testo caso seja igual a nulo siguinifica que estamos inserindo o novo departamento
				dao.insert(obj);
				
			}
			
			else {
				dao.update(obj);
			}
		}
		
		public void remove(Department obj) {
			dao.deleteById(obj.getId());
		}
		
		
	}


