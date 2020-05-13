package plagamedicum.ppvis.lab2s4.model;

public class FrazeChecker {
    public boolean checkFrazeFromDiagnosis(String diagnosis, String fraze){
        boolean result = false;
        String[] words = diagnosis.split(" ");
        if(words.length == 2){
            if((words[0]+" "+words[1]).equals(fraze)){ result = true; }
        }
        if(words.length == 3){
            if(((words[0]+" "+words[1]).equals(fraze)) || ((words[1]+" "+words[2]).equals(fraze)) || (words[0]+" "+words[1]+" "+words[2]).equals(fraze) || ((words[0]+" "+words[2]).equals(fraze))){
                result = true;
            }
        }
        for(String word : words) {
            if (word.equals(fraze)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
