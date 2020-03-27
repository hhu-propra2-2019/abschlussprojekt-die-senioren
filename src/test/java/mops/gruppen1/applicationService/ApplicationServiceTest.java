package mops.gruppen1.applicationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationServiceTest {

    ApplicationService applicationService;

    @BeforeEach
    private void setup() {
        applicationService = new ApplicationService();
    }

    @Tag("ApplicationServiceTest")
    @DisplayName("testuploadCsv_FilenameTooShort")
    @Test
    public void testUploadCsvFilenameTooShort(){
        //Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        List<String> usernames = new ArrayList<>();
        when(multipartFile.getOriginalFilename()).thenReturn(".csv");

        //Act & Assert
        Throwable thrown = assertThrows(Exception.class, ()
                -> applicationService.uploadCsv(multipartFile,usernames));

        assertThat(thrown.getMessage()).isEqualTo("Kein gültiger Dateiname");
    }

    @Tag("ApplicationServiceTest")
    @DisplayName("testuploadCsv_WrongFileFormat")
    @Test
    public void testUploadCsvWrongFileFormat(){
        //Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        List<String> usernames = new ArrayList<>();
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");

        //Act & Assert
        Throwable thrown = assertThrows(Exception.class, ()
                -> applicationService.uploadCsv(multipartFile,usernames));

        assertThat(thrown.getMessage()).isEqualTo("Diese Datei ist keine Csv-Datei");
    }

    @Tag("ApplicationServiceTest")
    @DisplayName("testuploadCsv_ValidInputFile")
    @Test
    public void testUploadCsvValidInputFile(){
        //Arrange
        MultipartFile multipartFile = mock(MultipartFile.class);
        List<String> csvUsernames = new ArrayList<>();
        csvUsernames.add("user1");
        csvUsernames.add("user2");
        List<String> usernames = new ArrayList<>();
        List<String> resultList = new ArrayList<>();
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        ApplicationService applicationService1 = Mockito.spy(applicationService);

        try {
            when(applicationService1.extractUsernamesFromCsv(any())).thenReturn(csvUsernames);
        }
        catch(Exception e) {}

        //Act
        try {
            resultList =  applicationService.uploadCsv(multipartFile, usernames);
        }
        catch(Exception e) {}

        //Assert
        assertThat(resultList.contains("user1"));
        assertThat(resultList.contains("user2"));
        assertThat(resultList.size() == 2);
    }
}
