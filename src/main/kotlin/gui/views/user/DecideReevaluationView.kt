package gui.views.user

import domain.Conference;
import domain.Proposal
import domain.User;
import exceptions.ConferenceException
import javafx.collections.FXCollections
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service;
import tornadofx.*

class DecideReevaluationView(
    private val user:User,
    private val service : Service,
    private val parent: View,
    private val conference:Conference
) : View(user.name + " - " + conference.name){
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val acceptProposalButton: Button by fxid()
    private val conflictingProposals: ListView<Proposal> by fxid()

    private val reviewersOfProposal: ListView<User> by fxid()

    init {
        goBackButton.apply {
            action {
                goBackHandle()
            }
        }

        conflictingProposals.onLeftClick {
            selectProposalHandle()
        }

        acceptProposalButton.apply{
            action {
                acceptProposalHandle()
            }
        }
        loadData()
    }

    private fun goBackHandle(){
        replaceWith(
            parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT)
        )
    }

    private fun selectProposalHandle(){
        val proposal = conflictingProposals.selectionModel.selectedItem

        reviewersOfProposal.items.setAll(FXCollections.observableArrayList(service.getReviewersOfProposal(proposal.id)))
    }

    private fun acceptProposalHandle(){
        try{
            service.acceptProposal(conflictingProposals.selectionModel.selectedItem.id)
            loadData()
        }catch (exc: ConferenceException){
            alert(Alert.AlertType.INFORMATION, "Could not accept paper")
        }

    }

    private fun loadData(){
        val observable = FXCollections.observableArrayList(service.getConflictingProposals(conference.id))
        conflictingProposals.items.setAll(observable)
        reviewersOfProposal.items.setAll(FXCollections.observableArrayList())
    }
}
