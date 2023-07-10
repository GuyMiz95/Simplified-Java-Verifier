package main.sJavaDataTypes;

import java.util.Vector;

/**
 * Class representing a Method under the SJava specifications
 */
public class SJavaMethod {
    /**
     * Name of the method
     */
    private final String name;
    /**
     * Ordered parameter list associated with the method
     */
    private final Vector<SJavaVariable> params;

    /**
     * Constructor for a new SJavaMethod
     * @param name Name of the method
     * @param params Ordered parameter list associated with the method
     */
    public SJavaMethod(String name, Vector<SJavaVariable> params) {
        this.name = name;
        this.params = params;
    }

    /**
     * @return Name associated with the specific SJava Method
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return Parameters associated with the specific SJava Method
     */
    public Vector<SJavaVariable> getParams() {
        return this.params;
    }
}
