package gui.views

import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import tornadofx.View
import tornadofx.action
import tornadofx.alert


class LoginView : View("CMS") {
    override val root : GridPane by fxml()
    val usernameField : TextField by fxid()
    val passwordField : TextField by fxid()
    val loginButton: Button by fxid()
    val createAccountButton: Button by fxid()

    init {
        loginButton.apply { action { handleLogin() }}
        createAccountButton.apply { action { handleCreateAccount()}}
    }

    private fun handleLogin() {
        alert(Alert.AlertType.INFORMATION, "Login")
    }

    private fun handleCreateAccount() {
        alert(Alert.AlertType.INFORMATION, "Create account")
    }
}
