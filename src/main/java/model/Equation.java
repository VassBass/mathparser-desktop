package model;

import java.util.Objects;

/**
 * Table in DB:
 * sqlite = equations
 */
public class Equation {

    /**
     * Field in DB:
     * sqlite = id
     */
    private int id;

    /**
     * Field in DB:
     * sqlite = equation
     */
    private String equation = "4.20";

    /**
     * Field in DB:
     * sqlite = result
     */
    private double result = 4.20;

    public void setId(int id){this.id = id;}
    public void setEquation(String equation){this.equation = equation;}
    public void setResult(double result){this.result = result;}

    public int getId(){return id;}
    public String getEquation(){return equation;}
    public double getResult(){return result;}

    @Override
    public int hashCode() {
        return Objects.hash(id,equation,result);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) return false;
        if (obj == this) return true;

        Equation o = (Equation) obj;
        return o.getId() == this.id
                && o.getEquation().equals(this.equation)
                && o.getResult() == this.result;
    }

    /**
     * @return equation object in json
     */
    @Override
    public String toString() {
        return "{\n"
                + "\t\"Equation\":{\n"
                + "\t\t\"id\": " + this.id + ",\n"
                + "\t\t\"equation\": \"" + this.equation + "\",\n"
                + "\t\t\"result\": " + this.result + "\n"
                + "\t}\n"
                + "}";
    }
}
