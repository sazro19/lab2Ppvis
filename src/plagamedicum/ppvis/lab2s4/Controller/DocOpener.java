package plagamedicum.ppvis.lab2s4.Controller;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import plagamedicum.ppvis.lab2s4.model.snpOfVeterinarian;
import plagamedicum.ppvis.lab2s4.model.Pet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DocOpener {
    private String        petName;
    private LocalDate     petBirthday;
    private LocalDate     petLastAppointment;
    private snpOfVeterinarian snp;
    private String        diagnosis;
    private List<Pet>     petList;

    public List<Pet> openDoc (File file) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactory;
        SAXParser parser;
        XMLHandler handler;

        petList = new ArrayList<>();

        handler       = new XMLHandler();
        parserFactory = SAXParserFactory.newInstance();
        parser        = parserFactory.newSAXParser();
        parser.parse(file, handler);
        return petList;
    }

    private class XMLHandler extends DefaultHandler {
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException{
            if(qName.equals("petName")){
                petName = attributes.getValue("name");
            }
            if(qName.equals("petBirthday")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
                String date = attributes.getValue("name");
                petBirthday = LocalDate.parse(date, formatter);
            }
            if(qName.equals("petLastAppointment")){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
                String date = attributes.getValue("name");
                petLastAppointment = LocalDate.parse(date, formatter);
            }
            if(qName.equals("snp")){
                snp = new snpOfVeterinarian(
                    attributes.getValue("surname"),
                    attributes.getValue("name"),
                    attributes.getValue("patronym")
                );
            }
            if(qName.equals("diagnosis")){
                diagnosis = attributes.getValue("name");
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException{
            if(qName.equals("pet")){
                petList.add(new Pet(
                        petName,
                        petBirthday,
                        petLastAppointment,
                        snp,
                        diagnosis
                ));
            }
        }
    }
}
