/**
 * @author divya
 * Created on Apr 24, 2016
 */
package models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.CascadeType;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonBackReference;


import com.avaje.ebean.Model;
import com.avaje.ebean.Model.Finder;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import play.data.validation.Constraints;

/**
 * @author divya
 *
 */

@Entity
public class User extends Model{
	@Id
    private Long id;
	
	@Constraints.Required
	private String email;
	
	@Constraints.Required
	private String password;

    private String accessLevel;
	private String firstName;
	private String lastName;
	private String mailingAddress;
	private String phoneNumber;
	private String researchFields;

	public String getFriendsID() {
		return friendsID;
	}

	public void setFriendsID(String friendsID) {
		this.friendsID = friendsID;
	}

	private String friendsID;
	@OneToOne(cascade=CascadeType.ALL)
	private Author author;
	
	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	@ManyToOne
	@JsonBackReference
	User subscribeSelf;

	@ManyToOne
	@JsonBackReference
	User friendSelf;

	@ManyToOne
	@JsonBackReference
	User friendRequestSelf;
	
	@OneToMany(mappedBy="friendSelf")
	@JsonManagedReference
	List<User> friends = new ArrayList<>();
	
	@OneToMany(mappedBy="subscribeSelf")
	@JsonManagedReference
	List<User> subscribers = new ArrayList<>();
	
	@OneToMany(mappedBy="friendRequestSelf")
	@JsonManagedReference
	List<User> friendRequestSender = new ArrayList<>();

	
	@ManyToMany(cascade=CascadeType.ALL)
	@JsonBackReference
	List<UserGroup> groups;

	public static Finder<Long, User> find = new Finder<Long, User>(User.class);

	public User() {
		super();
	}

	public User(String email, String password, String firstName, String lastName, String mailingAddress,
			String phoneNumber, String researchFields) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mailingAddress = mailingAddress;
		this.phoneNumber = phoneNumber;
		this.researchFields = researchFields;
		this.accessLevel ="User";
		this.friendsID = "";
	}

	public Long getId() {
		return id;
	}

	public User getSubscribeSelf() {
		return subscribeSelf;
	}

	public void setSubscribeSelf(User subscribeSelf) {
		this.subscribeSelf = subscribeSelf;
	}

	public User getFriendSelf() {
		return friendSelf;
	}

	public void setFriendSelf(User friendSelf) {
		this.friendSelf = friendSelf;
	}

	public User getFriendRequestSelf() {
		return friendRequestSelf;
	}

	public void setFriendRequestSelf(User friendRequestSelf) {
		this.friendRequestSelf = friendRequestSelf;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getMailingAddress() {
        return mailingAddress;
    }
    
    public void setMailingAddress(String mailingAddress) {
        this.mailingAddress = mailingAddress;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getResearchFields() {
        return researchFields;
    }
    
    public void setResearchFields(String researchFields) {
        this.researchFields = researchFields;
    }

	public List<User> getFriends() {
		return friends;
	}

	public void setFriends(List<User> friends) {
		this.friends = friends;
	}

	public List<User> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(List<User> subscribers) {
		this.subscribers = subscribers;
	}

	public List<User> getFriendRequestSender() {
		return friendRequestSender;
	}

	public void setFriendRequestSender(List<User> friendRequestSender) {
		this.friendRequestSender = friendRequestSender;
	}

	public List<UserGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<UserGroup> groups) {
		this.groups = groups;
	}

	public void addSubscriber(User subscriber) {
		this.subscribers.add(subscriber);
		this.update();
	}


	
    public void addFriendRequest(User friendRequest) {
		this.friendRequestSender.add(friendRequest);
	}

	
	public void addFriend(User friend) {
		this.friends.add(friend);
        friend.update();
        this.update();
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", email='" + email + '\'' +
				", password='" + password + '\'' +
				", accessLevel='" + accessLevel + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", mailingAddress='" + mailingAddress + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", researchFields='" + researchFields + '\'' +
				", self=" + subscribeSelf +
				", friends=" + friends.toString() +
				", subscribers=" + subscribers.toString() +
				", friendRequestSender=" + friendRequestSender.toString() +
				", groups=" + groups +
				'}';
	}
	// public void setUserGroups(List<UserGroup> userGroups) {
	// 	this.groups = userGroups;
	// }

	// public List<UserGroup> getUserGroups() {
	// 	return this.groups;
	// }
	
	
	// public void addUserGroup(UserGroup userGroup) {
	// 	this.groups.add(userGroup);
	// 	this.update();
	// }
}
