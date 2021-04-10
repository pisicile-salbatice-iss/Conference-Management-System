package gui.views.user

import domain.Conference
import domain.User
import gui.views.conference.BidProposalView
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class ChairView(private val user: User,
                private val service: Service,
                private val parent: View,
                private val conference: Conference
) : View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val goBackButton: Button by fxid()
    private val bidProposalButton: Button by fxid()

    init {
        goBackButton.apply {
            action{
                goBackHandle()
            }
        }

        bidProposalButton.apply {
            action{
                bidProposalHandle()
            }
        }
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun bidProposalHandle(){
        replaceWith(BidProposalView(user, service,this, conference),
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }
}