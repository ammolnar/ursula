package hu.elte.szgy.data;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name="users")
public class User implements Serializable { 
	private static final long serialVersionUID = 1L;
	@Id 
    private String username;
    private String password;

	public enum UserType { ORVOS, LABOR, BETEG, ADMIN }

	@Enumerated(EnumType.STRING)
	private UserType type;
	private int  userid;

	public String getUsername() { return this.username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return this.password; }
    public void setPassword(String password) { this.password = password; }

	public UserType getType() { return this.type; }
    public void setType(UserType type) { this.type = type; }
    public int getUserid() { return this.userid; }
    public void setUserid(int userid) { this.userid = userid; }

}

