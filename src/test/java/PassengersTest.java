import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static io.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;

public class PassengersTest {

    @Test
    public void verifyInterface() {
        Iterator<Row> iterator = readTestData("passengers.xlsx").iterator();
        iterator.next();

        while(iterator.hasNext()) {
            Row row = iterator.next();
            String authorizationHeader = row.getCell(0).getStringCellValue();
            String correlationidHeader = row.getCell(1).getStringCellValue();
            String domainHeader = row.getCell(2).getStringCellValue();
            String channelHeader = row.getCell(3).getStringCellValue();
            String fligthIdPathParam = row.getCell(4).getStringCellValue();
            String dateQuery = row.getCell(5).getStringCellValue();

            given()
                .header("authorization", authorizationHeader).and()
                .header("correlationid", correlationidHeader).and()
                .header("domain", domainHeader).and()
                .header("channel", channelHeader).and()
                .param("date", dateQuery).and().
            when()
                .get(format("http://localhost:3000/flights/%s/passengers", fligthIdPathParam)).
            then()
                .statusCode(200);
        }


    }

    private Sheet readTestData(String fileName) {
        InputStream testDataIs = this.getClass().getResourceAsStream(fileName);
        try {
            return new XSSFWorkbook(testDataIs).getSheetAt(0);
        } catch (IOException e) {
            throw new RuntimeException(format("Unable to load test data from %s", fileName));

        }
    }
}
