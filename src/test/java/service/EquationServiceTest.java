package service;

import model.Equation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sqlite.JDBC;
import repository.EquationRepository_sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class EquationServiceTest {
    private static final String DB_URL = "jdbc:sqlite:testData.db";
    private final EquationService service = new EquationService_impl(new EquationRepository_sql(DB_URL, null, null));

    private static Equation equation1(){
        Equation e = new Equation();
        e.setId(1);
        e.setEquation("2*2.10");
        e.setResult(4.20);
        return e;
    }

    private static Equation equation2(){
        Equation e = new Equation();
        e.setId(2);
        e.setEquation("2*2");
        e.setResult(4);
        return e;
    }

    private static Equation equation3(){
        Equation e = new Equation();
        e.setId(3);
        e.setEquation("5*(4-5) + 56");
        e.setResult(51D);
        return e;
    }

    private static final ArrayList<Equation> EQUATIONS = new ArrayList<>();

    @BeforeEach
    void setUp() {
        EQUATIONS.clear();
        EQUATIONS.add(equation1());
        EQUATIONS.add(equation2());
        EQUATIONS.add(equation3());

        try {
            DriverManager.registerDriver(new JDBC());
            try (Connection connection = DriverManager.getConnection(DB_URL);
                 Statement statement = connection.createStatement()){

                StringBuilder sql = new StringBuilder("DELETE FROM equations;");
                statement.execute(sql.toString());

                sql.setLength(0);
                sql.append("INSERT INTO equations (id, equation, result) VALUES ");
                for (Equation e : EQUATIONS){
                    sql.append("\n(")
                            .append(e.getId())
                            .append(", '").append(e.getEquation()).append("'")
                            .append(", ").append(e.getResult())
                            .append("),");
                }
                sql.deleteCharAt(sql.length()-1);
                sql.append(";");

                statement.execute(sql.toString());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * @see EquationService#getAll()
     */
    @Test
    void getAll() {
        assertIterableEquals(EQUATIONS, service.getAll());
    }

    /**
     * @see EquationService#get(int)
     * @see EquationService#get(String, double)
     */
    @Test
    void get() {
        //Testing of get(int id)
        assertEquals(equation1(), service.get(1));

        assertNull(service.get(5));

        //Testing of get(String condition, double result)
        assertIterableEquals(EQUATIONS, service.get(">=",4D));

        EQUATIONS.remove(2);
        assertIterableEquals(EQUATIONS, service.get("<", 51D));

        assertIterableEquals(new ArrayList<Equation>(), service.get(">", 100D));
        assertIterableEquals(new ArrayList<Equation>(), service.get(null, 0));
        assertIterableEquals(new ArrayList<Equation>(), service.get("khgdf", 0));
    }

    /**
     * @see EquationService#set(Equation)
     */
    @Test
    void set() {
        Equation equation = equation1();
        equation.setEquation("9*9");
        equation.setResult(81D);
        EQUATIONS.set(0, equation);

        assertTrue(service.set(equation));
        assertIterableEquals(EQUATIONS, service.getAll());

        equation.setId(9);
        assertFalse(service.set(equation));
        equation.setId(1);
        assertIterableEquals(EQUATIONS, service.getAll());

        assertFalse(service.set(null));
        assertIterableEquals(EQUATIONS, service.getAll());
    }

    /**
     * @see EquationService#add(Equation)
     */
    @Test
    void add() {
        Equation equation = new Equation();
        equation.setEquation("4/4");
        equation.setResult(1D);

        assertTrue(service.add(equation));

        int id = service.getAll().get(service.getAll().size()-1).getId();
        equation.setId(id);
        EQUATIONS.add(equation);
        assertEquals(equation, service.get(id));
        assertIterableEquals(EQUATIONS, service.getAll());

        assertFalse(service.add(null));
        assertIterableEquals(EQUATIONS, service.getAll());
    }

    /**
     * @see EquationService#remove(int)
     */
    @Test
    void remove() {
        EQUATIONS.remove(0);

        assertTrue(service.remove(1));
        assertIterableEquals(EQUATIONS, service.getAll());

        assertFalse(service.remove(1));
        assertIterableEquals(EQUATIONS, service.getAll());

        assertFalse(service.remove(-10));
        assertIterableEquals(EQUATIONS, service.getAll());
    }
}