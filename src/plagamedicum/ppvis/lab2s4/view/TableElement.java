package plagamedicum.ppvis.lab2s4.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import plagamedicum.ppvis.lab2s4.Controller.Controller;
import plagamedicum.ppvis.lab2s4.model.Pet;

public class TableElement {
    private Label              paginationLabel;
    private Label              itemsCountLabel;
    private Button             resetCountPages;
    private TextField          rowsOnPageField;
    private TableView<Pet>     table;
    private ToolBar            navigator,
                               pagination;
    private Pane               tableElement;
    private List<Pet> defaultPetList;
    private ObservableList<Pet> petObsList;
    private ObservableList<Pet> curPetObsList;
    private Controller controller;


    public TableElement(List<Pet> petList, Controller controller){
        final int TABLE_HEIGHT = 450;
        final int TABLE_WIDTH = 1000;
        final int DEFAULT_ROWS_ON_PAGE_NUMBER = 17;
        final String PET_NAME_LABEL_TEXT = "Имя питомца";
        final String PET_BIRTHDAY_LABEL_TEXT = "Дата рождения";
        final String PET_LAST_APPOINTMENT_LABEL_TEXT = "Дата последнего приёма";
        final String SNP_COLUMN_LABEL_TEXT = "ФИО ветеринара";
        final String DIAGNOSIS_COLUMN_LABEL_TEXT = "Диагноз";
        final String ROWS_ON_PAGE_LABEL_TEXT = "Строчек на странице ";
        final String TO_BEGIN_BUTTON_LABEL_TEXT = "<<";
        final String TO_LEFT_BUTTON_LABEL_TEXT = "<";
        final String TO_RIGHT_BUTTON_LABEL_TEXT = ">";
        final String TO_END_BUTTON_LABEL_TEXT = ">>";
        this.controller = controller;
        Button toBeginButton = new Button(TO_BEGIN_BUTTON_LABEL_TEXT);
        Button toLeftButton = new Button(TO_LEFT_BUTTON_LABEL_TEXT);
        Button toRightButton = new Button(TO_RIGHT_BUTTON_LABEL_TEXT);
        Button toEndButton = new Button(TO_END_BUTTON_LABEL_TEXT);
        TableColumn<Pet, String> petNameCol = new TableColumn<>(PET_NAME_LABEL_TEXT);
        TableColumn<Pet, LocalDate> petBirthdayCol = new TableColumn<>(PET_BIRTHDAY_LABEL_TEXT),
                                    petLastAppointmentCol = new TableColumn<>(PET_LAST_APPOINTMENT_LABEL_TEXT);
        TableColumn<Pet, String> snpCol   = new TableColumn<>(SNP_COLUMN_LABEL_TEXT),
                                 diagnosisCol = new TableColumn<>(DIAGNOSIS_COLUMN_LABEL_TEXT);

        defaultPetList = petList;
        petObsList = FXCollections.observableArrayList(defaultPetList);
        curPetObsList = FXCollections.observableArrayList();

        petNameCol.setCellValueFactory(new PropertyValueFactory<>("petName"));
        petNameCol.setMinWidth(100);
        petBirthdayCol.setCellValueFactory(new PropertyValueFactory<>("petBirthday"));
        petBirthdayCol.setMinWidth(200);
        petLastAppointmentCol.setCellValueFactory(new PropertyValueFactory<>("petLastAppointment"));
        petLastAppointmentCol.setMinWidth(200);
        snpCol.setCellValueFactory(new PropertyValueFactory<>("alignSnp"));
        snpCol.setMinWidth(300);
        diagnosisCol.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        diagnosisCol.setMinWidth(200);

        paginationLabel = new Label();
        navigator = new ToolBar(
                toBeginButton,
                toLeftButton,
                paginationLabel,
                toRightButton,
                toEndButton
        );

        itemsCountLabel = new Label("/" + petObsList.size() + "/");
        rowsOnPageField = new TextField();
        rowsOnPageField.setText(String.valueOf(DEFAULT_ROWS_ON_PAGE_NUMBER));
        resetCountPages   = new Button("Обновить количество строк на странице");
        pagination = new ToolBar(
                itemsCountLabel,
                new Separator(),
                new Label(ROWS_ON_PAGE_LABEL_TEXT),
                rowsOnPageField,
                resetCountPages,
                new Separator(),
                navigator
        );

        table = new TableView<>();
        table.setMinHeight(TABLE_HEIGHT);
        table.setMaxWidth(TABLE_WIDTH);
        table.getColumns().addAll(
                petNameCol,
                petBirthdayCol,
                petLastAppointmentCol,
                snpCol,
                diagnosisCol
        );
        table.setItems(curPetObsList);
        controller.setRowsOnPage(rowsOnPageField.getText(), petObsList, curPetObsList);
        tableElement = new VBox();
        tableElement.getChildren().addAll(table,
                                          pagination);

        resetCountPages.setOnAction(ae -> {controller.setRowsOnPage(rowsOnPageField.getText(), petObsList, curPetObsList);
                                           paginationLabel.setText(controller.getPagination());
                                           itemsCountLabel.setText(controller.getItemsCount());
        });
        toBeginButton.setOnAction(ae -> {controller.goBegin(petObsList, curPetObsList);
                                         paginationLabel.setText(controller.getPagination());
                                         itemsCountLabel.setText(controller.getItemsCount());
        });
        toLeftButton.setOnAction(ae -> {controller.goLeft(petObsList, curPetObsList);
                                        paginationLabel.setText(controller.getPagination());
                                        itemsCountLabel.setText(controller.getItemsCount());
        });
        toRightButton.setOnAction(ae -> {controller.goRight(petObsList, curPetObsList);
                                         paginationLabel.setText(controller.getPagination());
                                         itemsCountLabel.setText(controller.getItemsCount());
        });
        toEndButton.setOnAction(ae -> {controller.goEnd(petObsList, curPetObsList);
                                       paginationLabel.setText(controller.getPagination());
                                       itemsCountLabel.setText(controller.getItemsCount());
        });
    }

