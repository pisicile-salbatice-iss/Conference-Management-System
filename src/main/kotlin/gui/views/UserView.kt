package gui.views

import domain.User
import gui.views.login.LoginView
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class UserView(user: User, service: Service): View(user.name) {
    override val root : GridPane by fxml()
    private val logoutButton: Button by fxid()
    private val loginView: LoginView by inject()

    init {
        logoutButton.apply { action { handleLogout() }}
    }

    private fun handleLogout(){
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.LEFT))
    }
}