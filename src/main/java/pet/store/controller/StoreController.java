package pet.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.CustomerData;
import pet.store.controller.model.EmployeeData;
import pet.store.controller.model.PetStoreData;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class StoreController {

	@Autowired
	private PetStoreService petStoreService;
	
	////////////////////////////////////////////////////////////
	// customer endpoints //////////////////////////////////////
	////////////////////////////////////////////////////////////

	@PostMapping("/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public CustomerData insertCustomer(@RequestBody CustomerData customerData) {
		log.info("Creating customer {}", customerData);
		return petStoreService.saveCustomer(customerData);
	}
	
	@PutMapping("/customer/{customerId}")
	public CustomerData updateCustomer(@PathVariable Long customerId, @RequestBody CustomerData customerData) {
		customerData.setCustomerId(customerId);
		log.info("Updating customer {}", customerData);
		return petStoreService.saveUpdatedFieldsInCustomer(customerData);
	}
	
	@GetMapping("/customer")
	public List<CustomerData> retrieveAllCustomers(){
		log.info("Retrieving all Customers");
		return petStoreService.retrieveAllCustomers();
	}
	
	@GetMapping("/customer/{customerId}")
	public CustomerData retrieveCustomerById(@PathVariable Long customerId) {
		log.info("Retrieving customer with ID={}", customerId);
		return petStoreService.retrieveCustomerById(customerId);
	}
	
	////////////////////////////////////////////////////////////
	// pet store endpoints /////////////////////////////////////
	////////////////////////////////////////////////////////////

	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData insertPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store {}", petStoreData);
		return petStoreService.saveUpdatedFieldsInPetStore(petStoreData);
	}
	
	@GetMapping("/pet_store")
	public List<PetStoreData> retreiveAllPetStores(){
		log.info("Retrieving all Pet Stores");
		return petStoreService.retrieveAllPetStores();
	}
	
	@GetMapping("/pet_store/{petStoreId}")
	public PetStoreData retrievePetStoreById(@PathVariable Long petStoreId) {
		log.info("Retrieving pet store with ID={}", petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}
	
	////////////////////////////////////////////////////////////
	// employee endpoints //////////////////////////////////////
	////////////////////////////////////////////////////////////
	
	@PostMapping("/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public EmployeeData insertEmployee(@RequestBody EmployeeData employeeData) {
		log.info("Creating employee {}", employeeData);
		return petStoreService.saveEmployee(employeeData);
	}
	
	@PutMapping("/employee/{employeeId}")
	public EmployeeData updateEmployee(@PathVariable Long employeeId, @RequestBody EmployeeData employeeData) {
		employeeData.setEmployeeId(employeeId);
		log.info("Updating employee {}", employeeData);
		return petStoreService.saveUpdatedFieldsInEmployee(employeeData);
	}
	
	@GetMapping("/employee")
	public List<EmployeeData> retrieveAllEmployees(){
		log.info("Retrieving all Employees");
		return petStoreService.retrieveAllEmployees();
	}
	
	@GetMapping("/employee/{employeeId}")
	public EmployeeData retrieveEmployeeById(@PathVariable Long employeeId) {
		log.info("Retrieving employee with ID={}", employeeId);
		return petStoreService.retrieveEmployeeById(employeeId);
	}
}
