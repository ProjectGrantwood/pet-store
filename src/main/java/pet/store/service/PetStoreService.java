package pet.store.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.CustomerData;
import pet.store.controller.model.EmployeeData;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreData.PetStoreCustomer;
import pet.store.controller.model.PetStoreData.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

	@Autowired
	private CustomerDao customerDao;

	@Autowired
	private PetStoreDao petStoreDao;

	@Autowired
	private EmployeeDao employeeDao;

	//////////////////////////////////////////////////////////////////
	// "/employee" ///////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	@Transactional(readOnly = false)
	public EmployeeData saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long employeeId = petStoreEmployee.getEmployeeId();
		Employee employee = findOrCreateEmployee(petStoreId, employeeId);
		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);
		Set<Employee> petStoreEmployees = petStore.getEmployees();
		petStoreEmployees.add(employee);
		petStore.setEmployees(petStoreEmployees);
		return new EmployeeData(employeeDao.save(employee));
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTitle(petStoreEmployee.getEmployeeJobTitle());
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		Employee employee;
		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findEmployeeById(petStoreId, employeeId);
		}
		return employee;
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found."));
		if (petStoreId != employee.getPetStore().getPetStoreId()) {
			throw new IllegalArgumentException("Employee does not work at Pet Store with ID=" + petStoreId + ".");
		}
		return employee;
	}

	//////////////////////////////////////////////////////////////////
	// "/customer" ///////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	@Transactional(readOnly = false)
	public CustomerData saveCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findOrCreateCustomer(petStoreId, customerId);
		copyCustomerFields(customer, petStoreCustomer);
		Set<Customer> petStoreCustomers = petStore.getCustomers();
		petStoreCustomers.add(customer);
		petStore.setCustomers(petStoreCustomers);
		return new CustomerData(customerDao.save(customer));
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomer petStoreCustomer) {
		customer.setCustomerFirstName(petStoreCustomer.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomer.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomer.getCustomerEmail());

	}
	
	
	
	@Transactional(readOnly = false)
	public CustomerData updateCustomer(Long petStoreId, PetStoreCustomer petStoreCustomer) {
		PetStore petStore = findPetStoreById(petStoreId);
		Long customerId = petStoreCustomer.getCustomerId();
		Customer customer = findCustomerById(petStoreId, customerId);
		setUpdatedFieldsInCustomer(petStoreId, customer, petStoreCustomer);
		Set<Customer> petStoreCustomers = petStore.getCustomers();
		petStoreCustomers.add(customer);
		petStore.setCustomers(petStoreCustomers);
		return new CustomerData(customerDao.save(customer));
	}
		
	
	private void setUpdatedFieldsInCustomer(Long petStoreId, Customer customer, PetStoreCustomer petStoreCustomer) {
		String updatedCustomerFirstName = petStoreCustomer.getCustomerFirstName();
		String updatedCustomerLastName = petStoreCustomer.getCustomerLastName();
		String updatedCustomerEmail = petStoreCustomer.getCustomerEmail();
		
		if (!Objects.isNull(updatedCustomerFirstName)) {
			customer.setCustomerFirstName(updatedCustomerFirstName);
		}
		if (!Objects.isNull(updatedCustomerLastName)) {
			customer.setCustomerLastName(updatedCustomerLastName);
		}
		if (!Objects.isNull(updatedCustomerEmail)) {
			customer.setCustomerEmail(updatedCustomerEmail);
		}
		
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		Customer customer;
		if (Objects.isNull(customerId)) {
			customer = new Customer();
		} else {
			customer = findCustomerById(petStoreId, customerId);
		}
		return customer;
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));
		boolean hasMatchingPetStore = false;
		for (PetStoreData petStoreData : retrieveAllPetStores()) {
			if (petStoreData.getPetStoreId() == petStoreId) {
				hasMatchingPetStore = true;
				break;
			}
		}
		if (!hasMatchingPetStore) {
			throw new IllegalArgumentException("Customer has not shopped at Pet Store with ID=" + petStoreId + ".");
		}
		return customer;
	}
	
	@Transactional(readOnly = true)
	public CustomerData retrieveCustomerById(Long petStoreId, Long customerId) {
		return new CustomerData(findCustomerById(petStoreId, customerId));
	}
	
	@Transactional(readOnly = false)
	public void deleteCustomerById(Long petStoreId, Long customerId) {
		Customer customer = findCustomerById(petStoreId, customerId);
		customerDao.delete(customer);
	}

	//////////////////////////////////////////////////////////////////
	// "/pet_store" //////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		setFieldsInPetStore(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	@Transactional(readOnly = false)
	public PetStoreData saveUpdatedFieldsInPetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);
		updateFieldsInPetStore(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private void setFieldsInPetStore(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());
	}

	private void updateFieldsInPetStore(PetStore petStore, PetStoreData petStoreData) {
		String updatedPetStoreName = petStoreData.getPetStoreName();
		String updatedPetStoreAddress = petStoreData.getPetStoreAddress();
		String updatedPetStoreCity = petStoreData.getPetStoreCity();
		String updatedPetStoreZip = petStoreData.getPetStoreZip();
		String updatedPetStoreState = petStoreData.getPetStoreState();
		String updatedPetStorePhone = petStoreData.getPetStorePhone();
		if (!Objects.isNull(updatedPetStoreName)) {
			petStore.setPetStoreName(updatedPetStoreName);
		}
		if (!Objects.isNull(updatedPetStoreAddress)) {
			petStore.setPetStoreAddress(updatedPetStoreAddress);
		}
		if (!Objects.isNull(updatedPetStoreCity)) {
			petStore.setPetStoreCity(updatedPetStoreCity);
		}
		if (!Objects.isNull(updatedPetStoreState)) {
			petStore.setPetStoreState(updatedPetStoreState);
		}
		if (!Objects.isNull(updatedPetStoreZip)) {
			petStore.setPetStoreZip(updatedPetStoreZip);
		}
		if (!Objects.isNull(updatedPetStorePhone)) {
			petStore.setPetStorePhone(updatedPetStorePhone);
		}
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		PetStore petStore;
		if (Objects.isNull(petStoreId)) {
			petStore = new PetStore();
		} else {
			petStore = findPetStoreById(petStoreId);
		}
		return petStore;
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet Store with ID=" + petStoreId + " was not found."));
	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		return petStoreDao.findAll().stream().map(PetStoreData::new).toList();
	}
	
	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		return new PetStoreData(findPetStoreById(petStoreId));
	}
	
	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}

}
