package gui.views.user

import domain.Conference
import exceptions.ConferenceException
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.layout.GridPane

import service.Service
import tornadofx.*
import java.sql.Date


class AdminView(private val service: Service): View() {

    override val root : GridPane by fxml()
    private val nameField : TextField by fxid()
    private val priceField : TextField by fxid()
    private val dateField : DatePicker by fxid()
    private val addConferenceButton:  Button by fxid()
    init {
        addConferenceButton.apply{ action { handleCreateConference() }}
    }

    private fun handleCreateConference(){
        if(priceField.text.toInt() < 0){
            alert(Alert.AlertType.ERROR, "The price for the conference must be greater than 0")
            return
        }
        if(nameField.text.isBlank()){
            alert(Alert.AlertType.ERROR, "The conference must have a name")
            return
        }
        try {
            service.addConference(nameField.text,Date.valueOf(dateField.value),priceField.text.toInt())
            alert(Alert.AlertType.INFORMATION, "Conference added successfully")

        }catch (exception: ConferenceException){
            alert(Alert.AlertType.ERROR, exception.message)
        }
    }


}