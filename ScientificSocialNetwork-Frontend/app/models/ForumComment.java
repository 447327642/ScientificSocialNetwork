package models;

public class ForumComment {
  private Integer commentId;
  private Integer postId;
  private String userName;
  private String replyTo;
  private String content;
  private String timestamp;

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public ForumComment() {
  }

  public ForumComment(Integer commentId, Integer postId, String userName,
      String replyTo, String content, String timestamp) {
    this.commentId = commentId;
    this.postId = postId;
    this.userName = userName;
    this.replyTo = replyTo;
    this.content = content;
    this.timestamp = timestamp;
  }

  public Integer getCommentId() {
    return commentId;
  }

  public void setCommentId(Integer commentId) {
    this.commentId = commentId;
  }

  public Integer getPostId() {
    return postId;
  }

  public void setPostId(Integer postId) {
    this.postId = postId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getReplyTo() {
    return replyTo;
  }

  public void setReplyTo(String replyTo) {
    this.replyTo = replyTo;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
