package ch.fhnw.swc.mrs.model;

import java.util.List;

public class StatementImp extends Statement {



        private List<Rental> rentals;
        private String firstName;
        private String lastName;

        /**
         * Creates a statement object for a person (with the given name parameter) and a list of rentals.
         * @param name the family name.
         * @param firstName the first name.
         * @param rentals a list of rentals to be billed.
         */
        public StatementImp(String name, String firstName, List<Rental> rentals) {
            super(name, firstName, rentals);

        }


        /**
         * Prints a statement for the given client and the list of rentals.
         * @return a multi-line string containing an aligned statement (when using fixed spaced font).
         */
        public String print(){
            return "Statement: "+firstName;
        };
    }


