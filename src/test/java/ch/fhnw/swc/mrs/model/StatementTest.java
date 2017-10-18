package ch.fhnw.swc.mrs.model;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class StatementTest {
    
    private Statement s;
    private List<Rental> rentals;

    @Before
    public void setup() {
        Rental r1 = mock(Rental.class);
        Rental r2 = mock(Rental.class);
        Rental r3 = mock(Rental.class);

        rentals = new ArrayList<>(3);
        rentals.add(r1);
        rentals.add(r2);
        rentals.add(r3);
    }

    @Test
    public void testStatement() {
        s = new StatementImpl("Muster", "Hans", rentals);
        assertEquals("Muster", s.getLastName());
        assertEquals("Hans", s.getFirstName());
        assertEquals(3, s.getRentals().size());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testFirstName() {
        new StatementImpl("Muster", "Maximilian", rentals);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testLastName() {
       new StatementImpl("Mustermann", "Hans", rentals);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testRentals() {
       new StatementImpl("Muster", "Hans", null);
    }

}

 class StatementImpl extends Statement {
    public StatementImpl(String name, String firstName, List<Rental> rentals ) {
        super(name, firstName, rentals);
    }
    @Override
    public String print() {
        return getFirstName() + getLastName();
    }
}
