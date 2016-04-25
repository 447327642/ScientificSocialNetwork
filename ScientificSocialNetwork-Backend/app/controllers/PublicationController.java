/**
 * @author xingyuchen
 * Created on Apr 21, 2016
 */
package controllers;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

import models.*;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Common;

/**
 * @author xingyuchen
 *
 */
public class PublicationController extends Controller{
	public Result getAllPublications(String format) {
		// Use the Finder.find in model to get the data. Not only all() but
		// also a lot of other functions to use, check http://ebean-orm.github.io/
		// to see details
		List<Publication> publications = Publication.find.all();
		
		if (publications == null) {
			System.out.println("No publication found");
		}

		// Use the json in Play library this time
		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(publications);
			result = jsonNode.toString();
		}
		return ok(result);	
	}

	public Result getPublicationPanel(long id) {
		Publication publication = Publication.find.byId(id);
		if (publication == null) {
			System.out.println("No publication found");
		}
		
		publication.setCount(publication.getCount() + 1);
		publication.save();
		
		String result = new String();
		JsonNode jsonNode = Json.toJson(publication);
		result = jsonNode.toString();
		//System.out.println(result);
		return ok(result);
	}
	
	public Result getMostPopularPublications(String format) {
		List<Publication> publications = Publication.find.orderBy("count asc").setMaxRows(100).findList();
		
		if (publications == null) {
			System.out.println("No publication found");
		}
		
		// Use the json in Play library this time
		String result = new String();
		if (format.equals("json")) {
			JsonNode jsonNode = Json.toJson(publications);
			result = jsonNode.toString();
		}
		return ok(result);	
	}

	/**
	 * Comment: by Haoyun
	 */
	public Result addComment() {
		try{
			JsonNode json = request().body().asJson();
			if(json==null){
				System.out.println("Comment not created, expecting Json data");
				return Common.badRequestWrapper("Comment not created, expecting Json data");
			}

			long userId = json.path("userID").asLong();
			long timestamp = json.path("timestamp").asLong();
			long publicationId = json.path("publicationID").asLong();
			String content = json.path("Content").asText();


			User user = User.find.byId(userId);
			String userName = user.getFirstName() + " " + user.getLastName();
			if(user==null){
				System.out.println("Cannot find user with given user id");
				return Common.badRequestWrapper("Cannot find user with given user id");
			}
			Publication publication = Publication.find.byId(publicationId);
			if(publication==null){
				System.out.println("Cannot find workflow with given workflow id");
				return Common.badRequestWrapper("Cannot find workflow with given workflow id");
			}
			PublicationComment comment = new PublicationComment(publication, userId, timestamp, content, userName);
			comment.save();

			List<PublicationComment> list = PublicationComment.find.all();
			list.add(comment);

			publication.setComments(list);
			publication.save();

			JsonNode jsonNode = Json.toJson(comment);
			String result = jsonNode.toString();
			return ok(result);
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Failed to add comment!");
		}
	}

	// Post
	public Result addReply() {
		JsonNode jsonNode = request().body().asJson();
		if (jsonNode == null){
			System.out.println("Reply not added, expecting Json data");
			return Common.badRequestWrapper("Reply not added, expecting Json data");
		}

		long commentId = jsonNode.path("commentId").asLong();
		long fromUserId = jsonNode.path("fromUserId").asLong();
		long toUserId = jsonNode.path("toUserId").asLong();
		long timestamp = jsonNode.path("timestamp").asLong();
		String content = jsonNode.path("content").asText();
		PublicationComment comment = PublicationComment.find.byId(commentId);
		if(comment==null){
			System.out.println("Cannot find comment!");
			return Common.badRequestWrapper("Cannot find comment!");
		}
		User fromUser = User.find.byId(fromUserId);
		if(fromUser==null){
			System.out.println("Cannot find fromUser!");
			return Common.badRequestWrapper("Cannot find fromUser!");
		}
		User toUser = User.find.byId(toUserId);
		if(toUser==null){
			System.out.println("Cannot find toUser!");
			return Common.badRequestWrapper("Cannot find toUser!");
		}

		PublicationReply reply = new PublicationReply(comment, fromUser, toUser, timestamp, content);
		reply.save();

		List<PublicationReply> replyList = PublicationReply.find.where().eq("publication_comment_id", commentId).findList();
		//replyList.add(reply);
		comment.setReplies(replyList);
		comment.save();

		JsonNode responseJson = Json.toJson(reply);
		String result = responseJson.toString();
		return ok(result);
	}

	public Result getComments(Long publicationId) {
		try{
			if(publicationId==null){
				System.out.println("Expecting workflow id");
				return Common.badRequestWrapper("Expecting workflow id");
			}

			List<PublicationComment> comments = PublicationComment.find.where().eq("publication_id", publicationId).findList();
//			for (PublicationComment comment: comments) {
//				Set<User> empty = new HashSet<>();
//				comment.getUser().setFollowers(empty);
//				comment.getUser().setFriends(empty);
//			}


			String result = new String();
			JsonNode jsonNode = Json.toJson(comments);
			result = jsonNode.toString();
			return ok(result);
		} catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Failed to fetch comments");
		}
	}

	public Result thumbUp(Long commentId) {
		try{
			if(commentId==null){
				System.out.println("Expecting comment id");
				return Common.badRequestWrapper("Expecting comment id");
			}
			PublicationComment comment = PublicationComment.find.byId(commentId);
			comment.setThumb(comment.getThumb() + 1);
			comment.save();
			return ok("{\"success\":\"Success!\"}");
		}catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Fail to fetch replies");
		}
	}

	public Result thumbDown(Long commentId) {
		try{
			if(commentId==null){
				System.out.println("Expecting comment id");
				return Common.badRequestWrapper("Expecting comment id");
			}
			PublicationComment comment = PublicationComment.find.byId(commentId);
			comment.setThumb(comment.getThumb() - 1);
			comment.save();
			return ok("{\"success\":\"Success!\"}");
		}catch (Exception e){
			e.printStackTrace();
			return Common.badRequestWrapper("Fail to fetch replies");
		}
	}
}
