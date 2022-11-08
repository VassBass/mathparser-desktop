package repository;

import model.Equation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Repository to storage of equations
 * @see EquationRepository_sql
 */
public interface EquationRepository {

    /**
     * Gets connection with DB
     *
     * @return Connection Object
     *
     * @throws SQLException if DB wasn't found
     *
     */
    default Connection getConnection(String url, String user, String password) throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * @return list of equations
     *
     * @see EquationRepository_sql#getAll()
     */
    ArrayList<Equation>getAll();

    /**
     * @param id of equation who need to search
     *
     * @return equation with this id
     * null if equation with this id not found
     *
     * @see EquationRepository_sql#get(int)
     */
    Equation get(int id);

    /**
     * @param condition of search (<, <=, =, >=, >)
     *
     * @param result number for compare by condition of search with equations results
     *
     * @return list of equations that match the search terms
     * empty list if condition == null or if condition has incorrect characters
     *
     * @see EquationRepository_sql#get(String, double)
     */
    ArrayList<Equation>get(String condition, double result);

    /**
     * Adds new equation
     *
     * @param equation to add
     *
     * @return true if equation was added
     * false if wasn't
     *
     * @see EquationRepository_sql#add(Equation)
     */
    boolean add(Equation equation);

    /**
     * Changes equation with id of equation from @param
     *
     * @param equation to change
     *
     * @return true if equation was changed
     * false if not
     *
     * @see EquationRepository_sql#set(Equation)
     */
    boolean set(Equation equation);

    /**
     * Removes equation with this id
     *
     * @param id of equation who need to removed
     *
     * @return true if equation was removed
     * false if not
     *
     * @see EquationRepository_sql#remove(int)
     */
    boolean remove(int id);
}
