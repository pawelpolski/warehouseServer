package pawelpolski;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by macbook on 01.05.2017.
 */
public class Model {

    /**
     * Equipments list which can be displayed in JavaFX's TableView
     */
    private ArrayList<Equipment> equipments = new ArrayList();


    /**
     * Stores equipments types in array list
     */
    private ArrayList<String> equipmentsTypes = new ArrayList<>();

    Connection c = null;


    public Model() throws IOException {
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:warehouseDataBase.db");


        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");

        addTypesOfEquipmentsToList();
        equipments = getEquipments();
    }

    public void reset() {
        Statement stmt = null;

        try {
            stmt = c.createStatement();
            String sql;
            try {
                sql = "DROP TABLE EQUIPMENTS";
                stmt.execute(sql);
            } catch (Exception ex) {

            }
            sql = "CREATE TABLE EQUIPMENTS" +
                    "(ID INT    NOT NULL," +
                    " SERIAL           TEXT    NOT NULL, " +
                    " TYPE            TEXT     NOT NULL, " +
                    " STATUS       TEXT    NOT NULL)";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Table created successfully");
        equipments = new ArrayList<>();
    }
    /**
     * add fixed types of equipments to array list
     */
    public void addTypesOfEquipmentsToList() {
        Statement stmt = null;
        ArrayList<Equipment> l = new ArrayList();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM TYPES;");

            while (rs.next()) {
                equipmentsTypes.add(rs.getString("type"));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Initialization types done successfully");

    }

    public void updateEquipment(Equipment e) {
        Statement stmt = null;

        try {
            stmt = c.createStatement();
            String sql = "UPDATE EQUIPMENTS set SERIAL= '" + e.getSerialNumber() + "', TYPE='" + e.getType() + "' where ID=" + e.getId() + ";";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (Exception ex) {
            System.err.println(e.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        System.out.println("Updated successfully");

    }




    /**
     * Method allows to add new Equipment to list
     */

    public ArrayList<Equipment> addEquipment(Equipment e) {
        int max = 0;
        for (int i = 0; i < equipments.size(); i++) {
            if (equipments.get(i).getId() > max) {
                max = equipments.get(i).getId();
            }
        }
        e.setId(max + 1);
        equipments.add(e);
        Statement stmt = null;

        try {
            stmt = c.createStatement();
            String sql = "INSERT INTO EQUIPMENTS VALUES (" + e.getId() + ", '" + e.getSerialNumber() + "', '" + e.getType() + "', '" + e.getStatus() + "')";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (Exception ex) {
            System.err.println(e.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        System.out.println("Records created successfully");

        return equipments;
    }



    /**
     * Method removes row from Equipments list
     *
     * @param index index of equipment which is going to be removed
     * @return returns updeted list, without removed row
     */
    public ArrayList<Equipment> removeEquipment(int index) {
        Equipment e = equipments.remove(index);

        Statement stmt = null;

        try {
            stmt = c.createStatement();
            String sql = "DELETE from EQUIPMENTS where ID=" + e.getId() + ";";
            stmt.executeUpdate(sql);

            stmt.close();
        } catch (Exception ex) {
            System.err.println(e.getClass().getName() + ": " + ex.getMessage());
            System.exit(0);
        }
        System.out.println("Removed successfully");

        return equipments;
    }


    public ArrayList<Equipment> getEquipments() {
        Statement stmt = null;
        ArrayList<Equipment> l = new ArrayList();
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM EQUIPMENTS;");

            while (rs.next()) {
                int id = rs.getInt("id");
                String serial = rs.getString("serial");
                String type = rs.getString("type");
                String status = rs.getString("status");

                l.add(new Equipment(id, serial, type, status));
            }
            rs.close();
            stmt.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Equipments initialized successfully");
        equipments = l;
        return l;
    }

    /*public void setEquipments(ArrayList<Equipment> equipments) {
        this.equipments = equipments;
    }*/

    public ArrayList<String> getEquipmentsTypes() {
        return equipmentsTypes;
    }

    public void setEquipmentsTypes(ArrayList<String> equipmentsTypes) {
        this.equipmentsTypes = equipmentsTypes;
    }
}
