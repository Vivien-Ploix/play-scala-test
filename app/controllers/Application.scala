package controllers

import dao.UserDAO
import javax.inject.Inject
import models.User
import play.api._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms.mapping
import play.api.data.Forms.text
import play.api.mvc.{ AbstractController, ControllerComponents }
import scala.concurrent.ExecutionContext
import play.api.libs.mailer._
import org.apache.commons.mail.EmailAttachment

class Application @Inject() (
  mailerClient: MailerClient,
  userDao: UserDAO,
  controllerComponents: ControllerComponents)(implicit executionContext: ExecutionContext) extends AbstractController(controllerComponents) {

    def index() = Action { implicit request: Request[AnyContent] =>
        Ok(views.html.index())
    }

    val userForm = Form(
        mapping(
        "email" -> text(),
        "pseudo" -> text(),
        "password" -> text())(User.apply)(User.unapply))

    def insertUser = Action.async { implicit request =>
        val user: User = userForm.bindFromRequest.get
        userDao.insert(user).map(_ => {
            sendEmail(user.email);
            Redirect(routes.Application.index)
        })
    }


    def sendEmail(mail: String) = {
        val email = Email(
        "Registration email",
        "Mister FROM <vivien78@live.fr>",
        Seq(s"Miss TO <$mail>"),
        bodyText = Some("Congratulations, you are now a PyramidRace player !"),
        )
        mailerClient.send(email)
    }
}