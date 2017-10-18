package ch.fhnw.swc.mrs.model;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ITBill {

    private Movie m1;
    private Movie m2;
    private User u1;
    private User u2;


    @Before
    public void setup(){
        m1 = new Movie();
        m1.setTitle("Avatar");
        m1.setAgeRating(3);
        m1.setPriceCategory(RegularPriceCategory.getInstance());

        m2 = new Movie();

        u1 = new User("Hunziker", "Hans", LocalDate.of(1960,2,12));


    }

    @Test
    public void ITtest(){
        Bill b = new Bill("Muster", "Hans", createRentalList());

        System.out.println(b.print());

    }

    @Test
    public void testPrintRegularUSername(){

    }

    @Test
    public void testPrintLongUsername(){

    }



    private List<Rental> createRentalList() {
        List<Rental> rentals = new ArrayList<>(3);

        Movie m1 = mock(Movie.class);
        Movie m2 = mock(Movie.class);
        Movie m3 = mock(Movie.class);
        when(m1.getTitle()).thenReturn("Avatar");
        when(m2.getTitle()).thenReturn("Casablanca");
        when(m3.getTitle()).thenReturn("Tron");

        Rental r1 = mock(Rental.class);
        Rental r2 = mock(Rental.class);
        Rental r3 = mock(Rental.class);
        when(r1.getMovie()).thenReturn(m1);
        when(r2.getMovie()).thenReturn(m2);
        when(r3.getMovie()).thenReturn(m3);
        when(r1.getRentalDays()).thenReturn(1L);
        when(r2.getRentalDays()).thenReturn(2L);
        when(r3.getRentalDays()).thenReturn(3L);
        when(r1.getRentalFee()).thenReturn(8.4);
        when(r2.getRentalFee()).thenReturn(17.2);
        when(r3.getRentalFee()).thenReturn(26.4);

        rentals.add(r1);
        rentals.add(r2);
        rentals.add(r3);

        return rentals;
    }

}
