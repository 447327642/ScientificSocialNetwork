package actors

import play.api.libs.json.{JsValue, Json, Writes}
import play.twirl.api.HtmlFormat

case class ChatMessage(topic: String, user: String, text: String, created: java.util.Date)

object ChatMessage {
  implicit val chatMessageWrites = new Writes[ChatMessage] {
    def writes(chatMessage: ChatMessage): JsValue = {
      Json.obj(
        "type" -> "message",
        "topic" -> chatMessage.topic,
        "user" -> chatMessage.user,
        "text" -> multiLine(chatMessage.text)
      )
    }
  }

  private def multiLine(text: String) = {
    HtmlFormat.raw(text).body.replace("\n", "<br/>")
  }
}

