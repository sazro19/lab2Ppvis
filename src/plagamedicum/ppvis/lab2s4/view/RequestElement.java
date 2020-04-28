package plagamedicum.ppvis.lab2s4.view;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import plagamedicum.ppvis.lab2s4.model.Pet;

import plagamedicum.ppvis.lab2s4.Controller.Controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RequestElement {
    final String CRITERIA_1 = "Дата последнего приема и ФИО ветеринара",
            CRITERIA_2 = "Имя питомца и дата рождения",
            CRITERIA_3 = "По фразе из диагноза";
    private String       selectedItem;
    private ComboBox criteriaComBox;
    private Button searchButton;
    private TableElement tableElement;
    private GridPane grid;
    private Pane criteriaChooser,
            root;
    private List<Label> criteria1LabelList,
            criteria2LabelList;
    private Label           criteria3Label;
    private List<TextField> criteria1FieldList;
    private TextField       criteria2TextField,
            criteria3TextField;
    private DatePicker criteria2DatePicker,
            criteria1DatePicker;

    public RequestElement(View.WindowType windowType, Controller controller){
        criteriaComBox = new ComboBox();
        criteriaComBox.getItems().addAll(
                CRITERIA_1,
                CRITERIA_2,
                CRITERIA_3
        );
        criteriaComBox.setValue(CRITERIA_1);
        searchButton    = new Button("Найти");
        criteriaChooser = new HBox();

        criteria1LabelList = new ArrayList<>();
        criteria1FieldList = new ArrayList<>();
        criteria1DatePicker = new DatePicker();
        criteria2LabelList = new ArrayList<>();
        criteria2TextField = new TextField();
        criteria3TextField = new TextField();
        criteria2DatePicker = new DatePicker();
        criteria3Label     = new Label("Фраза из диагноза");
        initCriteriaLists();
        grid              = new GridPane();
        switchPreset();
        tableElement = new TableElement(new ArrayList<>(), controller);
        this.root = new VBox();
        if(windowType == View.WindowType.SEARCH){
            criteriaChooser.getChildren().addAll(
                    new Label("Критерий поиска: "),
                    criteriaComBox,
                    searchButton
            );

            this.root.getChildren().addAll(
                    new Separator(),
                    new Separator(),
                    criteriaChooser,
                    grid,
                    new Separator(),
                    new Separator(),
                    tableElement.get(),
                    new Separator(),
                    new Separator(),
                    new Separator()
            );
        }

        if(windowType == View.WindowType.DELETE){
            criteriaChooser.getChildren().addAll(
                    new Label("Критерий поиска: "),
                    criteriaComBox
            );

            this.root.getChildren().addAll(
                    new Separator(),
                    new Separator(),
                    criteriaChooser,
                    grid
            );
        }


        criteriaComBox.setOnAction(ae -> switchPreset());
        searchButton.setOnAction(ae->{
            List<Pet> petList = search(controller);

            tableElement.setObservableList(petList);
        });
    }

    private void switchPreset(){
        final int CRITERIA_1_FIELD_NUMBER = 4,
                CRITERIA_2_FIELD_NUMBER = 2;


        grid.getChildren().clear();
        selectedItem = criteriaComBox.getSelectionModel().getSelectedItem().toString();
        switch (selectedItem){
            case CRITERIA_1:
                for(int i = 0; i < CRITERIA_1_FIELD_NUMBER; i++){
                    if(i == 0) {
                        grid.addRow(i,
                                criteria1LabelList.get(i),
                                criteria1DatePicker
                        );
                    }else {
                        grid.addRow(i,
                                criteria1LabelList.get(i),
                                criteria1FieldList.get(i-1)
                        );
                    }
                }
                break;
            case CRITERIA_2:
                for(int i = 0; i < CRITERIA_2_FIELD_NUMBER; i++){
                    if(i == 0) {
                        grid.addRow(i,
                                criteria2LabelList.get(i),
                                criteria2TextField
                        );
                    }else { grid.addRow(i,
                            criteria2LabelList.get(i),
                            criteria2DatePicker
                    );
                    }
                }
                break;
            case CRITERIA_3:
                grid.addRow(0,
                        criteria3Label,
                        criteria3TextField
                );
        }
    }

    private void initCriteriaLists(){
        final String SURNAME_LABEL_TEXT       = "Фамилия ветеринара: ",
                NAME_LABEL_TEXT          = "Имя ветеринара:",
                PATRONYM_LABEL_TEXT      = "Прозвище ветеринара",
                DATE_BIRTH_LABEL_TEXT    = "Дата рождения:",
                PET_NAME_LABEL_TEXT      = "Имя питомца: ",
                DATE_OF_LAST_APPOINTMENT_LABEL_TEXT = "Дата последнего приема: ";

        criteria1LabelList.add(new Label(DATE_OF_LAST_APPOINTMENT_LABEL_TEXT));
        criteria1LabelList.add(new Label(SURNAME_LABEL_TEXT));
        criteria1LabelList.add(new Label(NAME_LABEL_TEXT));
        criteria1LabelList.add(new Label(PATRONYM_LABEL_TEXT));
        criteria1FieldList.add(new TextField());
        criteria1FieldList.add(new TextField());
        criteria1FieldList.add(new TextField());
        criteria2LabelList.add(new Label(PET_NAME_LABEL_TEXT));
        criteria2LabelList.add(new Label(DATE_BIRTH_LABEL_TEXT));
    }

    public Pane get(){
        return this.root;
    }

    public List<Pet> search(Controller controller){
        List<String> criteriaListText;
        List<LocalDate> criteriaListDate;
        criteriaListText = new ArrayList<String>();
        criteriaListDate = new ArrayList<LocalDate>();
        criteriaListDate.add(criteria1DatePicker.getValue());
        criteriaListText.add(criteria1FieldList.get(0).getText());
        criteriaListText.add(criteria1FieldList.get(1).getText());
        criteriaListText.add(criteria1FieldList.get(2).getText());
        criteriaListText.add(criteria2TextField.getText());
        criteriaListDate.add(criteria2DatePicker.getValue());
        criteriaListText.add(criteria3TextField.getText());
        return controller.search(selectedItem, criteriaListText, criteriaListDate);
    }
}
