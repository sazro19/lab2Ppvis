package plagamedicum.ppvis.lab2s4.model;


import java.util.ArrayList;
import java.util.List;


public class Model {
    private List<Pet> petList;

    public Model(){
            petList = new ArrayList<>();
    }

    public List<Pet> getPetList(){
        return petList;
    }

    public void setPetList(List<Pet> petList){
        this.petList = petList;
    }

    public void addPet(Pet pet){
        petList.add(pet);
    }


}
