package gui.views.user

import domain.User
import gui.views.login.LoginView
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuBar
import javafx.scene.layout.GridPane
import service.Service
import tornadofx.*

class UserView(user: User, service: Service): View(user.name) {
    override val root : GridPane by fxml()
    private val logoutButton: Button by fxid()
    private val menuBar: MenuBar by fxid()

    init {
        logoutButton.apply { action { handleLogout() }}
        if(user.name == "admin"){
            root.add(Label("hi admin"), 2, 2)
        }
    }



    private fun handleLogout(){
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }
}