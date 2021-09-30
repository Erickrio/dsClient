package com.devsuperior.dsClient.services;

import com.devsuperior.dsClient.dto.ClientDTO;
import com.devsuperior.dsClient.entities.Client;
import com.devsuperior.dsClient.repositories.ClientRepository;
import com.devsuperior.dsClient.services.exceptions.DatabaseException;
import com.devsuperior.dsClient.services.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
	
	@Autowired
	public ClientRepository repository;

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> obj = repository.findById(id);
		Client entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
		return new ClientDTO(entity);
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		Client entity = new Client();
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setChildren(dto.getChildren());
		entity.setBirthDate(dto.getBirthDate());
		entity.setIncome(dto.getIncome());
		entity = repository.save(entity);
		return new ClientDTO(entity);
	}

	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Integrity violation");

		}

	}
	
	@Transactional
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getOne(id);
			entity.setName(dto.getName());
			entity.setName(dto.getName());
			entity.setCpf(dto.getCpf());
			entity.setChildren(dto.getChildren());
			entity.setBirthDate(dto.getBirthDate());
			entity.setIncome(dto.getIncome());
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id not found" + id);
		}
	}

	//busca paginada
	@Transactional(readOnly = true)
	public Page<ClientDTO> findAllPaged(PageRequest pageRequest) {
		Page<Client> list = repository.findAll(pageRequest);
		return list.map(x -> new ClientDTO(x));
	}
	


}
