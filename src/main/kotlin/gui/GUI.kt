package gui

import gui.views.LoginView
import tornadofx.*


class GUI : App(LoginView::class) {
    fun run(args: Array<String>) {
        launch<GUI>(args)
    }
}
