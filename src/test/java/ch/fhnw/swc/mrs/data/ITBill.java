package ch.fhnw.swc.mrs.data;

import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.PriceCategory;
import ch.fhnw.swc.mrs.model.RegularPriceCategory;
import ch.fhnw.swc.mrs.model.User;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

public class ITBill {

    User u1;
    User u2;
    Movie m1;
    Movie m2;

    @Before
    public void setup() {
        PriceCategory pc = RegularPriceCategory.getInstance();
        u1 = new User("Marco", "Ghilardelli", LocalDate.of(1994, 7, 29));
        m1 = new Movie("JamesBond", LocalDate.now(), pc, 10);
        u2 = new User("Maurer", "Johannes-Ueli-August-Werner", LocalDate.of(1994, 7, 29));
        m2 = new Movie("Harry Potter", LocalDate.now(), pc, 12);
    }

    @Test
    public void testPrintRegularUsername() {

        DbMRSServices service = new DbMRSServices();
        service.init();
        service.createRental(u1, m1);
        service.createRental(u1, m2);
    }

    @Test
    public void testPrintLongUsername() {

        DbMRSServices service = new DbMRSServices();
        service.init();
        service.createRental(u2, m1);
        service.createRental(u2, m2);
    }

}
