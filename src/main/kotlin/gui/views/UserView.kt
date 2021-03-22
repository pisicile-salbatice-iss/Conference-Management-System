package gui.views

import javafx.scene.layout.GridPane
import tornadofx.View

class UserView: View("User") {
    override val root : GridPane by fxml()
}