package gui.views.login

import gui.views.UserView
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*


class LoginView : View("CMS") {
    private val service = Service()

    override val root : GridPane by fxml()
    private val usernameField : TextField by fxid()
    private val passwordField : PasswordField by fxid()
    private val loginButton: Button by fxid()
    private val createAccountButton: Button by fxid()

    init {
        loginButton.apply { action { handleLogin() }}
        createAccountButton.apply { action { handleCreateAccount()}}
    }

    private fun handleLogin() {
        val user = service.usersWithNameAndPassword(usernameField.text, passwordField.text)
        if(user.isNotEmpty()){
            replaceWith(UserView(user[0], service), ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
        }else{
            alert(Alert.AlertType.INFORMATION, "Bad credentials")
        }
    }

    private fun handleCreateAccount() {
        CreateAccountView().openWindow()
//        alert(Alert.AlertType.INFORMATION, "Create account")
    }
}