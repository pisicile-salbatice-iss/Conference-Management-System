package repository

import domain.User
import java.sql.DriverManager

class UserRepository (private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Users (
                    id INT PRIMARY KEY,
                    name VARCHAR(50),
                    password VARCHAR(50)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun findUserById(id: Int): User? {
        val sqlCommand = "SELECT * FROM Users WHERE id = $id"
        var user: User? = null
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            if (rs.next())
                user = User(rs.getInt("id"), rs.getString("user"), rs.getString("password"))
        }
        return user
    }

    fun getUsers(): List<User> {
        val users = mutableListOf<User>()
        val sqlCommand = "SELECT * FROM Users"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val user = User(rs.getInt("id"), rs.getString("user"), rs.getString("password"))
                users.add(user)
            }
        }
        return users
    }

    fun addUser(user: User) {
        val sqlCommand = "INSERT INTO Users (id, name, password) VALUES (${user.id}, ${user.name}, ${user.password})"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.executeUpdate()
        }
    }
}
