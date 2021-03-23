package service

import domain.Conference
import domain.Role
import domain.User
import repository.ConferenceRepository
import repository.UserConferenceRepository
import repository.UserRepository
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import kotlin.streams.toList

class Service {
    private val userRepository: UserRepository
    private val conferenceRepository: ConferenceRepository
    private val userConferenceRepository: UserConferenceRepository

    init {
        val configs = readSettingsFile()
        userRepository = UserRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        conferenceRepository = ConferenceRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
        userConferenceRepository = UserConferenceRepository(configs["database"]!!, configs["user"]!!, configs["password"]!!)
    }

    private fun readSettingsFile(): HashMap<String, String> {
        val propertiesMap = HashMap<String, String>()
        val properties = Properties()
        val configFile = "data/settings.properties"
        val fileInputStream: FileInputStream = try {
            FileInputStream(configFile)
        } catch (exception: IOException) {
            println(exception.message)
            return propertiesMap
        }
        try {
            properties.load(fileInputStream)
            propertiesMap["database"] = properties.getProperty("database")
            propertiesMap["user"] = properties.getProperty("user")
            propertiesMap["password"] = properties.getProperty("password")
        } catch (ioException: IOException) {
            println("IOException: " + ioException.message)
        }
        return propertiesMap
    }

    fun findUserById(id: Int) = userRepository.findUserById(id)
    fun getUsers() = userRepository.getUsers()
    fun addUser(id: Int, name: String, password: String) = userRepository.addUser(User(id, name, password))
    fun findConferenceById(id: Int) = conferenceRepository.findConferenceById(id)
    fun getConferences() = conferenceRepository.getConferences()
    fun addConference(id: Int, name: String, date: Date, attendancePrice: Int) = conferenceRepository.addConference(
        Conference(id, name, date, attendancePrice)
    )
    fun getConferencesOfUser(uid: Int) = userConferenceRepository.getConferencesOfUser(uid)
    fun getUsersOfConference(cid: Int) = userConferenceRepository.getUsersOfConference(cid)
    fun addUserToConference(uid: Int, cid: Int, role: Role, paid: Boolean) = userConferenceRepository.addPair(uid, cid, role, paid)

    fun usersWithNameAndPassword(username: String, password: String): List<User> {
        return getUsers().stream().filter {
            (it.name == username) and (it.password == password)
        }.toList()
    }
}