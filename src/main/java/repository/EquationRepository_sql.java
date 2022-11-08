package repository;

import model.Equation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Implementation of {@link EquationRepository}
 */
public class EquationRepository_sql implements EquationRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(EquationRepository_sql.class);

    private static final String OOPS = "Oops!";

    private String dbUrl, dbUser, dbPassword;

    public EquationRepository_sql(){
        getProperties();
        createTableIfNotExists();
    }

    /**
     * This constructor for Tests
     *
     * @param url of DB
     * @param user of DB
     * @param password of DB
     */
    public EquationRepository_sql(String url, String user, String password){
        dbUrl = url;
        dbUser = user;
        dbPassword = password;
        createTableIfNotExists();
    }

    /**
     * Gets url of db from jdbc.properties file
     *
     * @see EquationRepository#getConnection(String url, String user, String password)
     * @see #dbUrl
     * @see #dbUser
     * @see #dbPassword
     */
    private void getProperties(){
        try {
            String propertiesFileName = "jdbc.properties";
            InputStream in = EquationRepository.class.getClassLoader().getResourceAsStream(propertiesFileName);
            if (in == null){
                LOGGER.warn("Couldn't find property file");
                String message = "Database connection error. Want to try again?";
                int result = JOptionPane.showConfirmDialog(null, message, OOPS, JOptionPane.OK_CANCEL_OPTION);
                if (result == 0){
                    getProperties();
                }else {
                    System.exit(0);
                }
            }else {
                LOGGER.info("Properties file was found. Trying to read it.");
                Properties properties = new Properties();
                properties.load(in);

                dbUrl = properties.getProperty("jdbc.URL");
                dbUser = properties.getProperty("jdbc.USER");
                dbPassword = properties.getProperty("jdbc.PASSWORD");

                LOGGER.info("Properties file was read successful.");
                LOGGER.debug("DB URL = {}", dbUrl);
            }
        } catch (IOException e) {
            LOGGER.warn("Exception was thrown: ",e);
            String message = "Database connection error. Want to try again?";
            int result = JOptionPane.showConfirmDialog(null, message, OOPS, JOptionPane.OK_CANCEL_OPTION);
            if (result == 0){
                getProperties();
            }else {
                System.exit(0);
            }
        }
    }

    /**
     * Creates DB file if not exists and table "equations" to keep equations and results
     *
     * @see #dbUrl
     */
    private void createTableIfNotExists(){
        LOGGER.debug("Received a request to create a new table if it doesn't exist");

        String sql = """
                CREATE TABLE IF NOT EXISTS equations
                 (id integer NOT NULL UNIQUE,
                 equation test NOT NULL,
                 result real NOT NULL,
                 PRIMARY KEY ("id" AUTOINCREMENT));""";

        LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
        try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
             Statement statement = connection.createStatement()){
            int result = statement.executeUpdate(sql);

            if (result>0) LOGGER.info("New table \"equations\" was create");
        } catch (SQLException ex) {
            LOGGER.error("Exception was thrown:", ex);
        }
    }

    /**
     * @return list of equations from DB
     * empty list if DB wasn't found
     *
     * @see #dbUrl
     */
    @Override
    public ArrayList<Equation> getAll() {
        LOGGER.debug("Received a request to get list of all equations from DB");

        ArrayList<Equation> equations = new ArrayList<>();
        String sql = "SELECT * FROM equations;";

        LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
        try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){
            while (resultSet.next()){
                Equation equation = new Equation();
                equation.setId(resultSet.getInt("id"));
                equation.setEquation(resultSet.getString("equation"));
                equation.setResult(resultSet.getDouble("result"));
                equations.add(equation);
            }

            LOGGER.debug("Received list: {}", equations);
        } catch (SQLException ex) {
            LOGGER.error("Exception was thrown:", ex);
        }

        return equations;
    }

    /**
     * @param id of equation who need to search
     *
     * @return equation from db with this id
     * null if equation with this id not found in DB or DB wasn't found
     *
     * @see #dbUrl
     */
    @Override
    public Equation get(int id) {
        LOGGER.debug("Received a request to get equation with id = {}", id);

        String sql = "SELECT * FROM equations WHERE id = " + id + ";";
        Equation equation = null;

        LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
        try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)){

            if (resultSet.next()) {
                equation = new Equation();
                equation.setId(resultSet.getInt("id"));
                equation.setEquation(resultSet.getString("equation"));
                equation.setResult(resultSet.getDouble("result"));
            }

        } catch (SQLException ex) {
            LOGGER.error("Exception was thrown:", ex);
        }

        LOGGER.debug("Received equation: {}", equation);
        return equation;
    }

    /**
     * @param condition of search (<, <=, =, >=, >)
     *
     * @param result number for compare by condition of search with equations results
     *
     * @return list of equations that match the search terms
     * empty list if condition == null, if condition has incorrect characters or DB wasn't found
     *
     * @see #dbUrl
     */
    public ArrayList<Equation>get(String condition, double result){
        LOGGER.debug("A request was received to obtain a list of equations that match the condition: result {} {}", condition, result);

        ArrayList<Equation>equations = new ArrayList<>();

        if (condition != null) {
            if (condition.equals("<") || condition.equals("<=") || condition.equals("=")
                    || condition.equals(">=") || condition.equals(">")) {
                String sql = "SELECT * FROM equations WHERE result " + condition + " " + result + ";";

                LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
                try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)) {

                    while (resultSet.next()) {
                        Equation equation = new Equation();
                        equation.setId(resultSet.getInt("id"));
                        equation.setEquation(resultSet.getString("equation"));
                        equation.setResult(resultSet.getDouble("result"));
                        equations.add(equation);
                    }

                } catch (SQLException ex) {
                    LOGGER.error("Exception was thrown:", ex);
                }
            }
        }

        LOGGER.debug("Received list: {}", equations);
        return equations;
    }

    /**
     * Adds new equation to DB
     *
     * @param equation to add
     *
     * @return true if equation was added
     * false if DB wasn't found
     *
     * @see #dbUrl
     */
    @Override
    public boolean add(Equation equation) {
        LOGGER.debug("Received a request to add equation = {}", equation);

        if (equation == null) return false;

        String sql = "INSERT INTO equations (equation, result)"
                + "VALUES ("
                + "'" + equation.getEquation() + "'"
                + ", " + equation.getResult()
                + ");";

        LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
        try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
             Statement statement = connection.createStatement()){

            int result = statement.executeUpdate(sql);
            if (result > 0){
                LOGGER.debug("Equation was added");
                return true;
            }else return false;
        } catch (SQLException ex) {
            LOGGER.error("Exception was thrown:", ex);
            return false;
        }
    }

    /**
     * Changes equation from DB with id of equation from @param
     *
     * @param equation to change
     *
     * @return true if equation was changed
     * false if @param == null, if equation with this id wasn't found or DB wasn't found
     *
     * @see #dbUrl
     */
    @Override
    public boolean set(Equation equation) {
        LOGGER.debug("Received a request to set equation = {}", equation);

        if (equation == null) return false;

        String sql = "UPDATE equations SET "
                + "equation = '" + equation.getEquation() + "'"
                + ", result = " + equation.getResult()
                + " WHERE id = " + equation.getId() + ";";

        LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
        try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
             Statement statement = connection.createStatement()){

            int result = statement.executeUpdate(sql);

            if (result > 0){
                LOGGER.debug("Equation was sets");
                return true;
            }else return false;
        } catch (SQLException ex) {
            LOGGER.error("Exception was thrown:", ex);
            return false;
        }
    }

    /**
     * Removes equation with this id from DB
     *
     * @param id of equation who need to removed
     *
     * @return true if equation was removed
     * false if equation with this id wasn't found or DB wasn't found
     *
     * @see #dbUrl
     */
    @Override
    public boolean remove(int id) {
        LOGGER.debug("Received a request to remove equation with id = {}", id);

        String sql = "DELETE FROM equations WHERE id = " + id + ";";

        LOGGER.debug("Trying to connect to DB with URL = {}", dbUrl);
        try (Connection connection = getConnection(dbUrl, dbUser,dbPassword);
             Statement statement = connection.createStatement()){

            int result = statement.executeUpdate(sql);

            if (result > 0){
                LOGGER.debug("Equation was removed");
                return true;
            }else return false;
        } catch (SQLException ex) {
            LOGGER.error("Exception was thrown:", ex);
            return false;
        }
    }
}
