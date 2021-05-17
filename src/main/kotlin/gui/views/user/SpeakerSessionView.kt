package gui.views.user

import domain.Conference
import domain.Proposal
import domain.Session
import domain.User
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.GridPane
import javafx.stage.FileChooser
import org.apache.commons.io.FileUtils
import service.Service
import tornadofx.View
import tornadofx.action
import tornadofx.label
import java.io.File
import java.io.IOException

class SpeakerSessionView(
    private val user: User,
    private val service: Service,
    private val parent: View,
    private val conference: Conference,
    private val session: Session
) : View(user.name + " - " + session.topic) {
    override val root: GridPane by fxml()
    private val timeToPresentLabel: Label by fxid()
    private val paperTextLabel: Label by fxid()
    private val addPresentationButton: Button by fxid()
    private val paper: Proposal = service.getPaperOfSpeakerAtSession(user, conference, session)

    init {
        print(paper.paperText)
        paperTextLabel.text = paper.paperText
        timeToPresentLabel.text =service.getDateOfPresentation(user, paper, conference, session).toString()
        addPresentationButton.apply {
            action {
                uploadPresentation()
            }
        }
    }
    private fun uploadPresentation() {
        val fileChooser: FileChooser = FileChooser()
        fileChooser.title = "Upload presentation"
        fileChooser.extensionFilters.addAll(
            FileChooser.ExtensionFilter("PDF", "*.pdf"),
            FileChooser.ExtensionFilter("PPT", "*.ppt"),
            FileChooser.ExtensionFilter("PPTX", "*.pptx")
        )
        val file = fileChooser.showOpenDialog(null)
        println(file.extension)

        val destinationFile = File("src/main/resources/presentations/" + user.name + "." + file.extension)
        try {
            file.copyTo(destinationFile)
        } catch (exception: IOException) {
            exception.printStackTrace()
            return
        }
        Alert(Alert.AlertType.CONFIRMATION, "File was uploaded")
    }
}
