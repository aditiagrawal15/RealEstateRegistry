package realestate.registry.application.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;


import realestate.registry.application.beans.PropertyOwnership;
import realestate.registry.application.beans.RegistryNagarpalikaRecord;
import realestate.registry.application.beans.RegistryUser;
import realestate.registry.application.beans.SaleAgreement;
import realestate.registry.application.beans.WorkFlow;
import realestate.registry.application.exception.RERException;
import realestate.registry.application.repository.PropertyOwnershipRepository;
import realestate.registry.application.repository.RegistryNagarpailkaRecordsRepository;
import realestate.registry.application.repository.RegistryUserRepository;
import realestate.registry.application.repository.SaleAgreementRepository;
import realestate.registry.application.repository.WorkflowRepository;
import realestate.registry.application.utilities.EncryptionUtility;
import realestate.registry.application.utilities.RERConstants;
import realestate.registry.application.utilities.RERResponseEntity;
import realestate.registry.application.vo.WorkFlowResponseDTO;
import realestate.registry.application.vo.WorkFlowVO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/registry")
public class UserAuthenticationController {

	@Autowired
	private RegistryUserRepository userRepo;

	@Autowired
	private RegistryNagarpailkaRecordsRepository regNagarRepo;

	@Autowired
	private PropertyOwnershipRepository propertyRepo;

	@Autowired
	private SaleAgreementRepository saleagreeRepo;

	@Autowired
	private WorkflowRepository workflowRepo;

//	@Value("${reg_home}")
//	private String reg_home;

