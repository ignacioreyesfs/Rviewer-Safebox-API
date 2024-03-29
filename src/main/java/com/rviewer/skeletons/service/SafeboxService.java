package com.rviewer.skeletons.service;

import java.util.ArrayList;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.rviewer.skeletons.dao.SafeboxRepository;
import com.rviewer.skeletons.exception.APIError;
import com.rviewer.skeletons.model.Safebox;
import com.rviewer.skeletons.service.dto.NewSafeboxReqDTO;
import com.rviewer.skeletons.service.dto.NewSafeboxResponseDTO;
import com.rviewer.skeletons.service.dto.SafeboxItems;

@Service
public class SafeboxService implements UserDetailsService {
	private PasswordEncoder encoder;
	private SafeboxRepository safeboxRepo;
	
	public SafeboxService(@Lazy PasswordEncoder encoder, SafeboxRepository safeboxRepo) {
		this.encoder = encoder;
		this.safeboxRepo = safeboxRepo;
	}
	
	@Transactional
	public NewSafeboxResponseDTO create(NewSafeboxReqDTO safeboxReq) {
		if(safeboxRepo.findByName(safeboxReq.getName()) != null) {
			throw new ResourceAlreadyExistsException(APIError.SAFEBOX_ALREADY_EXISTS);
		}
		Safebox safebox = new Safebox();
		safebox.setName(safeboxReq.getName());
		safebox.setPassword(encoder.encode(safeboxReq.getPassword()));
		safebox = safeboxRepo.save(safebox);
		return new NewSafeboxResponseDTO(safebox.getId());
	}
	

	@Override
	public UserDetails loadUserByUsername(String safeboxId) throws UsernameNotFoundException {
		Safebox safebox = safeboxRepo.findById(safeboxId).orElseThrow(SafeboxNotFoundException::new);
		
		return new User(safebox.getId(), safebox.getPassword(), new ArrayList<>());
	}

	public SafeboxItems getSafeboxItems(String id) {
		Safebox safebox = safeboxRepo.findById(id).orElseThrow(SafeboxNotFoundException::new);
		
		return new SafeboxItems(safebox.getItems());
	}

	public void updateItems(String id, SafeboxItems content) {
		Safebox safebox = safeboxRepo.findById(id).orElseThrow(SafeboxNotFoundException::new);
		safebox.setItems(content.getItems());
		safeboxRepo.save(safebox);
	}

}
