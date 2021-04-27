package repository

import domain.Conference
import domain.Session
import java.sql.DriverManager

class SessionRepository(private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Sessions (
                    sessionId INT PRIMARY KEY,
                    conferenceId INT REFERENCES Conferences(id),
                    topic VARCHAR(50),
                    time TIME
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }

    }

    fun getSessions(): List<Session> {
        val sessions = mutableListOf<Session>()
        val sqlCommand = "SELECT * FROM Sessions"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val session = Session(rs.getInt("sessionId"),rs.getInt("confereceId"),rs.getString("topic"), rs.getTime("time"))
                sessions.add(session)
            }
        }
        return sessions
    }

    fun findSessionsByConferecenceId(conferenceId: Int): List<Session> {
        val sqlCommand = "SELECT * FROM Sessions WHERE conferenceId = ?"
        val sessions = mutableListOf<Session>()
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, conferenceId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val session = Session(rs.getInt("sessionId"),rs.getInt("conferenceId"),rs.getString("topic"), rs.getTime("time"))
                sessions.add(session)
            }
        }
        return sessions
    }

    fun addSession(entity: Session) {
        val sqlCommand = "INSERT INTO Sessions (sessionId,conferenceId,topic, time) VALUES (?, ?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, entity.sessionId)
            preparedStatement.setInt(2, entity.confereceId)
            preparedStatement.setString(3, entity.topic)
            preparedStatement.setTime(4, entity.hour)
            preparedStatement.executeUpdate()
        }
    }
}