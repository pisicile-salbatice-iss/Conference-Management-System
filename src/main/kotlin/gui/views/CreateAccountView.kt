package gui.views

import exceptions.ConferenceException
import javafx.scene.control.Alert
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.View
import tornadofx.action
import tornadofx.alert
import javafx.scene.control.Button
import javafx.scene.control.TextField

class CreateAccountView : View("Create account") {
    override val root : GridPane by fxml()
    val emailField : TextField by fxid()
    val usernameField : TextField by fxid()
    val passwordField : TextField by fxid()
    val confirmPasswordField : TextField by fxid()
    private val createAccountButton:  Button by fxid()

    private val service = Service()

    init {
        createAccountButton.apply{ action { handleCreateAccount() }}
    }

    private fun handleCreateAccount(){
        if(!passwordField.text.equals(confirmPasswordField.text)){
            alert(Alert.AlertType.ERROR, "Passwords don't match")
            return
        }
        try {
            service.addUser(usernameField.text, passwordField.text, emailField.text)
            alert(Alert.AlertType.INFORMATION, "Account created successfully")
            this.close()
        }catch (exception: ConferenceException){
            alert(Alert.AlertType.ERROR, exception.message)
        }
    }
}
