package models

import javax.inject.Inject

import models.Tables.UsersChatsRow
import play.api.db.slick.DatabaseConfigProvider
import slick.backend.DatabaseConfig
import slick.driver.JdbcProfile
import slick.jdbc.JdbcBackend
import telegram.Contact


import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by v-yaroslavskyi on 2/12/17.
  */

class UserChat @Inject()(dbConfigProvider: DatabaseConfigProvider)
                        (implicit exc: ExecutionContext) {

  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db
  val userChats = Tables.UsersChats
  import dbConfig.driver.api._

  def get(tel: String): Future[Option[UsersChatsRow]] =
    db.run(userChats.filter(_.telephone.like(s"%$tel%")).result.headOption)

  def insert(user: UsersChatsRow): Future[Int] =
    db.run(userChats returning userChats.map(_.id) += user)

  def del(id: Int) =
    db.run(userChats.filter(_.id === id).delete)

}