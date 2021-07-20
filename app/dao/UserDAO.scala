package dao

import scala.concurrent.{ ExecutionContext, Future }
import javax.inject.Inject

import models.User
import play.api.db.slick.DatabaseConfigProvider
import play.api.db.slick.HasDatabaseConfigProvider
import slick.jdbc.JdbcProfile

class UserDAO @Inject() (protected val dbConfigProvider: DatabaseConfigProvider)(implicit executionContext: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {
  import profile.api._

  private val Users = TableQuery[UsersTable]

  def insert(user: User): Future[Unit] = db.run(Users += user).map { _ => () }

  private class UsersTable(tag: Tag) extends Table[User](tag, "USER") {

    def email = column[String]("EMAIL")
    def pseudo = column[String]("PSEUDO")
    def password = column[String]("PASSWORD")

    def * = (email, pseudo, password) <> (User.tupled, User.unapply)
  }
}
