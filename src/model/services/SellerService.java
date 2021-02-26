package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {
	
	private SellerDao dao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return dao.findAll();
		/*List<Seller> list = new ArrayList();
		list.add(new Seller (1,"Bokks"));
		list.add(new Seller (2,"Computers"));
		list.add(new Seller (3,"Eletronics"));
		return list;*/ // implementação mockada.
	}
		
		public void saveOrUpdate(Seller obj) {
			
			if(obj.getId() == null) { 
				// testo caso seja igual a nulo siguinifica que estamos inserindo o novo departamento
				dao.insert(obj);
				
			}
			
			else {
				dao.update(obj);
			}
		}
		
		public void remove(Seller obj) {
			dao.deleteById(obj.getId());
		}

		
		
	}


