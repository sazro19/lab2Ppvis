package plagamedicum.ppvis.lab2s4.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import plagamedicum.ppvis.lab2s4.model.Pet;

public class TableElement {
    private int                rowsOnPage,
                               currentPage = 1,
                               numberOfPages;
    private Label              paginationLabel,
                               itemsCountLabel;
    private Button             resetCountPages;
    private TextField          rowsOnPageField;
    private TableView<Pet>     table;
    private ToolBar            navigator,
                               pagination;
    private Pane               tableElement;
    private List<Pet> defaultPetList;
    private ObservableList<Pet> petObsList,
                                    curPetObsList;

    public TableElement(List<Pet> petList){
        final int    TABLE_HEIGHT                 = 450,
                     TABLE_WIDTH                  = 1000,
                     DEFAULT_ROWS_ON_PAGE_NUMBER  = 17;
        final String PET_NAME_LABEL_TEXT           = "Имя питомца",
                     PET_BIRTHDAY_LABEL_TEXT       = "Дата рождения",
                     PET_LAST_APPOINTMENT_LABEL_TEXT = "Дата последнего приёма",
                     SNP_COLUMN_LABEL_TEXT         = "ФИО ветеринара",
                     DIAGNOSIS_COLUMN_LABEL_TEXT      = "Диагноз",
                     ROWS_ON_PAGE_LABEL_TEXT      = "Строчек на странице ",
                     TO_BEGIN_BUTTON_LABEL_TEXT   = "<<",
                     TO_LEFT_BUTTON_LABEL_TEXT    = "<",
                     TO_RIGHT_BUTTON_LABEL_TEXT   = ">",
                     TO_END_BUTTON_LABEL_TEXT     = ">>";
        Button    toBeginButton   = new Button(TO_BEGIN_BUTTON_LABEL_TEXT),
                  toLeftButton    = new Button(TO_LEFT_BUTTON_LABEL_TEXT),
                  toRightButton   = new Button(TO_RIGHT_BUTTON_LABEL_TEXT),
                  toEndButton     = new Button(TO_END_BUTTON_LABEL_TEXT);
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
        setRowsOnPage();

        tableElement = new VBox();
        tableElement.getChildren().addAll(table,
                                          pagination);

        resetCountPages.setOnAction(ae -> setRowsOnPage());
        toBeginButton.setOnAction(ae -> goBegin());
        toLeftButton.setOnAction(ae -> goLeft());
        toRightButton.setOnAction(ae -> goRight());
        toEndButton.setOnAction(ae -> goEnd());
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
        setRowsOnPage();
    }

    private void setRowsOnPage(){
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
    }
}
