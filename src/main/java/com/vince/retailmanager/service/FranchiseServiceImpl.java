package com.vince.retailmanager.service;

import com.vince.retailmanager.entity.Company;
import com.vince.retailmanager.entity.Franchisee;
import com.vince.retailmanager.entity.Franchisor;
import com.vince.retailmanager.exception.ObjectStateException;
import com.vince.retailmanager.repository.*;
import com.vince.retailmanager.web.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.vince.retailmanager.utils.StringUtils.singlePlural;

@Service
public class FranchiseServiceImpl implements FranchiseService {

	private UserService userService;
	private AccessTokensRepository accessTokensRepository;
	private CompanyRepository companyRepository;
	private FranchisorRepository franchisorRepository;
	private FranchiseeRepository franchiseeRepository;

	@Autowired
	public FranchiseServiceImpl(
		 UserService userService,
		 AccessTokensRepository accessTokensRepository,
		 CompanyRepository companyRepository,
		 FranchisorRepository franchisorRepository,
		 FranchiseeRepository franchiseeRepository,
		 PaymentRepository paymentRepository) {
		this.userService = userService;
		this.accessTokensRepository = accessTokensRepository;
		this.companyRepository = companyRepository;
		this.franchisorRepository = franchisorRepository;
		this.franchiseeRepository = franchiseeRepository;
	}


	@Override
	@Transactional
	public void saveCompany(Company company) {
		companyRepository.save(company);
	}

	@Override
	@Transactional(readOnly = true)
	public Franchisor findFranchisorById(int id) throws EntityNotFoundException {
		Franchisor franchisor = franchisorRepository.findById(id).orElse(null);
		if (franchisor == null) {
			throw new EntityNotFoundException(Franchisor.class, "id", String.valueOf(id));
		}
		return franchisor;
	}

	@Override
	@Transactional
	public void saveFranchisee(Franchisee franchisee) {
		franchiseeRepository.save(franchisee);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Franchisor> findAllFranchisors() throws DataAccessException {
		return franchisorRepository.findAll();
	}


	@Override
	@Transactional(readOnly = true)
	public Franchisee findFranchiseeById(int id) throws EntityNotFoundException {
		Franchisee franchisee = franchiseeRepository.findById(id).orElse(null);
		if (franchisee == null) {
			throw new EntityNotFoundException(Franchisee.class, "id", String.valueOf(id));
		}
		return franchisee;
	}


	@Override
	public void disableFranchisor(Franchisor franchisor) throws ObjectStateException {
		Set<Franchisee> franchisees = franchisor.getFranchisees();
		if (!franchisees.isEmpty()) {
			throwObjectStateException(franchisor, franchisees);
			return;
		}
		disableCompany(franchisor);
	}

	private void throwObjectStateException(Franchisor franchisor, Set<Franchisee> franchisees) {
		Map<String, Set<Franchisee>> invalidValues = new HashMap<>();
		invalidValues.put("franchisees", franchisees);
		String franchisorClassName = franchisor.getClass().getSimpleName();
		String errorMsg = new StringBuilder()
			 .append(franchisorClassName)
			 .append(" still has ")
			 .append(franchisees.size())
			 .append(" active ")
			 .append(singlePlural(franchisees.size(), "franchisee"))
			 .toString();

		throw new ObjectStateException(
			 errorMsg,
			 franchisorClassName,
			 invalidValues
		);
	}


	@Override
	@Transactional
	public void disableCompany(Company company) {
		//check if there are any franchisees dependendent on franchisor
		//remove all access tokens associated with company /// but throw exception in constraint validator
		company.setEnabled(false);
		companyRepository.save(company);
	}

}