	@SuppressWarnings("rawtypes")
	@PostMapping(path="/user/login",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity login(@RequestBody RegistryUser userdetails){
		RERException exception;
		String cryptedPassword = EncryptionUtility.cryptWithMD5(userdetails.getPassword());
		RegistryUser user = userRepo.findUserByAadharnumber(userdetails.getAadharNumber());
		if(user!=null) {
			if(!user.isFirstLogin() && user.getPassword()!=null)
			{
				if(cryptedPassword.equals(user.getPassword())){
					user.setLoggedIn(true);
					user.setLoginTime(new java.util.Date());
					user.setLoggedInRole(userdetails.getLoggedInRole());
					userRepo.saveAndFlush(user);
				}
				else {
					user.setLoggedIn(false);
					userRepo.saveAndFlush(user);
					exception = new RERException("Incorrect Password entered.", "Please enter correct password.", RERConstants.ERROR);
					return new RERResponseEntity(exception, HttpStatus.UNAUTHORIZED);
				}
			}
			else {
				exception = new RERException("First time login detected", "Please set password first using New User option.", RERConstants.WARNING);
				return new RERResponseEntity(exception, HttpStatus.UNAUTHORIZED);
			}
		}
		else {
			exception = new RERException("User with aadhar number '"+userdetails.getAadharNumber()+"' not found.","Please enter a valid aadhar number.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return new RERResponseEntity(user, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping(path="/user/logout/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity logout(@PathVariable Integer userId){
		boolean isSuccess = false;
		RERException exception;
		Optional<RegistryUser> user = userRepo.findById(userId);
		if(user.isPresent()) {
			isSuccess = true;
			user.get().setLoggedIn(false);
			user.get().setLoggedInRole(null);
			userRepo.saveAndFlush(user.get());
		}
		else {
			exception = new RERException("User with ID '"+userId+"' not found.","Please provide a valid user id.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new RERResponseEntity(isSuccess, HttpStatus.OK);

	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path="/user/setPassword",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity setPassword(@RequestBody RegistryUser userdetails){
		RERException exception;
		RegistryUser user = userRepo.findUserByAadharnumber(userdetails.getAadharNumber());
		if(user==null) {
			exception = new RERException("User with aadhar number '"+userdetails.getAadharNumber()+"' not found.","Please enter a valid aadhar number.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			if(user.getPassword()!=null) {
				exception = new RERException("Password is already configured for Aadhar Number: "+userdetails.getAadharNumber()+" on Real Estate Registry.","Please try to login using your credentials.",RERConstants.WARNING);
				return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			user.setActiveFlag(true);
			user.setLoggedIn(false);
			user.setPassword(EncryptionUtility.cryptWithMD5(userdetails.getPassword()));
			user.setLoginTime(new java.util.Date());
			user.setFirstLogin(false);
			userRepo.saveAndFlush(user);
			return new RERResponseEntity(user, HttpStatus.CREATED);
		}
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping(path="/user/delete/{userName}",produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity deleteUser(@PathVariable String userName){
		RERException exception;
		RegistryUser user = userRepo.findByUserName(userName);
		if(user!=null) {
			user.setActiveFlag(false);
			userRepo.saveAndFlush(user);
			return new RERResponseEntity("User with username '"+userName+"' deleted successfully.", HttpStatus.OK);
		}
		else {
			exception = new RERException("User with username '"+userName+"' does not exists.","Cannot delete user.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path="/submitRegistryWorkflow",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity submitRegistryWorkflow(@RequestBody RegistryNagarpalikaRecord record) {
		RERException exception;
		Optional<PropertyOwnership> property = propertyRepo.findById(record.getPropertyFk().getPropertyId());
		if(!property.isPresent()) {
			exception = new RERException("Property with ID '"+record.getPropertyFk().getPropertyId()+"' not found.","Provide valid property ID",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			record.setPropertyFk(property.get());
		}
		Optional<RegistryUser> purchaser =userRepo.findById(record.getOwnerFk().getId());
		if(!purchaser.isPresent()) {
			exception = new RERException("Purchaser with ID '"+record.getOwnerFk().getId()+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			record.setOwnerFk(purchaser.get());	
		}
		record.setActiveFlag(false);
		regNagarRepo.saveAndFlush(record);

		WorkFlow workflow = new WorkFlow();
		workflow.setActiveFlag(true);
		workflow.setRaisedBy(purchaser.get());
		workflow.setRaisedRole("BUYER");
		workflow.setSellerApprover(property.get().getCurrentOwnerFk());
		workflow.setDetails("Registry workflow created.");
		workflow.setStatus("PENDING");
		workflow.setSaleAgreementFk(null);
		workflow.setRegistry(record);
		workflow.setPendingWith(property.get().getCurrentOwnerFk());
		workflow.setCreated(new Date());
		workflow.setUpdated(new Date());
		workflowRepo.saveAndFlush(workflow);
		return new RERResponseEntity(workflow, HttpStatus.CREATED);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path="/submitSaleAgreementWorkflow",consumes = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity submitSaleAgreementWorkflow(@RequestBody SaleAgreement agreement) {
		RERException exception;
		RegistryUser seller = userRepo.findUserByAadharnumber(agreement.getSellerUserFk().getAadharNumber());
		if(seller==null) {
			exception = new RERException("Seller with aadhar number '"+agreement.getSellerUserFk().getAadharNumber()+"' does not exists.","Provide valid aadhar number",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			agreement.setSellerUserFk(seller);
		}
		Optional<RegistryUser> purchaser =userRepo.findById(agreement.getPurchaserUserFk().getId());
		if(!purchaser.isPresent()) {
			exception = new RERException("Purchaser with ID '"+agreement.getPurchaserUserFk().getId()+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			agreement.setPurchaserUserFk(purchaser.get());
		}
		Optional<PropertyOwnership> property = propertyRepo.findById(agreement.getPropertyFk().getPropertyId());
		if(!property.isPresent()) {
			exception = new RERException("Property with ID '"+agreement.getPropertyFk().getPropertyId()+"' not found.","Provide valid property ID",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			agreement.setPropertyFk(property.get());
		}
		agreement.setActiveFlag(false);
		saleagreeRepo.saveAndFlush(agreement);

		WorkFlow workflow = new WorkFlow();
		workflow.setActiveFlag(true);
		workflow.setRaisedBy(purchaser.get());
		workflow.setRaisedRole("BUYER");
		workflow.setSellerApprover(seller);
		workflow.setDetails("Sale Aggrement workflow created.");
		workflow.setStatus("PENDING");
		workflow.setSaleAgreementFk(agreement);
		workflow.setRegistry(null);
		workflow.setPendingWith(seller);
		workflow.setCreated(new Date());
		workflow.setUpdated(new Date());
		workflowRepo.saveAndFlush(workflow);

		return new RERResponseEntity(workflow, HttpStatus.CREATED);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path="/submitReponseWorkflow",consumes = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity submitReponseWorkflow(@RequestBody WorkFlowResponseDTO response) {
		RERException exception;
		WorkFlow workflow = workflowRepo.findByIdandActiveFlag(response.getWorkFlowId());
		if(workflow==null) {
			exception = new RERException("Workflow with ID '"+response.getWorkFlowId()+"' not found.","Provide valid workflow ID",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		Optional<RegistryUser> user =userRepo.findById(response.getUserId());
		if(!user.isPresent()) {
			exception = new RERException("User with ID '"+response.getUserId()+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(response.getUserRole().equalsIgnoreCase("SELLER") && response.getResponse().equalsIgnoreCase("APPROVED")) {
			List<RegistryUser> registrars = userRepo.findRegistrars();
			if(registrars!=null && registrars.size()!=0) {
				workflow.setRegistrarApprover(registrars.get(0));
				workflow.setStatus("PENDING");
				workflow.setSellerComments(response.getSellerComment());
				workflow.setPendingWith(registrars.get(0));
				workflow.setUpdated(new Date());
				workflowRepo.saveAndFlush(workflow);
			}
		}
		else if(response.getUserRole().equalsIgnoreCase("SELLER") && response.getResponse().equalsIgnoreCase("REJECTED")) {
			workflow.setStatus("REJECTED");
			workflow.setSellerComments(response.getSellerComment());
			workflow.setPendingWith(workflow.getRaisedBy());
			workflowRepo.saveAndFlush(workflow);
		}
		else if(response.getUserRole().equalsIgnoreCase("REGISTRAR") && response.getResponse().equalsIgnoreCase("APPROVED")) {
			workflow.setStatus("APPROVED");
			workflow.setRegistrarComments(response.getRegistrarComment());
			workflow.setPendingWith(null);
			workflow.setUpdated(new Date());

			if(workflow.getRegistry()!=null) {
				Optional<RegistryNagarpalikaRecord> record = regNagarRepo.findById(workflow.getRegistrationNo());
				if(record.isPresent()) {
					record.get().setActiveFlag(true);
					regNagarRepo.saveAndFlush(record.get());
				}
				Optional<PropertyOwnership> property = propertyRepo.findById(record.get().getPropertyFk().getPropertyId());
				if(!property.isPresent()) {
					exception = new RERException("Property with ID '"+record.get().getPropertyFk().getPropertyId()+"' not found.","Provide valid property ID",RERConstants.ERROR);
					return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
				}
				else {
					property.get().setPreviousOwnerFk(property.get().getCurrentOwnerFk());
					property.get().setCurrentOwnerFk(workflow.getRaisedBy());
					propertyRepo.saveAndFlush(property.get());
				}
			}
			else if(workflow.getSaleAgreementFk()!=null) {
				Optional<SaleAgreement> saleAgreement = saleagreeRepo.findById(workflow.getSaleAgreementId());
				if(saleAgreement.isPresent()) {
					saleAgreement.get().setActiveFlag(true);
					saleagreeRepo.saveAndFlush(saleAgreement.get());
				}
			}


			workflowRepo.saveAndFlush(workflow);
		}
		else if(response.getUserRole().equalsIgnoreCase("REGISTRAR") && response.getResponse().equalsIgnoreCase("REJECTED")) {
			workflow.setStatus("REJECTED");
			workflow.setRegistrarComments(response.getRegistrarComment());
			workflow.setPendingWith(null);
			workflow.setUpdated(new Date());
			workflowRepo.saveAndFlush(workflow);
		}
		return new RERResponseEntity(workflow, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping(path="/getUserWorkflows/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity getUserWorkflow(@PathVariable Integer userId){
		RERException exception;
		Optional<RegistryUser> user =userRepo.findById(userId);
		if(!user.isPresent()) {
			exception = new RERException("User with ID '"+userId+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		List<WorkFlowVO> workflows = workflowRepo.findWorkFlowsByUserId(userId, WorkFlowVO.class);
		return new RERResponseEntity(workflows, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@GetMapping(path="/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public RERResponseEntity getUserDetails(@PathVariable Integer userId){
		Optional<RegistryUser> user = userRepo.findById(userId);
		if(user.isPresent()) {
			return new RERResponseEntity(user.get(), HttpStatus.OK);
		}
		else {
			return new RERResponseEntity(null, HttpStatus.OK);
		}
	}

	@GetMapping(value = "/generatepdf/{workflowId}")
	public ResponseEntity<InputStreamResource> downloadPdf(@PathVariable Long workflowId) {

		WorkFlow workflow = workflowRepo.findByIdandActiveFlag(workflowId);
		String purchaserName = null;
		String sellerName = null;
		String registrarName = null;
		String propertyValue = null;
		if(workflow!=null && workflow.getSaleAgreementFk()!=null) {
			purchaserName = workflow.getRaisedByNameWithAadhar();
			sellerName = workflow.getSellerApproverByNameWithAadhar();
			registrarName = workflow.getRegistrarApproverByNameWithAadhar();
			propertyValue = workflow.getSaleAgreementFk().getPropertyFk().getPropertyValueWithComma();
		}
		else if(workflow!=null && workflow.getRegistry()!=null) {
			
		}
		ByteArrayInputStream input = null;
		try {
			if(workflow!=null && workflow.getSaleAgreementFk()!=null) {
			 input = EncryptionUtility.generateSaleAgreementPDF(purchaserName,sellerName,registrarName,propertyValue);
			}
			else if(workflow!=null && workflow.getRegistry()!=null) {
				input = EncryptionUtility.generateRegistryCertificate(workflow);
			}
		}
		catch(DocumentException | IOException | URISyntaxException  e) {
			e.printStackTrace();
			return null;	
		}
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "inline");
		
		return ResponseEntity
				.ok()
				.headers(headers)
				.contentType(MediaType.APPLICATION_OCTET_STREAM)
				.body(new InputStreamResource(input));
	}

//	@GetMapping(value = "/generatepdftemp")
//	public ResponseEntity<InputStreamResource> download() throws URISyntaxException, DocumentException, IOException {
//		ByteArrayInputStream input = null;
//		input = EncryptionUtility.generateRegistryCertificate(null, null, null);
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Content-Disposition", "inline");
//		
//		return ResponseEntity
//				.ok()
//				.headers(headers)
//				.contentType(MediaType.APPLICATION_OCTET_STREAM)
//				.body(new InputStreamResource(input));
//	}
	
}
