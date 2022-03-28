package mediko.newmediko.Utils;

public class Friends {
    private String City, Experience, Gender, Name, ProfileImage, Qualification, Specalization, key, Fees;

    public Friends() {
    }

    public Friends(String city, String experience, String gender, String name, String profileImage, String qualification, String specialization, String key, String fees) {
        City = city;
        Experience = experience;
        Gender = gender;
        Name = name;
        ProfileImage = profileImage;
        Qualification = qualification;
        Specalization = specialization;
        this.key = key;
        Fees = fees;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getSpecialization() {
        return Specalization;
    }

    public void setSpecialization(String specialization) {
        Specalization = specialization;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFees() {
        return Fees;
    }

    public void setFees(String fees) {
        Fees = fees;
    }
}