    public Pane get(){
        return tableElement;
    }

    public void rewriteDefaultList(List<Pet> list){
        defaultPetList = list;
    }

    public void resetToDefaultItems(){
        setObservableList(defaultPetList);
    }

    public void setObservableList(List<Pet> list){
        petObsList = FXCollections.observableArrayList(list);
        controller.setRowsOnPage(rowsOnPageField.getText(), petObsList, curPetObsList);
    }

    /*private void setRowsOnPage(){
        rowsOnPage = Integer.parseInt(rowsOnPageField.getText());
        currentPage = 1;
        refreshPage();
    }

    private void goBegin(){
        currentPage = 1;
        refreshPage();
    }

    private void goLeft(){
        if(currentPage > 1){
            currentPage--;
        }
        refreshPage();
    }

    private void goRight(){
        if(currentPage < numberOfPages){
            currentPage++;
        }
        refreshPage();
    }

    private void goEnd(){
        currentPage = numberOfPages;
        refreshPage();
    }

    private void refreshPage(){
        int fromIndex = (currentPage - 1) * rowsOnPage,
            toIndex   =  currentPage      * rowsOnPage;

        if(toIndex > petObsList.size()){
            toIndex = petObsList.size();
        }

        curPetObsList.clear();
        curPetObsList.addAll(
                petObsList.subList(
                        fromIndex,
                        toIndex
                )
        );

        refreshPaginationLabel();
    }

    private void refreshPaginationLabel(){
        numberOfPages = (petObsList.size() - 1) / rowsOnPage + 1;
        paginationLabel.setText(currentPage + "/" + numberOfPages);
        itemsCountLabel.setText("/" + petObsList.size() + "/");
    }*/
}
