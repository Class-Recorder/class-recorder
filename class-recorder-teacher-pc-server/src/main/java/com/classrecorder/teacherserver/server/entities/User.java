package com.classrecorder.teacherserver.server.entities;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonView;

@Entity
@DiscriminatorColumn(name = "user_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User {
	
	/*
	 * VIEWS
	 */
	public interface Basic {};
	
	/*
	 * ATTRIBUTES
	 */
	
	@JsonView(Basic.class)
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@JsonView(Basic.class)
	@Column(unique=true)
	private String userName;
	
	private String passwordHash;
	
	@JsonView(Basic.class)
	private String fullName;
	
    @JsonView(Basic.class)
    @Column(unique=true)
	private String email;
	
	@JsonView(Basic.class)
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> roles;
	
	/*
	 * CONSTRUCTOR
	 */
	public User(){}
	
	public User(String userName, String password, String fullName, String email, List<String> roles){
		this.userName = userName;
		this.passwordHash = new BCryptPasswordEncoder().encode(password);
		this.fullName = fullName;
		this.email = email;
		this.roles = roles;
	}
	
	/*
	 * GETTERS AND SETTERS
	 */

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}
	
	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	
	@Override
	public String toString(){
		return this.userName;
	}
	
	@Override
	public boolean equals(Object obj){
		if(this == obj){
			return true;
		}
		if(obj == null){
			return false;
		}
		if(getClass() != obj.getClass()){
			return false;
		}
		final User other = (User) obj;
		if(Objects.equals(this.getId(), other.getId())){
			return true;
		}
		return (Objects.equals(this.getUserName(), other.getUserName()));
	}
	
}
