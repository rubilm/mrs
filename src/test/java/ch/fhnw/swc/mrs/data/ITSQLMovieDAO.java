package ch.fhnw.swc.mrs.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ch.fhnw.swc.mrs.model.Movie;
import ch.fhnw.swc.mrs.model.RegularPriceCategory;
import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.DatabaseUnitException;
import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.NoSuchColumnException;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.dataset.xml.XmlDataSet;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.dbunit.operation.DatabaseOperation;
import org.xml.sax.InputSource;

import ch.fhnw.swc.mrs.model.PriceCategory;
import ch.fhnw.swc.mrs.model.User;


public class ITSQLMovieDAO extends DBTestCase {

    /** Class under test: UserDAO. */
    private SQLMovieDAO dao;
    private IDatabaseTester tester;
    private Connection connection;
    private LocalDate today;

    private static final String COUNT_SQL = "SELECT COUNT(*) FROM movies";
    private static final String DB_CONNECTION = "jdbc:hsqldb:mem:mrs";

    /** Create a new Integration Test object. */
    public ITSQLMovieDAO() {
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS, HsqlDatabase.DB_DRIVER);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL, DB_CONNECTION);
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME, "sa");
        System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD, "");
    }

    @Override
    protected void setUpDatabaseConfig(DatabaseConfig config) {
        config.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new HsqldbDataTypeFactory());
    }

    @Override
    protected IDataSet getDataSet() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("MovieDaoTestData.xml");
        return new FlatXmlDataSetBuilder().build(new InputSource(stream));
    }

    static {
        try {
            new HsqlDatabase().initDB(DB_CONNECTION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize a DBUnit DatabaseTester object to use in tests.
     *
     * @throws Exception
     *             whenever something goes wrong.
     */
    public void setUp() throws Exception {
        super.setUp();
        PriceCategory.init();
        tester = new JdbcDatabaseTester(HsqlDatabase.DB_DRIVER, DB_CONNECTION);
        connection = getConnection().getConnection();
        dao = new SQLMovieDAO(connection);
        today = LocalDate.now();
    }

    public void tearDown() throws Exception {
        connection.close();
        tester.onTearDown();
    }

    public void testDeleteNonexisting() throws Exception {
        // count no. of rows before deletion
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows = r.getInt(1);
        assertEquals(3, rows);


        // Delete non-existing record
        Movie movie = new Movie("FindingNemo", today, RegularPriceCategory.getInstance(), 0);
        movie.setId(42);
        dao.delete(movie);

        r = s.executeQuery(COUNT_SQL);
        r.next();
        rows = r.getInt(1);
        assertEquals(3, rows);
    }


    public void testDelete() throws Exception {
        // count no. of rows before deletion
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows = r.getInt(1);
        assertEquals(3, rows);

        // delete existing record
        Movie movie = new Movie("Avatar", LocalDate.of(2005,9,17),RegularPriceCategory.getPriceCategoryFromId("New Release"),16 );
        movie.setId(2);
        dao.delete(movie);


        // Fetch database data after deletion
        IDataSet databaseDataSet = tester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable("MOVIES");

        InputStream stream = this.getClass().getResourceAsStream("MovieDaoTestResult.xml");
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(stream);
        ITable expectedTable = expectedDataSet.getTable("MOVIES");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    //Mach hier weiter

    public void testGetAll() throws DatabaseUnitException, SQLException, Exception {
        List<Movie> movielist = dao.getAll();
        ITable actualTable = convertToTable(movielist);

        InputStream stream = this.getClass().getResourceAsStream("MovieDaoTestData.xml");
        IDataSet expectedDataSet = new FlatXmlDataSetBuilder().build(stream);
        ITable expectedTable = expectedDataSet.getTable("MOVIES");

        Assertion.assertEquals(expectedTable, actualTable);
    }

    public void testGetAllSingleRow() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("MovieDaoSingleRow.xml");
        IDataSet dataSet = new FlatXmlDataSetBuilder().build(stream);
        DatabaseOperation.CLEAN_INSERT.execute(tester.getConnection(), dataSet);

        List<Movie> movielist = dao.getAll();
        assertEquals(1, movielist.size());
        assertEquals("Casablanca", movielist.get(0).getTitle());
    }

    public void testGetAllEmptyTable() throws Exception {
        InputStream stream = this.getClass().getResourceAsStream("MovieDaoEmpty.xml");
        IDataSet dataSet = new XmlDataSet(stream);
        DatabaseOperation.CLEAN_INSERT.execute(tester.getConnection(), dataSet);

        List<Movie> movielist = dao.getAll();
        assertNotNull(movielist);
        assertEquals(0, movielist.size());
    }

    public void testGetById() throws SQLException {
        Movie movie = dao.getById(2);
        assertEquals("Avatar", movie.getTitle());
    }

    public void testGetByName() throws SQLException {
        List<Movie> movielist = dao.getByTitle("Avatar");
        assertEquals(1, movielist.size());
    }

    public void testUpdate() throws SQLException {
        // count no. of rows before operation
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows0 = r.getInt(1);

        Movie casablanca = dao.getById(3);
        casablanca.setTitle("Daisy");
        dao.saveOrUpdate(casablanca);
        Movie actual = dao.getById(3);
        assertEquals(casablanca.getTitle(), actual.getTitle());

        r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows1 = r.getInt(1);
        assertEquals(rows0, rows1);
    }

    public void testSave() throws Exception {
        // count no. of rows before operation
        Statement s = connection.createStatement();
        ResultSet r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows1 = r.getInt(1);

        Movie goofy = new Movie("Goofy", today, RegularPriceCategory.getInstance(), 12);
        dao.saveOrUpdate(goofy);
        Movie actual = dao.getById(goofy.getId());
        assertEquals(goofy.getTitle(), actual.getTitle());

        r = s.executeQuery(COUNT_SQL);
        r.next();
        int rows2 = r.getInt(1);
        assertEquals(rows1 + 1, rows2);
    }

    @SuppressWarnings("deprecation")
    private ITable convertToTable(List<Movie> movieslist) throws Exception {
        ITableMetaData meta = new TableMetaData();
        DefaultTable t = new DefaultTable(meta);
        int row = 0;
        for (Movie u : movieslist) {
            t.addRow();
            LocalDate d = u.getReleaseDate();
            t.setValue(row, "id", u.getId());
            t.setValue(row, "title", u.getTitle());
            t.setValue(row, "isrented", u.isRented());
            t.setValue(row, "releasedate", new Date(d.getYear()-1900, d.getMonthValue()-1, d.getDayOfMonth()));
            t.setValue(row, "pricecategory", u.getPriceCategory());
            t.setValue(row, "agerating", u.getAgeRating());
            row++;
        }
        return t;
    }


    private static final class TableMetaData implements ITableMetaData {

        private List<Column> cols = new ArrayList<>();

        TableMetaData() {
            cols.add(new Column("id", DataType.INTEGER));
            cols.add(new Column("title", DataType.VARCHAR));
            cols.add(new Column("isrented", DataType.BOOLEAN));
            cols.add(new Column("releasedate", DataType.DATE));
            cols.add(new Column("pricecategory", DataType.VARCHAR));
            cols.add(new Column("agerating", DataType.INTEGER));
        }

        @Override
        public int getColumnIndex(String colname) throws DataSetException {
            int index = 0;
            for (Column c : cols) {
                if (c.getColumnName().equals(colname.toLowerCase())) {
                    return index;
                }
                index++;
            }
            throw new NoSuchColumnException(getTableName(), colname);
        }

        @Override
        public Column[] getColumns() throws DataSetException {
            return cols.toArray(new Column[4]);
        }

        @Override
        public Column[] getPrimaryKeys() throws DataSetException {
            Column[] cols = new Column[1];
            cols[0] = new Column("id", DataType.INTEGER);
            return cols;
        }

        @Override
        public String getTableName() {
            return "movies";
        }
    }
}
