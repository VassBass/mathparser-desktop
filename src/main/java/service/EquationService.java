package service;

import model.Equation;
import repository.EquationRepository;
import repository.EquationRepository_sql;

import java.util.ArrayList;

/**
 * Interface manages of saved equations
 * @see EquationService_impl
 */
public interface EquationService {

    /**
     * @return list of equations
     *
     * @see EquationService_impl#getAll()
     * @see EquationRepository#getAll()
     * @see EquationRepository_sql#getAll()
     */
    ArrayList<Equation>getAll();

    /**
     * @param id of equation that need to search
     *
     * @return equation with this id
     * null if equation with this id not found
     *
     * @see EquationService_impl#get(int)
     * @see EquationRepository#get(int) 
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
     * @see EquationService_impl#get(String, double)
     * @see EquationRepository#get(String, double)
     * @see EquationRepository_sql#get(String, double)
     */
    ArrayList<Equation>get(String condition, double result);

    /**
     * Changes equation with id of equation from @param
     *
     * @param equation to change
     *
     * @return true if equation was changed
     * false if not
     *
     * @see EquationService_impl#set(Equation)
     * @see EquationRepository#set(Equation)
     * @see EquationRepository_sql#set(Equation)
     */
    boolean set(Equation equation);


    /**
     * Adds new equation
     *
     * @param equation to add
     *
     * @return true if equation was added
     * false if wasn't
     *
     * @see EquationService_impl#add(Equation)
     * @see EquationRepository#add(Equation)
     * @see EquationRepository_sql#add(Equation)
     */
    boolean add(Equation equation);

    /**
     * Removes equation with this id
     *
     * @param id of equation that need to removed
     *
     * @return true if equation was removed
     * false if not
     *
     * @see EquationService_impl#remove(int)
     * @see EquationRepository#remove(int)
     * @see EquationRepository_sql#remove(int)
     */
    boolean remove(int id);
}
