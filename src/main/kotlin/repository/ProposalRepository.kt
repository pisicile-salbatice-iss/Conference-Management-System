package repository

import domain.Conference
import domain.Proposal
import java.sql.DriverManager

class ProposalRepository (private val url: String, private val db_user: String, private val db_password: String) {
    init {
        val sqlCreateTableQuery = """
                CREATE TABLE IF NOT EXISTS Proposals (
                    id INT PRIMARY KEY,
                    ucid INT REFERENCES userconference(ucid),
                    text varchar(1000)
                )"""
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCreateTableQuery)
            preparedStatement.executeUpdate()
        }
    }

    fun addProposal(proposal: Proposal) {
        val sqlCommand = "INSERT INTO Proposals (id, ucid, text) VALUES (?, ?, ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, proposal.id)
            preparedStatement.setInt(2, proposal.userConferenceId)
            preparedStatement.setString(3, proposal.text)
            preparedStatement.executeUpdate()
        }
    }

    fun getProposals(): List<Proposal> {
        val proposals = mutableListOf<Proposal>()
        val sqlCommand = "SELECT * FROM Proposals"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposal = Proposal(rs.getInt("id"), rs.getInt("ucid"), rs.getString("text"))
                proposals.add(proposal)
            }
        }
        return proposals
    }

    fun getProposalsOfUser(userId: Int): List<Proposal> {
        val proposals = mutableListOf<Proposal>()
        val sqlCommand = "SELECT * FROM Proposals WHERE ucid IN (SELECT * FROM UserConference WHERE UserConference.uid = ?)"
        DriverManager.getConnection(url, db_user, db_password).use { connection ->
            val preparedStatement = connection.prepareStatement(sqlCommand)
            preparedStatement.setInt(1, userId)
            val rs = preparedStatement.executeQuery()
            while (rs.next()) {
                val proposal = Proposal(rs.getInt("id"), rs.getInt("ucid"), rs.getString("text"))
                proposals.add(proposal)
            }
        }
        return proposals
    }
}