package fr.pierre.apirest.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.pierre.apirest.entities.Request;
import fr.pierre.apirest.repositories.RequestRepository;

@Service
public class RequestService {

	@Autowired
	RequestRepository requestRepository;

	
	Logger logger = LoggerFactory.getLogger(RequestService.class);

	public List<Request> getAll() {
		if (!requestRepository.findAll().isEmpty()) {
			List<Request> requests = requestRepository.findAll();
			this.logger.debug("getAll Return = " + requests);
			return requests;
		}
		return null;
	}
	
	public Request getbyId(Long id) {
		this.logger.debug("getbyId Call = " + id);
		Request request = requestRepository.findById(id).get();
		this.logger.debug("getbyId Return = " + request);
		return request;
	}
	
	public List<Request> getByBookIbn(Long ibn) {
		this.logger.debug("getByIbn Call = " + ibn);
		List<Request> requests = requestRepository.findByBookIbn(ibn);
		this.logger.debug("getByIbn Return = " + requests);
		return requests;
	}
	
	public List<Request> getByUserId(Long id) {
		this.logger.debug("getByUser Call = " + id);
		List<Request> requests = requestRepository.findByUserId(id);
		this.logger.debug("getByUser Return = " + requests);
		return requests;
	}

	public Request add(Request request) {
		this.logger.debug("add Call = " + request);
		Request requestreturn = requestRepository.save(request);
		this.logger.debug("add Return = " + requestreturn);
		return requestreturn;
	}
	
	public Request update(Request request) {
		this.logger.debug("update Call = " + request);
		Request requestReturn = requestRepository.save(request);
		this.logger.debug("update Return = " + requestReturn);
		return requestReturn;
	}

	public void delete(Long id) {
		this.logger.debug("delete Call = " + id);
		requestRepository.deleteById(id);
	}
}
