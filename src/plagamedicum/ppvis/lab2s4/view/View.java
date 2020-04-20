package plagamedicum.ppvis.lab2s4.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import plagamedicum.ppvis.lab2s4.Controller.Controller;
import plagamedicum.ppvis.lab2s4.model.Pet;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class View {
    private Scene        scene;
	private TableElement tableElement;
    private Controller   controller;
    private Stage        stage;
    private VBox         root;
    private enum WindowType {
        DELETE, SEARCH
    }

	public View(Controller controller) {
        final int    STAGE_WIDTH  = 1000,
                     STAGE_HEIGHT = 650;
        final String STAGE_TITLE_TEXT = "Lab2";

        this.controller = controller;
        initWindow();
        stage = new Stage();
        stage.setWidth (STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.setTitle (STAGE_TITLE_TEXT);
        stage.setScene(scene);
	}

	private void initWindow(){
        final String FILE_MENU_LABEL_TEXT              = "Файл",
                     EDIT_MENU_LABEL_TEXT              = "Редактировать",
                     OPEN_DOC_MENU_ITEM_LABEL_TEXT     = "Открыть документ",
                     SAVE_DOC_MENU_ITEM_LABEL_TEXT     = "Сохранить документ",
                     ADD_ITEM_MENU_ITEM_LABEL_TEXT     = "Добавить строчки",
                     SEARCH_ITEMS_MENU_ITEM_LABEL_TEXT = "Искать строчки",
                     DELETE_ITEMS_MENU_ITEM_LABEL_TEXT = "Удалить строчки",
                     CLOSE_APP_MENU_ITEM_LABEL_TEXT    = "Выход",
                     OPEN_DOC_BUTTON_LABEL_TEXT        = "Открыть документ",
                     SAVE_DOC_BUTTON_LABEL_TEXT        = "Сохранить документ",
                     ADD_ITEMS_BUTTON_LABEL_TEXT       = "Добавить строчки",
                     SEARCH_ITEMS_BUTTON_LABEL_TEXT    = "Искать строчки",
                     DELETE_ITEMS_BUTTON_LABEL_TEXT    = "Удалить строчки";
        MenuItem openDocMenuItem     = new MenuItem(OPEN_DOC_MENU_ITEM_LABEL_TEXT),
                 saveMenuItem        = new MenuItem(SAVE_DOC_MENU_ITEM_LABEL_TEXT),
                 addItemsMenuItem    = new MenuItem(ADD_ITEM_MENU_ITEM_LABEL_TEXT),
                 searchItemsMenuItem = new MenuItem(SEARCH_ITEMS_MENU_ITEM_LABEL_TEXT),
                 deleteItemsMenuItem = new MenuItem(DELETE_ITEMS_MENU_ITEM_LABEL_TEXT),
                 closeAppMenuItem    = new MenuItem(CLOSE_APP_MENU_ITEM_LABEL_TEXT);
        Menu     fileMenu            = new Menu(FILE_MENU_LABEL_TEXT),
                 editMenu            = new Menu(EDIT_MENU_LABEL_TEXT);
        MenuBar  menuBar             = new MenuBar();
        Button   openDocButton       = new Button(OPEN_DOC_BUTTON_LABEL_TEXT),
                 saveDocButton       = new Button(SAVE_DOC_BUTTON_LABEL_TEXT),
                 addItemsButton      = new Button(ADD_ITEMS_BUTTON_LABEL_TEXT),
                 searchItemsButton   = new Button(SEARCH_ITEMS_BUTTON_LABEL_TEXT),
                 deleteItemsButton   = new Button(DELETE_ITEMS_BUTTON_LABEL_TEXT);
        ToolBar  instruments;

        fileMenu.getItems().addAll(
             //   newDocMenuItem,
                openDocMenuItem,
                saveMenuItem,
                new SeparatorMenuItem(),
                closeAppMenuItem);
        editMenu.getItems().addAll(
                addItemsMenuItem,
                new SeparatorMenuItem(),
                searchItemsMenuItem,
                deleteItemsMenuItem);
        menuBar.getMenus().addAll(
                fileMenu,
                editMenu);

        instruments = new ToolBar(
                openDocButton,
                saveDocButton,
                new Separator(),
                addItemsButton,
                searchItemsButton,
                deleteItemsButton);

        tableElement = new TableElement(controller.getPetList());

        root = new VBox();
        root.getChildren().addAll(
                menuBar,
                instruments,
                tableElement.get());
        scene = new Scene(root);

        openDocButton.setOnAction(ae -> openDoc());
            openDocMenuItem.setOnAction(ae -> openDoc());
        saveDocButton.setOnAction(ae -> saveDoc());
            saveMenuItem.setOnAction(ae -> saveDoc());
        addItemsButton.setOnAction(ae -> addItems());
            addItemsMenuItem.setOnAction(ae -> addItems());
        searchItemsButton.setOnAction(ae -> searchItems());
            searchItemsMenuItem.setOnAction(ae -> searchItems());
        deleteItemsButton.setOnAction(ae -> deleteItems());
            deleteItemsMenuItem.setOnAction(ae -> deleteItems());
        closeAppMenuItem.setOnAction(ae -> Platform.exit());
    }

	public Stage getStage(){
	    return stage;
    }

    private void openDoc(){
        FileChooser openDocChooser = new FileChooser();

        openDocChooser.setTitle("Открыть документ");
        openDocChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                new FileChooser.ExtensionFilter("XML-документ", "*.xml")
        );

        try {
            controller.openDoc(openDocChooser.showOpenDialog(stage));
        } catch (Exception exception){
            exception.printStackTrace();
        }

        tableElement.rewriteDefaultList(controller.getPetList());
        tableElement.resetToDefaultItems();
    }

    private void saveDoc(){
        FileChooser saveDocChooser = new FileChooser();

        saveDocChooser.setTitle("Сохранить документ");
        saveDocChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Все файлы", "*.*"),
                new FileChooser.ExtensionFilter("XML-документ", "*.xml")
        );

        controller.saveDoc(saveDocChooser.showSaveDialog(stage));
    }

	private void addItems(){
        final String WINDOW_TITLE_TEXT      = "Добавить строчки: ",
                     PET_NAME_LABEL_TEXT     = "Имя питомца",
                     PET_BIRTHDAY_LABEL_TEXT = "Дата рождения",
                     PET_LAST_APPOINTMENT_LABEL_TEXT = "Дата последнего посещения",
                     SURNAME_LABEL_TEXT     = "Фамилия ветеринара: ",
                     NAME_LABEL_TEXT        = "Имя ветеринара: ",
                     PATRONYM_LABEL_TEXT    = "Отчество ветеринара: ",
                     GROUP_LABEL_TEXT       = "Диагноз: ";

        TextField    petNameField   = new TextField(),
                     surnameField   = new TextField(),
                     nameField      = new TextField(),
                     patronymField  = new TextField(),
                     diagnosisField = new TextField();
        DatePicker   birthdayField  = new DatePicker(),
                     lastAppointmentField = new DatePicker();

	    GridPane     root           = new GridPane();
        Alert        addItemWindow;

        root.addRow(0,
                new Label(PET_NAME_LABEL_TEXT),
                petNameField
        );
        root.addRow(1,
                new Label(PET_BIRTHDAY_LABEL_TEXT),
                birthdayField
        );
        root.addRow(2,
                new Label(PET_LAST_APPOINTMENT_LABEL_TEXT),
                lastAppointmentField
        );
        root.addRow(3,
                new Label(SURNAME_LABEL_TEXT),
                surnameField
        );
        root.addRow(4,
                new Label(NAME_LABEL_TEXT),
                nameField
        );
        root.addRow(5,
                new Label(PATRONYM_LABEL_TEXT),
                patronymField
        );
        root.addRow(6,
                new Label(GROUP_LABEL_TEXT),
                diagnosisField
        );

        addItemWindow = createEmptyCloseableDialog();
        addItemWindow.setTitle(WINDOW_TITLE_TEXT);
        addItemWindow.getDialogPane().setContent(root);
        addItemWindow.show();

        ((Button)addItemWindow.getDialogPane().lookupButton(addItemWindow.getButtonTypes().get(0))).setOnAction(ae->{
            controller.addPet(
                    petNameField.getText(),
                    birthdayField.getValue(),
                    lastAppointmentField.getValue(),
                    surnameField.getText(),
                    nameField.getText(),
                    patronymField.getText(),
                    diagnosisField.getText()
            );
            tableElement.resetToDefaultItems();
            addItemWindow.close();
        });
    }

    private class RequestElement{
        final String CRITERIA_1 = "Дата последнего приема и ФИО ветеринара",
                     CRITERIA_2 = "Имя питомца и дата рождения",
                     CRITERIA_3 = "По фразе из диагноза";
        private String       selectedItem;
        private ComboBox     criteriaComBox;
        private Button       searchButton;
        private TableElement tableElement;
        private GridPane     grid;
        private Pane         criteriaChooser,
                             root;
        private List<Label>     criteria1LabelList,
                                criteria2LabelList;
        private Label           criteria3Label;
        private List<TextField> criteria1FieldList;
        private TextField       criteria2TextField,
                                criteria3TextField;
        private DatePicker      criteria2DatePicker,
                                criteria1DatePicker;

        public RequestElement(WindowType windowType){
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
            tableElement = new TableElement(new ArrayList<>());
            this.root = new VBox();
            if(windowType == WindowType.SEARCH){
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

            if(windowType == WindowType.DELETE){
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
                List<Pet> petList = search();

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

        public List search(){
            List criteriaListText, criteriaListDate;
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

    private void searchItems(){
        final String WINDOW_TITLE_TEXT = "Искать строчки";
        Alert        searchItemsWindow;
        RequestElement requestElement = new RequestElement(WindowType.SEARCH);

        searchItemsWindow = createEmptyCloseableDialog();
        searchItemsWindow.setTitle(WINDOW_TITLE_TEXT);
        searchItemsWindow.getDialogPane().setContent(requestElement.get());
        searchItemsWindow.show();

        ((Button)searchItemsWindow.getDialogPane().lookupButton(searchItemsWindow.getButtonTypes().get(0))).setOnAction(
                ae-> searchItemsWindow.close()
        );
    }

    private void deleteItems(){
        final String WINDOW_TITLE_TEXT = "Удалить строчки";
        Alert        deleteItemsWindow;
        RequestElement requestElement = new RequestElement(WindowType.DELETE);

        deleteItemsWindow = createEmptyCloseableDialog();
        deleteItemsWindow.setTitle(WINDOW_TITLE_TEXT);
        deleteItemsWindow.getDialogPane().setContent(requestElement.get());
        deleteItemsWindow.show();

        ((Button)deleteItemsWindow.getDialogPane().lookupButton(deleteItemsWindow.getButtonTypes().get(0))).setOnAction(ae->{
            createDeleteInfoWindow(String.valueOf(requestElement.search().size()));
            controller.delete(requestElement.search());
            tableElement.resetToDefaultItems();
            deleteItemsWindow.close();
        });
    }

    private void createDeleteInfoWindow(String deleteInfo){
        final String CLOSE_BUTTON_LABEL_TEXT = "ОК";
        ButtonType   closeButton       = new ButtonType(CLOSE_BUTTON_LABEL_TEXT);
	    Alert window  = new Alert(Alert.AlertType.NONE);
	    VBox  vertice = new VBox();

	    vertice.getChildren().add(new Label("Удалено " + deleteInfo + " строчек."));
	    window.getDialogPane().setContent(vertice);
        window.getButtonTypes().addAll(closeButton);
        window.show();
    }

    private Alert createEmptyCloseableDialog(){
        final String CLOSE_BUTTON_LABEL_TEXT = "Далее";
        ButtonType   closeButton       = new ButtonType(CLOSE_BUTTON_LABEL_TEXT);
        Alert        window            = new Alert(Alert.AlertType.NONE);

        window.getButtonTypes().addAll(closeButton);
        return window;
    }
}
