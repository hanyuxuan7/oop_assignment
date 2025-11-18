package data;

import entity.Internship;
import java.util.Collection;

public interface InternshipRepository {
    void addInternship(Internship internship);
    Internship getInternship(String internshipID);
    Collection<Internship> getAllInternships();
    void removeInternship(String internshipID);
    void saveInternships(String filePath);
    void loadInternships(String filePath);
}
