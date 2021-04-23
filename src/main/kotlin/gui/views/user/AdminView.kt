package gui.views.user

import domain.Conference
import domain.Role
import exceptions.ConferenceException
import gui.views.login.LoginView
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import java.sql.Date
import javax.mail.Message
import javax.mail.MessagingException
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage


class AdminView(private val service: Service) : View() {
    override val root: GridPane by fxml()
    private val nameField: TextField by fxid()
    private val priceField: TextField by fxid()
    private val dateField: DatePicker by fxid()
    private val addConferenceButton: Button by fxid()
    private val conferenceListView: ListView<Conference> by fxid()
    private val chairNameField: TextField by fxid()
    private val coChairRadioButton: CheckBox by fxid()
    private val inviteChairButton: Button by fxid()
    private val logoutButton: Button by fxid()
    private val submitPaperDeadline: DatePicker by fxid()
    private val reviewPaperDeadline: DatePicker by fxid()


    init {
        conferenceListView.items.addAll(service.getConferences().asObservable())
        addConferenceButton.apply { action { handleCreateConference() } }
        inviteChairButton.apply { action { handleInviteChair() } }
        logoutButton.apply { action { handleLogout() }}
    }

    private fun handleLogout() {
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }

    private fun handleInviteChair() {
        val username = chairNameField.text
        if (!service.isUsernameExistent(username)) {
            alert(Alert.AlertType.ERROR, "Username does not exist in the database")
            return
        }

        val id = service.getIdOfUsername(username)
        val email = service.getEmailOfUser(username)

        val conference = getSelectedConference()
        if (conference == null) {
            alert(Alert.AlertType.ERROR, "No conference selected")
            return
        }

        val from = "admin@cms.com"
        val host = "localhost"
        val properties = System.getProperties()
        properties.setProperty("mail.smtp.host", host)
        val session = Session.getDefaultInstance(properties)
        val isCoChair = coChairRadioButton.isSelected
        try {
            val message = MimeMessage(session)
            message.setFrom(InternetAddress(from))
            message.addRecipient(Message.RecipientType.TO, InternetAddress(email))
            message.subject = "Conference Invitation"
            var text: String = if (isCoChair) {
                "You are invited as a co-chair to conference "
            } else {
                "You are invited as a chair to conference "
            }
            text += conference.name
            text += ". We are looking forward to hearing from you!"
            message.setText(text)
            Transport.send(message)
            alert(Alert.AlertType.CONFIRMATION, "Email sent successfully")
        } catch (exception: MessagingException) {
            alert(Alert.AlertType.ERROR, "Email could not be sent: ${exception.message}")
            return
        }

        service.addUserToConference(
            uid = id,
            cid = conference.id,
            role = if (isCoChair) {
                Role.CO_CHAIR
            } else {
                Role.CHAIR
            },
            paid = false
        )
    }

    private fun getSelectedConference(): Conference? {
        return conferenceListView.selectedItem
    }

    private fun handleCreateConference() {
        if (priceField.text.toInt() < 0) {
            alert(Alert.AlertType.ERROR, "The price for the conference must be greater than 0")
            return
        }
        if (nameField.text.isBlank()) {
            alert(Alert.AlertType.ERROR, "The conference must have a name")
            return
        }
        try {
            service.addConference(nameField.text, Date.valueOf(dateField.value), priceField.text.toInt(), Date.valueOf(submitPaperDeadline.value), Date.valueOf(reviewPaperDeadline.value))
            alert(Alert.AlertType.INFORMATION, "Conference added successfully")

        } catch (exception: ConferenceException) {
            alert(Alert.AlertType.ERROR, exception.message)
            return
        }
        conferenceListView.items.clear()
        conferenceListView.items.addAll(service.getConferences().asObservable())

    }


}
