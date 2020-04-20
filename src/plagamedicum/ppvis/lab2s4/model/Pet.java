package plagamedicum.ppvis.lab2s4.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Pet {
	private String     petName;
	private LocalDate  petBirthday;
	private LocalDate  petLastAppointment;
	private SNP 	   snp;
	private String     diagnosis;

	public Pet(String petName, LocalDate petBirthday, LocalDate petLastAppointment, SNP snp, String diagnosis){
		this.petName = petName;
		this.petBirthday = petBirthday;
		this.petLastAppointment = petLastAppointment;
		this.snp = snp;
		this.diagnosis = diagnosis;
	}

	public SNP getSnp(){
		return snp;
	}

	public String getSurname(){
		return snp.getSurname();
	}

	public String getName() { return  snp.getName(); }

	public String getPatronym() { return  snp.getPatronym(); }

	public String getPetName() { return  this.petName; }

	public void setPetName(String petName) { this.petName = petName; }

	public LocalDate getPetBirthday() { return this.petBirthday; }

	public String getPetBirthdayString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
		String formattedString = petBirthday.format(formatter);
		return  formattedString;
	}

	public void  setPetBirthday(LocalDate petBirthday) { this.petBirthday = petBirthday; }

	public LocalDate getPetLastAppointment() { return this.petLastAppointment; }

	public String getPetLastAppointmentString() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
		String formattedString = petLastAppointment.format(formatter);
		return  formattedString;
	}

	public void setPetLastAppointment(LocalDate petLastAppointment) { this.petLastAppointment = petLastAppointment; }

	public void setSnp(SNP snp){
		this.snp = snp;
	}

	public String getAlignSnp(){
		return snp.getSurname() + " " + snp.getName() + " " + snp.getPatronym();
	}

	public void setAlignSnp(String alignSnp){
		this.snp = new SNP(alignSnp);
	}

	public String getDiagnosis(){
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis){
		this.diagnosis = diagnosis;
	}

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
