package data;

import entity.InternshipApplication;
import java.util.Collection;

public interface ApplicationRepository {
    void addApplication(InternshipApplication application);
    InternshipApplication getApplication(String applicationID);
    Collection<InternshipApplication> getAllApplications();
    void removeApplication(String applicationID);
    void saveApplications(String filePath);
    void loadApplications(String filePath);
}
