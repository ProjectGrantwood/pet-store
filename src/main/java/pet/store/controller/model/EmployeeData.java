package pet.store.controller.model;

import java.util.HashSet;
import java.util.Set;

import lombok.Data;
import lombok.NoArgsConstructor;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Data
@NoArgsConstructor
public class EmployeeData {
	
	
	private Long employeeId;
	private String employeeFirstName;
	private String employeeLastName;
	private String employeePhone;
	private String employeeJobTitle;
	private PetStoreResponse petStoreResponse;
	
	public EmployeeData(Employee employee) {
		employeeId = employee.getEmployeeId();
		employeeFirstName = employee.getEmployeeFirstName();
		employeeLastName = employee.getEmployeeLastName();
		employeePhone = employee.getEmployeePhone();
		employeeJobTitle = employee.getEmployeeJobTitle();
	}
	
	@Data
	@NoArgsConstructor
	public static class PetStoreResponse {
		
		private Long petStoreId;
		private String petStoreName;
		private String petStoreAddress;
		private String petStoreCity;
		private String petStoreState;
		private String petStoreZip;
		private String petStorePhone;
		private Set<Employee> employees = new HashSet<>();
		
		public PetStoreResponse(PetStore petStore) {
			petStoreId = petStore.getPetStoreId();
			petStoreName = petStore.getPetStoreName();
			petStoreAddress = petStore.getPetStoreAddress();
			petStoreCity = petStore.getPetStoreCity();
			petStoreState = petStore.getPetStoreState();
			petStorePhone = petStore.getPetStorePhone();
			for (Employee employee : petStore.getEmployees()) {
				employees.add(employee);
			}
		}
		
	}
	
	
}
