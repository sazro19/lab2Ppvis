package plagamedicum.ppvis.lab2s4.Controller;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.*;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import plagamedicum.ppvis.lab2s4.model.Model;
import plagamedicum.ppvis.lab2s4.model.SNP;
import plagamedicum.ppvis.lab2s4.model.Pet;

public class Controller {
    private Model model;

    private DocOpener docOpener;

    public Controller(Model model){
        this.model = model;
    }

    public List<Pet> getPetList(){
        return model.getPetList();
    }

    public void addPet(String petName, LocalDate petBirthday, LocalDate petLast, String surname, String name, String patronym, String diagnosis){
        model.addPet(
                new Pet(petName, petBirthday, petLast, new SNP(surname, name, patronym), diagnosis)
        );
    }

    public void openDoc(File file) {
        docOpener = new DocOpener();
        try {
            model.setPetList(docOpener.openDoc(file));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void saveDoc(File file) {
        List<Pet>              petList = model.getPetList();
        Element                pets,
                               pet,
                               petName,
                               petBirthday,
                               petLastAppointment,
                               snp,
                               diagnosis;
        Attr                   petNameName,
                               petBirthdayName,
                               petLastAppointmentName,
                               surname,
                               name,
                               patronym,
                               diagnosisName;
        Document               doc;
        DocumentBuilderFactory docBuilderFactory;
        DocumentBuilder        docBuilder;
        TransformerFactory     transformerFactory;
        Transformer            transformer;
        DOMSource              source;
        StreamResult           streamResult;

        try{
            docBuilderFactory = DocumentBuilderFactory.newInstance();
            docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.newDocument();

            pets = doc.createElement("pets");
            doc.appendChild(pets);

            for (Pet petI : petList){
                petName = doc.createElement("petName");
                petNameName = doc.createAttribute("name");
                petNameName.setValue(petI.getPetName());
                petName.setAttributeNode(petNameName);

                petBirthday = doc.createElement("petBirthday");
                petBirthdayName = doc.createAttribute("name");
                petBirthdayName.setValue(petI.getPetBirthdayString());
                petBirthday.setAttributeNode(petBirthdayName);

                petLastAppointment = doc.createElement("petLastAppointment");
                petLastAppointmentName = doc.createAttribute("name");
                petLastAppointmentName.setValue(petI.getPetLastAppointmentString());
                petLastAppointment.setAttributeNode(petLastAppointmentName);

                surname = doc.createAttribute("surname");
                surname.setValue(petI.getSnp().getSurname());
                name = doc.createAttribute("name");
                name.setValue(petI.getSnp().getName());
                patronym = doc.createAttribute("patronym");
                patronym.setValue(petI.getSnp().getPatronym());
                snp = doc.createElement("snp");
                snp.setAttributeNode(surname);
                snp.setAttributeNode(name);
                snp.setAttributeNode(patronym);

                diagnosis = doc.createElement("diagnosis");
                diagnosisName = doc.createAttribute("name");
                diagnosisName.setValue(petI.getDiagnosis());
                diagnosis.setAttributeNode(diagnosisName);

                pet = doc.createElement("pet");
                pet.appendChild(petName);
                pet.appendChild(petBirthday);
                pet.appendChild(petLastAppointment);
                pet.appendChild(snp);
                pet.appendChild(diagnosis);
                pets.appendChild(pet);
            }

            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            source = new DOMSource(doc);
            streamResult = new StreamResult(file);
            transformer.transform(source, streamResult);
        } catch (Exception  exception){
            exception.printStackTrace();
        }
    }

    public List search(String selectedItem, List<String> criteriaListText, List<LocalDate> criteriaListDate){
        List<Pet> petList = getPetList();
        List                resultList;
        resultList = new ArrayList<Pet>();
        criteria criteriaForSelection = criteria.getCriteriaByName(selectedItem);
        switch (criteriaForSelection){
            case CRITERIA_1:
                final LocalDate LAST_APPOINTMENT = criteriaListDate.get(0);
                final String SURNAME = criteriaListText.get(0),
                             NAME    = criteriaListText.get(1),
                             PATRONYM = criteriaListText.get(2);
                for(Pet pet:petList) {
                    if (pet.getSurname().equals(SURNAME) && pet.getName().equals(NAME) && pet.getPatronym().equals(PATRONYM) && pet.getPetLastAppointment().equals(LAST_APPOINTMENT)) {
                        resultList.add(pet);
                    }
                }
                break;
            case CRITERIA_2:
                final LocalDate BIRTHDAY = criteriaListDate.get(1);
                final String PET_NAME = criteriaListText.get(3);
                for(Pet pet:petList) {
                    if (pet.getPetName().equals(PET_NAME) && pet.getPetBirthday().equals(BIRTHDAY)) {
                        resultList.add(pet);
                    }
                }
                break;
            case CRITERIA_3:
                final String  FRAZE   = criteriaListText.get(4);
                for(Pet pet:petList) {
                    if (pet.checkFrazeFromDiagnosis(pet.getDiagnosis(), FRAZE)) {
                        resultList.add(pet);
                    }
                }
                break;
        }

        return resultList;
    }
    enum criteria {
        CRITERIA_1("Дата последнего приема и ФИО ветеринара"),
        CRITERIA_2("Имя питомца и дата рождения"),
        CRITERIA_3("По фразе из диагноза");
        private final String name;

        criteria(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static criteria getCriteriaByName(String name) {
            criteria res = null;
            for (criteria x : values()) {
                // либо equalsIgnoreCase, на ваше усмотрение
                if (x.getName().equals(name)) {
                     res = x;
                }
            }
            return res;
        }
    }
    public void delete(List<Pet> indexList){
        for(Pet pet:indexList){
            getPetList().remove(pet);
        }
    }
}
