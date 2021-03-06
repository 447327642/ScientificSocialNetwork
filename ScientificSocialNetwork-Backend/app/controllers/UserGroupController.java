package controllers;

import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

import javax.persistence.PersistenceException;

import com.fasterxml.jackson.databind.JsonNode;

import models.User;
import models.UserGroup;


import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Common;


public class UserGroupController extends Controller {


    //post create group
    public Result createGroup() {
        JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("group not created, expecting Json data");
            return Common.badRequestWrapper("group not created, expecting Json data");
        }

        long userID = json.path("userID").asLong();
        int access = json.path("access").asInt();
        String groupName = json.path("groupName").asText();
        String groupDescription = json.path("groupDescription").asText();
        String topic = json.path("topic").asText();
		
		User user = User.find.byId(userID);
        System.out.println("user is " + user);
        List<User> groupMembers = new ArrayList<User>();
        groupMembers.add(user);

        UserGroup group = new UserGroup( userID, groupName,  groupDescription,  access,  topic, groupMembers);
        System.out.println("group is " + group);
        group.save();
        
        return created(Json.toJson(group.getGroupName()));
    }

    //post
    public Result addMembersToGroup() {
         JsonNode json = request().body().asJson();
        if (json == null) {
            System.out.println("group not created, expecting Json data");
            return Common.badRequestWrapper("group not created, expecting Json data");
        }

        long userID = json.path("userID").asLong();
        long groupID = json.path("groupID").asLong();


		User user = User.find.byId(userID);
		UserGroup group = UserGroup.find.byId(groupID);

        if(group == null) {
            return Common.badRequestWrapper("Failed to add member!");
        }
        else {
            // group.addUserToGroup(user);
            // user.addUserGroup(group);
        }
    
		String result = new String();
		result ="Added user to group";
        return ok(result);
    }

    //get
    public Result getGroupList(Long userID) {
        List<UserGroup> groups = UserGroup.find.where().eq("creator_user", userID).findList();;
        
        if (groups == null) {
            System.out.println("No user group found");
        }

        // Use the json in Play library this time
        String result = new String();
        JsonNode jsonNode = Json.toJson(groups);
        result = jsonNode.toString();
        return ok(result);  
    }

    //get
    public Result getGroupMember(Long groupId) {
        String result = new String();
        return ok(result);
    }
}