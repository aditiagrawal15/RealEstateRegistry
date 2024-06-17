package realestate.registry.application.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itextpdf.text.DocumentException;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import realestate.registry.application.beans.PropertyOwnership;
import realestate.registry.application.beans.RegistryNagarpalikaRecord;
import realestate.registry.application.beans.RegistryUser;
import realestate.registry.application.beans.SaleAgreement;
import realestate.registry.application.exception.RERException;
import realestate.registry.application.repository.PropertyOwnershipRepository;
import realestate.registry.application.repository.RegistryNagarpailkaRecordsRepository;
import realestate.registry.application.repository.RegistryUserRepository;
import realestate.registry.application.repository.SaleAgreementRepository;
import realestate.registry.application.utilities.EncryptionUtility;
import realestate.registry.application.utilities.RERConstants;
import realestate.registry.application.utilities.RERResponseEntity;
import realestate.registry.application.vo.CountDTO;
import realestate.registry.application.vo.RegistryUserVO;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/registry")
public class RealEstateRegistryController {

	@Autowired
	private RegistryUserRepository userRepo;
	
	@Autowired
	private RegistryNagarpailkaRecordsRepository regNagarRepo;
	
	@Autowired
	private PropertyOwnershipRepository propertyRepo;
	
	@Autowired
	private SaleAgreementRepository saleagreeRepo;
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path="/users")
	public ResponseEntity test(){
		Pageable sortedByName = 
				  PageRequest.of(0, 1, Sort.by("name"));
		 Page<List<RegistryUserVO>> users = userRepo.findAllRegistryUsers(sortedByName, RegistryUserVO.class);
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path="/getCounts")
	public RERResponseEntity getDashboardCounts(){
		CountDTO data = new CountDTO();
		data.setRegistryUserCount(userRepo.count());
		data.setLoggedInUserCount((long) userRepo.findLoggedInUsers().size());
		data.setRegistryNagarpalikaCount(regNagarRepo.count());
		data.setPropertyCount(propertyRepo.count());
		return new RERResponseEntity(data, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path="/getPropertyDetails/{propertyId}")
	public RERResponseEntity getPropertyDetails(@PathVariable Integer propertyId){
		RERException exception;
		Optional<PropertyOwnership> property = propertyRepo.findById(propertyId);
		if(!property.isPresent()) {
			exception = new RERException("Property Id '"+propertyId+"' not found.","Please provide valid property Id.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new RERResponseEntity(property.get(), HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path="/getUserRegistrations/{userId}")
	public RERResponseEntity getUserRegistrations(@PathVariable Integer userId){
		RERException exception;
		List<RegistryNagarpalikaRecord> records = null;
		Optional<RegistryUser> user = userRepo.findById(userId);
		if(!user.isPresent()) {
			exception = new RERException("User with ID '"+userId+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
		 records = regNagarRepo.findByUserId(userId);
		}
		return new RERResponseEntity(records, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path="/getValidSaleAgreement/{propertyId}")
	public RERResponseEntity getValidSaleAgreement(@PathVariable Integer propertyId){
		RERException exception;
		SaleAgreement saleAgreement =null;
		Optional<PropertyOwnership> property = propertyRepo.findById(propertyId);
		if(!property.isPresent()) {
			exception = new RERException("Property with ID '"+propertyId+"' not found.","Provide valid property ID",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		else {
			 saleAgreement = saleagreeRepo.findByPropertyId(propertyId);
			if(saleAgreement!=null) {
				exception = new RERException("A prior Sale Agreement is already present on Property Id '"+propertyId,"",RERConstants.ERROR);
				return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		return new RERResponseEntity(null, HttpStatus.OK);
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping(path="/validateRegistration/{propertyId}/{ownerAadhar}")
	public RERResponseEntity validateRegistration(@PathVariable Integer propertyId, @PathVariable Long ownerAadhar){
		RERException exception;
		SaleAgreement saleAgreement = saleagreeRepo.findByPropertyId(propertyId);
		RegistryUser owner = userRepo.findUserByAadharnumber(ownerAadhar);
		if(owner==null) {
			exception = new RERException("User with aadhar number '"+ownerAadhar+"' does not exists.","Provide valid aadhar number",RERConstants.ERROR);
			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(saleAgreement==null) {
			Optional<PropertyOwnership> property = propertyRepo.findById(propertyId);
			if(property.isPresent()) {
				if(owner.getId()==property.get().getCurrentOwnerFk().getId()) {
					return new RERResponseEntity(null, HttpStatus.OK);
				}
				else {
					exception = new RERException("As per records, Property '"+property.get().getPropertyId()+"' is not owned by Aadhar Number "+ownerAadhar,".Please provide valid Aadhar Number as per Property Details.",RERConstants.ERROR);
					return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			else {
				exception = new RERException("Property with ID '"+propertyId+"' not found.","Provide valid property ID",RERConstants.ERROR);
				return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		else if(saleAgreement!=null) {
			Date tentativeDate = saleAgreement.getTentativeDate();
			if(tentativeDate==null) {
				exception = new RERException("Tenative date not available for Sale Agreement with ID '"+saleAgreement.getAgreementId(),"Provide valid tentative date.",RERConstants.ERROR);
				return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			Date currentDate = new Date();
			if(tentativeDate.compareTo(currentDate) >= 0) {
				long difference_In_Time = tentativeDate.getTime() - currentDate.getTime();
				long difference_In_Days = (difference_In_Time / (1000 * 60 * 60 * 24)) % 365;
				exception = new RERException("A prior Sale Agreement is already present on Property Id '"+propertyId +"' & "+difference_In_Days+" days are still left in completion of this agreement.","Please try again after "+difference_In_Days+" days.",RERConstants.ERROR);
				return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			else if(tentativeDate.compareTo(currentDate) < 0) {
				Optional<PropertyOwnership> property = propertyRepo.findById(propertyId);
				if(property.isPresent()) {
					if(owner.getId()==property.get().getCurrentOwnerFk().getId()) {
						return new RERResponseEntity(null, HttpStatus.OK);
					}
					else {
						exception = new RERException("As per records, Property '"+property.get().getPropertyId()+"' is not owned by Aadhar Number "+ownerAadhar,".Please provide valid Aadhar Number as per Property Details.",RERConstants.ERROR);
						return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				return new RERResponseEntity(null, HttpStatus.OK);
			}
		}
		return null;
		
	}
	
//	@SuppressWarnings("rawtypes")
//	//@RequestMapping(method = RequestMethod.POST,path = "/saveSaleAgreement")
//	@PostMapping(path="/addSaleAgreement",consumes = MediaType.APPLICATION_JSON_VALUE)
//	public RERResponseEntity addSaleAgreement(@RequestBody SaleAgreement agreement) {
//		RERException exception;
//		RegistryUser seller = userRepo.findUserByAadharnumber(agreement.getSellerUserFk().getAadharNumber());
//		if(seller==null) {
//			exception = new RERException("Seller with aadhar number '"+agreement.getSellerUserFk().getAadharNumber()+"' does not exists.","Provide valid aadhar number",RERConstants.ERROR);
//			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		else {
//			agreement.setSellerUserFk(seller);
//		}
//		Optional<RegistryUser> purchaser =userRepo.findById(agreement.getPurchaserUserFk().getId());
//		if(!purchaser.isPresent()) {
//			exception = new RERException("Purchaser with ID '"+agreement.getPurchaserUserFk().getId()+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
//			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		else {
//			agreement.setPurchaserUserFk(purchaser.get());
//		}
//		Optional<PropertyOwnership> property = propertyRepo.findById(agreement.getPropertyFk().getPropertyId());
//		if(!property.isPresent()) {
//			exception = new RERException("Property with ID '"+agreement.getPropertyFk().getPropertyId()+"' not found.","Provide valid property ID",RERConstants.ERROR);
//			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		else {
//			agreement.setPropertyFk(property.get());
//		}
//		saleagreeRepo.saveAndFlush(agreement);
//		return new RERResponseEntity(agreement, HttpStatus.CREATED);
//	}
	
//	@SuppressWarnings("rawtypes")
//	@PostMapping(path="/saveRegistry",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public RERResponseEntity saveRegistry(@RequestBody RegistryNagarpalikaRecord record) {
//		RERException exception;
//		Optional<PropertyOwnership> property = propertyRepo.findById(record.getPropertyFk().getPropertyId());
//		if(!property.isPresent()) {
//			exception = new RERException("Property with ID '"+record.getPropertyFk().getPropertyId()+"' not found.","Provide valid property ID",RERConstants.ERROR);
//			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		else {
//			record.setPropertyFk(property.get());
//		}
//		Optional<RegistryUser> purchaser =userRepo.findById(record.getOwnerFk().getId());
//		if(!purchaser.isPresent()) {
//			exception = new RERException("Purchaser with ID '"+record.getOwnerFk().getId()+"' not found.","Please provide a valid user ID.",RERConstants.ERROR);
//			return new RERResponseEntity(exception, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		else {
//			record.setOwnerFk(purchaser.get());	
//		}
//		regNagarRepo.saveAndFlush(record);
//		return new RERResponseEntity(record, HttpStatus.CREATED);
//	}
	
//	@SuppressWarnings("rawtypes")
//	@GetMapping(path="/generatePDF")
//	public RERResponseEntity generatePDF(){
//		try {
//			EncryptionUtility.generatePDF("test.pdf");
//		} catch (URISyntaxException | IOException | DocumentException e) {
//			e.printStackTrace();
//		}
//		return new RERResponseEntity(null, HttpStatus.CREATED);
//	}
}
