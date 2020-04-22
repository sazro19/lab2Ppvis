package plagamedicum.ppvis.lab2s4.view;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import plagamedicum.ppvis.lab2s4.Controller.Controller;

public class View {
    private Scene        scene;
	private TableElement tableElement;
    private Controller   controller;
    private Stage        stage;
    private VBox         root;
    private RequestElement requestElement;
    public enum WindowType {
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

    private void searchItems(){
        final String WINDOW_TITLE_TEXT = "Искать строчки";
        Alert        searchItemsWindow;
        requestElement = new RequestElement(WindowType.SEARCH, controller);

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
        RequestElement requestElement = new RequestElement(WindowType.DELETE, controller);

        deleteItemsWindow = createEmptyCloseableDialog();
        deleteItemsWindow.setTitle(WINDOW_TITLE_TEXT);
        deleteItemsWindow.getDialogPane().setContent(requestElement.get());
        deleteItemsWindow.show();

        ((Button)deleteItemsWindow.getDialogPane().lookupButton(deleteItemsWindow.getButtonTypes().get(0))).setOnAction(ae->{
            createDeleteInfoWindow(String.valueOf(requestElement.search(controller).size()));
            controller.delete(requestElement.search(controller));
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
