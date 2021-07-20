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

class Application @Inject() (
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
    userDao.insert(user).map(_ => Redirect(routes.Application.index))
  }

}