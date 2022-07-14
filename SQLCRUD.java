package com.srccodes;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


//Each table gets its own SQLCRUD after it is created
public class SQLCRUD implements CRUD<String,String>{
    private String table = "";
    private List<String> pk = new ArrayList<>();
    private List<Integer> indPK = new ArrayList<>();
    private int numberPK = 0;
    private int numOfColumns = 0;
    private String columnsNames = "";
    Connection con;

    //ctor
    public SQLCRUD(String url, String username, String password, String table) throws ClassNotFoundException, SQLException {
        this.table = table;

        /* create connection */
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection(url, username, password);

        /* find columns names */
        columnsNames = findColumnNames();
        numOfColumns = columnsNames.split(",").length;
        String[] columnsNamesSplit = columnsNames.split(",");

        /* find the primary key */
        DatabaseMetaData dbMetaData = con.getMetaData();
        ResultSet primaryKey = dbMetaData.getPrimaryKeys(null , null, table);

        while(primaryKey.next()){
            int i = 0;
            String temp = primaryKey.getString("COLUMN_NAME");
            pk.add(temp);
            for(; i < numOfColumns && !columnsNamesSplit[i].equals(temp); ++i){}
            indPK.add(i);
        }
        numberPK = pk.size();

    }

    @Override
    public void close() throws Exception {
        con.close();
    }

    @Override
    public String create(String data) throws IOException {
        String query = "INSERT INTO " + table + " (" + columnsNames + ") "
                + "VALUES(" + data + ")";

        executeUpdateStatement(query);

        String[] splitData = data.split(",");
        String primaryKey = "";
        for(int i = 0; i < numberPK ;++i){
            primaryKey += splitData[indPK.get(i)];
            if(i + 1 != numberPK) {
            	primaryKey += " ";
            }
        }

        return primaryKey;
    }

    @Override
    public String read(String key) throws IOException {
        String query = "SELECT * FROM " + table + " WHERE " + whereCondition(key) + ";";
        return executeQueryStatement(query);
    }

    @Override
    public void update(String key, String data) throws IOException {
        String[] columnsNamesSplit = columnsNames.split(",");
        String[] dataSplit = data.split(",");

        /* create query */
        String query = "UPDATE " + table + " SET ";
        for(int i = 0; i < numOfColumns; ++i){
            query += columnsNamesSplit[i] + "=" + dataSplit[i];
            if(numOfColumns-i > 1){
                query += ",";
            }
        }
        query += " WHERE " + whereCondition(key) + ";";

        executeUpdateStatement(query);
    }

    @Override
    public void delete(String key) throws IOException {
        String query = "DELETE FROM " + table + " WHERE " + whereCondition(key) + ";";
        executeUpdateStatement(query);
    }


    private String findColumnNames(){
        String columnsNames = "";
        String qu = "SELECT group_concat(column_name order by ordinal_position) FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = 'Employees' AND TABLE_NAME = 'EmpDetail';";
        try (Statement st1 = con.createStatement()) {
            ResultSet s1 = st1.executeQuery(qu);
            while(s1.next()){
                columnsNames = s1.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columnsNames;
    }
    private void executeUpdateStatement(String query){
        Statement st;

        try {
            st = con.createStatement();
            st.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String executeQueryStatement(String query){
        Statement st;
        ResultSet rs;
        String retValue = "";
        try {
            st = con.createStatement();
            rs = st.executeQuery(query);
            while(rs.next()){
                for(int i=0; i < numOfColumns; ++i){
                    retValue += rs.getString(i+1) + " ";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return retValue;
    }
    private String whereCondition(String key){
        String[] splitKey = key.split(" ");
        String query = "";
        for(int i = 0; i < numberPK; ++i){
            query += pk.get(i) + "=" + splitKey[i];
            if(numberPK-i > 1){
                query += " AND ";
            }
        }
        return query;
    }
}
