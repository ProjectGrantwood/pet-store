package pet.store.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.CustomerData;
import pet.store.controller.model.EmployeeData;
import pet.store.controller.model.PetStoreData;
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
	public EmployeeData saveEmployee(EmployeeData employeeData) {
		Long employeeId = employeeData.getEmployeeId();
		Employee employee = findOrCreateEmployee(employeeId);
		setFieldsInEmployee(employee, employeeData);
		return new EmployeeData(employeeDao.save(employee));
	}
	
	@Transactional(readOnly = false)
	public EmployeeData saveUpdatedFieldsInEmployee(EmployeeData employeeData) {
		Long employeeId = employeeData.getEmployeeId();
		Employee employee = findOrCreateEmployee(employeeId);
		updateFieldsInEmployee(employee, employeeData);
		return new EmployeeData(employeeDao.save(employee));
	}

	private void setFieldsInEmployee(Employee employee, EmployeeData employeeData) {
		employee.setEmployeeFirstName(employeeData.getEmployeeFirstName());
		employee.setEmployeeLastName(employeeData.getEmployeeLastName());
		employee.setEmployeePhone(employeeData.getEmployeePhone());
		employee.setEmployeeJobTitle(employeeData.getEmployeeJobTitle());
	}
	
	private void updateFieldsInEmployee(Employee employee, EmployeeData employeeData) {
		String updatedEmployeeFirstName = employeeData.getEmployeeFirstName();
		String updatedEmployeeLastName = employeeData.getEmployeeLastName();
		String updatedEmployeePhone = employeeData.getEmployeePhone();
		String updatedEmployeeJobTitle = employeeData.getEmployeeJobTitle();
		if (!Objects.isNull(updatedEmployeeFirstName)) {
			employee.setEmployeeFirstName(updatedEmployeeFirstName);
		}
		if (!Objects.isNull(updatedEmployeeLastName)) {
			employee.setEmployeeLastName(updatedEmployeeLastName);
		}
		if (!Objects.isNull(updatedEmployeePhone)) {
			employee.setEmployeePhone(updatedEmployeePhone);
		}
		if (!Objects.isNull(updatedEmployeeJobTitle)) {
			employee.setEmployeeJobTitle(updatedEmployeeJobTitle);
		}
	}

	private Employee findOrCreateEmployee(Long employeeId) {
		Employee employee;
		if (Objects.isNull(employeeId)) {
			employee = new Employee();
		} else {
			employee = findEmployeeById(employeeId);
		}
		return employee;
	}

	private Employee findEmployeeById(Long employeeId) {
		return employeeDao.findById(employeeId)
				.orElseThrow(() -> new NoSuchElementException("Employee with ID=" + employeeId + " was not found."));
	}

	@Transactional(readOnly = true)
	public List<EmployeeData> retrieveAllEmployees() {
		return employeeDao.findAll().stream().map(EmployeeData::new).toList();
	}
	
	@Transactional(readOnly = true)
	public EmployeeData retrieveEmployeeById(Long employeeId) {
		return new EmployeeData(findEmployeeById(employeeId));
	}

	//////////////////////////////////////////////////////////////////
	// "/customer" ///////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////

	@Transactional(readOnly = false)
	public CustomerData saveCustomer(CustomerData customerData) {
		Long customerId = customerData.getCustomerId();
		String customerEmail = customerData.getCustomerEmail();
		Customer customer = findOrCreateCustomer(customerId, customerEmail);

		setFieldsInCustomer(customer, customerData);
		return new CustomerData(customerDao.save(customer));
	}
	
	@Transactional(readOnly = false)
	public CustomerData saveUpdatedFieldsInCustomer(CustomerData customerData) {
		Long customerId = customerData.getCustomerId();
		Customer customer = findOrCreateCustomer(customerId, customerData.getCustomerEmail());
		updateFieldsInCustomer(customer, customerData);
		return new CustomerData(customerDao.save(customer));
	}
	
	private void updateFieldsInCustomer(Customer customer, CustomerData customerData) {
		String updatedCustomerFirstName = customerData.getCustomerFirstName();
		String updatedCustomerLastName = customerData.getCustomerLastName();
		String updatedCustomerEmail = customerData.getCustomerEmail();
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

	private void setFieldsInCustomer(Customer customer, CustomerData customerData) {
		customer.setCustomerFirstName(customerData.getCustomerFirstName());
		customer.setCustomerLastName(customerData.getCustomerLastName());
		customer.setCustomerEmail(customerData.getCustomerEmail());
	}

	private Customer findOrCreateCustomer(Long customerId, String customerEmail) {
		Customer customer;
		if (Objects.isNull(customerId)) {
			Optional<Customer> opCustomer = customerDao.findByCustomerEmail(customerEmail);
			if (opCustomer.isPresent()) {
				throw new DuplicateKeyException("Customer with email " + customerEmail + " already exists.");
			}
			customer = new Customer();
		} else {
			customer = findCustomerById(customerId);
		}
		return customer;
	}

	private Customer findCustomerById(Long customerId) {
		return customerDao.findById(customerId)
				.orElseThrow(() -> new NoSuchElementException("Customer with ID=" + customerId + " was not found."));
	}

	@Transactional(readOnly = true)
	public List<CustomerData> retrieveAllCustomers() {
		return customerDao.findAll().stream().map(CustomerData::new).toList();
	}

	@Transactional(readOnly = true)
	public CustomerData retrieveCustomerById(Long customerId) {
		return new CustomerData(findCustomerById(customerId));
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

}
