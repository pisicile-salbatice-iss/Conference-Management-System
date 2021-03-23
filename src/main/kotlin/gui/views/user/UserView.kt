package gui.views.user

import domain.User
import gui.views.login.LoginView
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuBar
import javafx.scene.layout.GridPane
import service.Service
import javafx.geometry.Pos;
import tornadofx.*

class UserView(user: User, service: Service): View(user.name) {
    override val root : GridPane by fxml()
    private val logoutButton: Button by fxid()
    private val menuBar: MenuBar by fxid()

    private val adminView = AdminView(service)

    init {
        logoutButton.apply { action { handleLogout() }}
        if(user.name == "admin"){
            root.children.removeAt(4);
            root.add(adminView.root, 1, 2)
        }
    }

    private fun handleLogout(){
        replaceWith(LoginView::class, ViewTransition.Slide(0.3.seconds, ViewTransition.Direction.RIGHT))
    }
}