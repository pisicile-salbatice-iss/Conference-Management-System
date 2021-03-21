package gui

import tornadofx.*

class MainView : View("CMS") {
    override val root = vbox {
        setPrefSize(600.0, 400.0)
        label(title)
    }
}

class GUI : App(MainView::class) {
    fun run(args: Array<String>) {
        launch<GUI>(args)
    }
}
