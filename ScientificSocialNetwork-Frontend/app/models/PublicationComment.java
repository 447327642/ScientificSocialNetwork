package models;

/**
 * Created by why on 4/21/16.
 */
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.ws.WSClient;
import play.libs.ws.WSResponse;
import utils.Constants;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class PublicationComment{
//    @Inject
//    private static WSClient ws;
//    private final static String CREATE = Constants.URL_HOST + Constants.CMU_BACKEND_PORT + "publication/addComment";
    public Publication publication;
    private long id;
    // one to one
    private long user_id;
    private String userName;
    private long timestamp;
    private String content;
    private int thumb;

    //TODO reply


    public PublicationComment() {

    }

    public PublicationComment(Publication publication, long user_id, long timestamp, String content, String userName) {
        this.publication = publication;
        this.user_id = user_id;
        this.userName = userName;
        this.timestamp = timestamp;
        this.content = content;
        this.thumb = 0;
    }

//    public static JsonNode create(ObjectNode node) {
//
//        CompletionStage<JsonNode> jsonPromise = ws.url(CREATE).post(node).thenApply(WSResponse::asJson);
//        CompletableFuture<JsonNode> jsonFuture = jsonPromise.toCompletableFuture();
//        JsonNode responseNode = jsonFuture.join();
//        return responseNode;
//    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserId() { return user_id; }

    public void setUserId(long user_id) {
        this.user_id = user_id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PublicationComment{" +
                "publication=" + publication.getId() +
                ", id=" + id +
                ", user_id=" + user_id +
                ", userName='" + userName + '\'' +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                ", thumb=" + thumb +
                '}';
    }
}

