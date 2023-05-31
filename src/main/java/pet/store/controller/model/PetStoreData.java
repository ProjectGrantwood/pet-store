package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class PetStoreData {
	
	private Long petStoreId;
	private String petStoreName;
	private String petStoreAddress;
	private String petStoreCity;
	private String petStoreState;
	private String petStoreZip;
	private String petStorePhone;
	private Set<CustomerResponse> customers = new HashSet<>();
	private Set<EmployeeResponse> employees = new HashSet<>();
	
	
	public PetStoreData(PetStore petStore) {
		petStoreId = petStore.getPetStoreId();
		petStoreName = petStore.getPetStoreName();
		petStoreAddress = petStore.getPetStoreAddress();
		petStoreCity = petStore.getPetStoreCity();
		petStoreState = petStore.getPetStoreState();
		petStoreZip = petStore.getPetStoreZip();
		petStorePhone = petStore.getPetStorePhone();
		for (Customer customer : petStore.getCustomers()) {
			customers.add(new CustomerResponse(customer));
		}
	}
	
	@Data
	@NoArgsConstructor
	public static class CustomerResponse {
		private Long customerId;
		private String customerFirstName;
		private String customerLastName;
		private String customerEmail;
		
		CustomerResponse(Customer customer){
			customerId = customer.getCustomerId();
			customerFirstName = customer.getCustomerFirstName();
			customerLastName = customer.getCustomerLastName();
			customerEmail = customer.getCustomerEmail();
		}
		
	}
	
	@Data
	@NoArgsConstructor
	public static class EmployeeResponse {
		private PetStore petStore;
		private Long employeeId;
		private String employeeFirstName;
		private String employeeLastName;
		private String employeePhone;
		private String employeeJobTitle;
		
		EmployeeResponse(Employee employee){
			petStore = employee.getPetStore();
			employeeId = employee.getEmployeeId();
			employeeFirstName = employee.getEmployeeFirstName();
			employeeLastName = employee.getEmployeeLastName();
			employeePhone = employee.getEmployeePhone();
			employeeJobTitle = employee.getEmployeeJobTitle();
		}
	}
}
