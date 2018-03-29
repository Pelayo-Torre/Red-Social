package com.uniovi.services;
import java.util.LinkedList;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniovi.entities.User;
import com.uniovi.repositories.UsersRepository;
import org.springframework.data.domain.Pageable;

@Service
public class UsersService {
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired 
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private RolesService rolesService;
	
	@PostConstruct
	public void init() {
	}
	
	public Page<User> getUsers(Pageable pageable) {
		Page<User> users = usersRepository.findAll(pageable);
		return users;
	}
	
	public Page<User> getUsersFriends(Pageable pageable, String email){
		Page<User> users = usersRepository.findUsersFriendsByEmail(pageable, email);
		return users;
	}
	
	public User getUser(Long id) {
		return usersRepository.findOne(id);
	}
	
	public void addUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		usersRepository.save(user);
	}
		
	public void deleteUser(Long id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();
		
		User userAutenticado = usersRepository.findByEmail(email);
		if(userAutenticado.getRole().equals(rolesService.getRoles()[1]))
			usersRepository.delete(id);
	}
	
	public User getUserByEmail(String email) {
		return usersRepository.findByEmail(email);
	}
	
	public Page<User> searchUserByNameOrEmail(Pageable pageable, String searchText){
		Page<User> users = new PageImpl<User>(new LinkedList<User>());
		searchText = "%"+searchText+"%";
		users = usersRepository.searchByNameOrEmail(pageable, searchText);
		return users;
	}

	/**
	 * Comprueba que si se cambia el id en la url
	 * solo se muestre aquellas publicaciones de las que es usuario amigo
	 * @param revised
	 * @param id
	 */
	public User usuarioAmigo(Long id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String email = auth.getName();		
		User userAutenticado = usersRepository.findByEmail(email);
		User amigo = usersRepository.findOne(id);
		amigo = usersRepository.findUsersFriends(userAutenticado.getEmail(), amigo.getEmail());

		if(amigo != null) {
			return amigo;
		}
		return null;
	}
	

}
