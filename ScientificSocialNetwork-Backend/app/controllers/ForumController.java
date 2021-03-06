package controllers;

import models.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import com.fasterxml.jackson.databind.JsonNode;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

@Named
@Singleton
public class ForumController extends Controller {
  public ForumController() {
  }

  public Result addNewPost() {
    JsonNode postNode = request().body().asJson();
    System.out.println(postNode.asText());
    if (postNode == null) {
      return badRequest("Post not saved, expecting json data.");
    }
    Long postId = postNode.findPath("postId").asLong();
    Long userId = postNode.findPath("userId").asLong();
    String postTitle = postNode.findPath("title").asText();
    String postContent = postNode.findPath("content").asText();
    String paperLink = postNode.findPath("link").asText();

    /*
    * Specify type, default as "discussion", optionally "question"
    * */
    String type = null;
    JsonNode typeNode = postNode.findPath("type");
    if (typeNode == null) type = "discussion";
    else type = typeNode.asText();

    User user = User.find.byId(userId);

    if (user != null) {
      System.out.println("User: " + user.getId());
    } else {
      System.out.println("User not found.");
    }

    ForumPost forumPost;
    System.out.println("addNewPost(): postId=" + postId);
    if (postId != 0) {
      forumPost = ForumPost.find.byId(postId);
      forumPost.setPostTitle(postTitle);
      forumPost.setPostContent(postContent);
      forumPost.setPaperLink(paperLink);
      forumPost.setType(type);
      forumPost.save();
    } else {
      forumPost = new ForumPost(user, postTitle, postContent,
          paperLink, -1L, type);
    }

    forumPost.save();
    return created(Json.toJson(forumPost.getPostId()).toString());
  }

  public Result addNewComment() {
    JsonNode commentNode = request().body().asJson();
    if (commentNode == null) {
      return badRequest("Comment not saved, expecting json data.");
    }

    Long postId = commentNode.findPath("postId").asLong();
    String userName = commentNode.findPath("userName").asText();
    String replyTo = commentNode.findPath("replyTo").asText();
    String content = commentNode.findPath("content").asText();

    ForumPost post = ForumPost.find.byId(postId);
    // TODO: change to username
    User user = User.find.where().eq("firstName", userName).findUnique();
    User replyToUser = User.find.where().eq("firstName", replyTo).findUnique();

    ForumPostComment comment = new ForumPostComment(post, user, content,
        replyToUser);
    comment.save();

    return created(Json.toJson(comment.getCid()).toString());
  }

  public Result getOnePost(Long id) {
    ForumPost post = ForumPost.find.byId(id);
    List<ForumPostComment> comments = ForumPostComment.find.where()
        .eq("post_id", id).findList();
    DetailedForumPost detailedPost = new DetailedForumPost(post, comments);

    return ok(Json.toJson(detailedPost));
  }

  public Result getPosts(Integer start, Integer limit) {
    List<ForumPost> posts = ForumPost.find.setMaxRows(limit).findList();
    return ok(Json.toJson(posts));
  }

  public Result votePost() {
    JsonNode voteNode = request().body().asJson();
    if (voteNode == null) {
      return badRequest("vote not saved, expecting json data.");
    }

    Long pid = voteNode.findPath("pid").asLong();
    Long uid = voteNode.findPath("uid").asLong();
    Integer updown = voteNode.findPath("updown").asInt();

    ForumPost post = ForumPost.find.byId(pid);
    User user = User.find.byId(uid);
    ForumPostRating rating = new ForumPostRating(user, updown, post);
    rating.save();
    return created(Json.toJson(rating.getRid()).toString());
  }

  public Result getPostUpvoteCount(Long pid) {
    Integer count = ForumPostRating.find.where().eq("updown", 1).findList()
        .size();
    return ok(Json.toJson(count).toString());
  }

  public Result getPostDownvoteCount(Long pid) {
    Integer count = ForumPostRating.find.where().eq("updown", -1).findList()
        .size();
    return ok(Json.toJson(count).toString());
  }

  public Result voteComment() {
    JsonNode voteNode = request().body().asJson();
    if (voteNode == null) {
      return badRequest("vote not saved, expecting json data.");
    }

    Long cid = voteNode.findPath("cid").asLong();
    Long uid = voteNode.findPath("uid").asLong();
    Integer updown = voteNode.findPath("updown").asInt();

    ForumPostComment comment = ForumPostComment.find.byId(cid);
    User user = User.find.byId(uid);
    ForumPostCommentRating rating = new ForumPostCommentRating(user, updown,
        comment);
    rating.save();
    return created(Json.toJson(rating.getRid()).toString());
  }

  public Result commentThumbUp(Long cid) {
    ForumPostComment comment = ForumPostComment.find.byId(cid);
    comment.setVote(comment.getVote() + 1);
    comment.save();
    return created(Json.toJson(comment.getVote()));
  }
  
  public Result commentThumbDown(Long cid) {
    ForumPostComment comment = ForumPostComment.find.byId(cid);
    comment.setVote(comment.getVote() - 1);
    comment.save();
    return created(Json.toJson(comment.getVote()));
  }
  
  
  public Result getCommentUpvoteCount(Long cid) {
    Integer count = ForumPostCommentRating.find.where().eq("updown", 1)
        .findList().size();
    return ok(Json.toJson(count).toString());
  }

  public Result getCommentDownvoteCount(Long cid) {
    Integer count = ForumPostCommentRating.find.where().eq("updown", -1)
        .findList().size();
    return ok(Json.toJson(count).toString());
  }


  /**
   * Alfred: To display only title and current rating of posts.
   * @return
     */
  public Result getPostsWithVoteCounts () {
    // Get all posts
    System.out.println("getPostsWithVoteCounts");
    List<ForumPost> posts = ForumPost.find.all();
    System.out.println("Number of posts found: " + posts.size());
    List<PostTitle> titles = new ArrayList<>();
    for (ForumPost post : posts) {

      Integer upvoteCount = ForumPostRating
              .find
              .where()
              .eq("post_id", post.getPostId())
              .eq("updown", 1)
              .findList().size();

      Integer downvoteCount = ForumPostRating
              .find
              .where()
              .eq("post_id", post.getPostId())
              .eq("updown", -1)
              .findList().size();

      PostTitle title = new PostTitle ();
      title.setPostId(post.getPostId());
      title.setPostTitle(post.getPostTitle());
      title.setUpvote(upvoteCount);
      title.setDownvote(downvoteCount);
      /**
       * Type Default to discussion
       */
      System.out.format("Post %d Type: %s\n", post.getPostId(), post.getType());
      if (post.getType() == null || post.getType().length() == 0) {
        title.setPostType("discussion (unspecified)");
      } else {
        title.setPostType(post.getType());
      }
      titles.add(title);
    }
    return ok(Json.toJson(titles).toString());
  }

  public Result createPost () {
    JsonNode json = request().body().asJson();
//    System.out.println("=============");
    System.out.println(json);
    ForumPost post = new ForumPost();
    post.setPaperLink(json.get("link").asText());
    post.setPostTitle(json.get("title").asText());
    post.setPostContent(json.get("content").asText());
    post.save();
        return created(post.getPostId()+"");
//    return created(new Gson().toJson(post.getPostId()));
  }
}
