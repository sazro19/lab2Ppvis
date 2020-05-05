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
    private String selectedItem;
    private ComboBox criteriaComBox;
    private Button searchButton;
    private TableElement tableElement;
    private GridPane grid;
    private Pane criteriaChooser;
    private Pane root;
    private List<Label> criteria1LabelList;
    private List<Label> criteria2LabelList;
    private Label criteria3Label;
    private List<TextField> criteria1FieldList;
    private TextField criteria2TextField;
    private TextField criteria3TextField;
    private DatePicker criteria2DatePicker;
    private DatePicker criteria1DatePicker;

    public enum criteriaForRequesting {
        CRITERIA_1("Дата последнего приема и ФИО ветеринара"),
        CRITERIA_2("Имя питомца и дата рождения"),
        CRITERIA_3("По фразе из диагноза");
        private final String value;

        criteriaForRequesting(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static criteriaForRequesting getCriteriaByName(String value) {
            criteriaForRequesting res = null;
            for (criteriaForRequesting x : values()) {
                if (x.getValue().equals(value)) {
                    res = x;
                }
            }
            return res;
        }
    }

    public RequestElement(View.WindowType windowType, Controller controller){
        criteriaForRequesting criteria1 = criteriaForRequesting.CRITERIA_1;
        criteriaForRequesting criteria2 = criteriaForRequesting.CRITERIA_2;
        criteriaForRequesting criteria3 = criteriaForRequesting.CRITERIA_3;
        criteriaComBox = new ComboBox();
        criteriaComBox.getItems().addAll(
                criteria1.getValue(),
                criteria2.getValue(),
                criteria3.getValue()
        );
        criteriaComBox.setValue(criteria1.getValue());
        searchButton = new Button("Найти");
        criteriaChooser = new HBox();

        criteria1LabelList = new ArrayList<>();
        criteria1FieldList = new ArrayList<>();
        criteria1DatePicker = new DatePicker();
        criteria2LabelList = new ArrayList<>();
        criteria2TextField = new TextField();
        criteria3TextField = new TextField();
        criteria2DatePicker = new DatePicker();
        criteria3Label = new Label("Фраза из диагноза");
        initCriteriaLists();
        grid = new GridPane();
        switchPreset();
        tableElement = new TableElement(new ArrayList<>(), controller);
        this.root = new VBox();
        if(windowType == View.WindowType.SEARCH){
            criteriaChooser.getChildren().addAll(
                    new Label("Критерий поиска:"),
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
        final int CRITERIA_1_FIELD_NUMBER = 4;
        final int CRITERIA_2_FIELD_NUMBER = 2;


        grid.getChildren().clear();
        selectedItem = criteriaComBox.getSelectionModel().getSelectedItem().toString();
        criteriaForRequesting criteria = criteriaForRequesting.getCriteriaByName(selectedItem);
        switch (criteria){
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
        final String SURNAME_LABEL_TEXT = "Фамилия ветеринара: ";
        final String NAME_LABEL_TEXT = "Имя ветеринара:";
        final String PATRONYM_LABEL_TEXT = "Прозвище ветеринара:";
        final String DATE_BIRTH_LABEL_TEXT = "Дата рождения:";
        final String PET_NAME_LABEL_TEXT = "Имя питомца:";
        final String DATE_OF_LAST_APPOINTMENT_LABEL_TEXT = "Дата последнего приема:";

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
        criteriaListText = new ArrayList<>();
        criteriaListDate = new ArrayList<>();
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
