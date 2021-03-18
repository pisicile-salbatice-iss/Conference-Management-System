import tornadofx.*

class MainView : View("balarii") {
    override val root = vbox {
        label(title)
    }
}

class Application : App(MainView::class)

fun main(args: Array<String>) {
    launch<Application>(args)
}
