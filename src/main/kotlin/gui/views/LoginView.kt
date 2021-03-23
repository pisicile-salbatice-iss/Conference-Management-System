package gui.views

import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.View
import tornadofx.action
import tornadofx.alert


class LoginView : View("CMS") {
    override val root : GridPane by fxml()
    val usernameField : TextField by fxid()
    val passwordField : TextField by fxid()
    private val loginButton: Button by fxid()
    private val createAccountButton: Button by fxid()

    private val service = Service()

    init {
        loginButton.apply { action { handleLogin() }}
        createAccountButton.apply { action { handleCreateAccount()}}
    }

    private fun handleLogin() {
        if(service.isUserExistent(usernameField.text, passwordField.text)){
            alert(Alert.AlertType.INFORMATION, "Welcome " + usernameField.text)
        }else{
            alert(Alert.AlertType.INFORMATION, "Bad credentials")
        }
    }

    private fun handleCreateAccount() {
        CreateAccountView().openWindow()
//        alert(Alert.AlertType.INFORMATION, "Create account")
    }
}
