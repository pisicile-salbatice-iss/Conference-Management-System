package gui.views.user

import domain.*
import exceptions.ConferenceException
import gui.views.conference.BidProposalView
import gui.views.login.CreateAccountView
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*
import java.text.SimpleDateFormat
import java.util.*


class ChairView(private val user: User,
                private val service: Service,
                private val parent: View,
                private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val bidProposalButton: Button by fxid()
    private val sendPaperButton: Button by fxid()
    private val postponeDeadlinesButton: Button by fxid()
    private val sendResultsButton: Button by fxid()
    private val ViewSessions: Button by fxid()
    private val ListOfPapers: ListView<Proposal> by fxid()
    private val ListOfPCMembers: ListView<UserConference> by fxid()
    init {
        goBackButton.apply {
            action{
                goBackHandle()
            }
        }

        postponeDeadlinesButton.apply{
            action {
                postponeDeadlines()
            }
        }

        bidProposalButton.apply {
            action{
                bidProposalHandle()
            }
        }

        sendPaperButton.apply {
            action{
                sendPaperToPCMemberHandle()
            }
        }

        sendResultsButton.apply {
            action {
                sendResultsHandle()
            }
        }
        ViewSessions.apply {
            action {
                showSessions()
            }
        }

        loadPCMembers()
        loadPapers()
    }

    private fun sendResultsHandle() {
        val reviewPaperDeadline = conference.reviewPaperDeadline

        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        if(Date(year, month, day) >= reviewPaperDeadline){
            alert(Alert.AlertType.INFORMATION, "Review deadline has not passed")
            return
        }

        replaceWith(SendResultsView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.UP)
        )
    }

    private fun postponeDeadlines(){
        replaceWith(PostponeDeadlinesView(user, service, this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun bidProposalHandle(){
        replaceWith(BidProposalView(user, service,this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }
    private fun showSessions(){
        replaceWith(SessionsView(user,service,this,conference), ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }

    private fun sendPaperToPCMemberHandle(){
        val proposal = ListOfPapers.selectionModel.selectedItem
        val reviewer = ListOfPCMembers.selectionModel.selectedItem
        if(proposal == null)
        {
            alert(Alert.AlertType.INFORMATION, "Select a proposal")
            return
        }
        if(reviewer == null)
        {
            alert(Alert.AlertType.INFORMATION, "Select a reviewer")
            return
        }
        try {
            service.assignPaper(proposal.id, reviewer.userId)
            alert(Alert.AlertType.INFORMATION, "Assigned paper");
        }
        catch (exception: ConferenceException)
        {
            alert(Alert.AlertType.ERROR, exception.message)
            return
        }
    }
    private fun loadPapers() {
        val observable = FXCollections.observableArrayList(service.getProposals())
        ListOfPapers.items.addAll(observable)
    }
    private fun loadPCMembers() {
        val users = FXCollections.observableArrayList(service.getUsersOfConference(conference.id).filter { userConference -> userConference.conferenceId == conference.id &&
                userConference.role == Role.REVIEWER })
        ListOfPCMembers.items.addAll(users)
    }


}
