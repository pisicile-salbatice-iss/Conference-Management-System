package gui.views.user

import domain.*
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.View
import tornadofx.ViewTransition
import tornadofx.action
import tornadofx.seconds
import java.nio.channels.SeekableByteChannel
import java.sql.Time

abstract class SessionsView (private val user: User,
                             private val service: Service,
                             private val parent: View,
                             private val conference: Conference
): View(user.name + " - " + conference.name) {
    override val root: GridPane by fxml()
    private val FinalPapers: ListView<Proposal> by fxid()
    private val Sessions: ListView<Session> by fxid()
    private val PapersOfSession: ListView<Proposal> by fxid()
    private val topic: TextField by fxid()
    private val goBack: Button by fxid()
    private val AddSession: Button by fxid()
    private val map = hashMapOf<Int, Any?>()

    init {
        goBack.apply {
            action{
                goBackHandle()
            }
        }
        AddSession.apply {
            action{
                addSession()
            }
        }

        loadSessions()
        loadPapers()
    }

    private fun goBackHandle(){
        replaceWith(parent,
            ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }

    private fun loadPapers() {
        val observable = FXCollections.observableArrayList(service.getAcceptedPapers(conference.id))
        FinalPapers.items.addAll(observable)
    }

    private fun loadSessions() {
        val observable = FXCollections.observableArrayList(service.getSessionsOfAConference(conference.id))
        Sessions.items.addAll(observable)
    }

    private fun addSession(){
        val t = topic.text
        service.addSession(conference.id,t,time = Time(16))
        loadSessions()
    }



}