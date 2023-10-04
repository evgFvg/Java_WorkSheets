import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompaniesCRUD {
    private final Connection connection;

    public CompaniesCRUD(Connection connection) {
        this.connection = connection;
    }

    public int insertCompanyRecord(String company_name, String address, String email, String phone) throws SQLException {
        String insertQuery =
                "INSERT INTO Company (company_name, address, contact_email, phone_number)\n" +
                        " VALUES (?, ?, ?, ?)";
        int newCompanyID = 0;

        try (PreparedStatement pStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, company_name);
            pStatement.setString(2, address);
            pStatement.setString(3, email);
            pStatement.setString(4, phone);

            if (pStatement.executeUpdate() > 0) {
                ResultSet resSet = pStatement.getGeneratedKeys();

                if (resSet.next()) {
                    newCompanyID = resSet.getInt(1);
                }
            }
            return newCompanyID;
        }
    }

    public int updateCompanyRecord(int company_id,  String column, String newValue) throws SQLException {
        if(!checkColumnValidity(column)) {
            return 0;
        }
        if(newValue == null) {
            throw new NullPointerException("Null newValue is not allowed");
        }
        String updateQueryTemplate =
                "UPDATE Company SET " +
                        "%s = ? WHERE company_id = %d";
        String updatedQuery = String.format(updateQueryTemplate, column, company_id);

        PreparedStatement pStatement =  connection.prepareStatement(updatedQuery);
        pStatement.setString(1, newValue);
        int rowAffected = pStatement.executeUpdate();
        pStatement.close();

        return rowAffected;
    }

    private boolean checkColumnValidity(String column) throws SQLException {
        if (column == null) {
            throw new NullPointerException("Null column is not valid");
        }

        List<String> columnsList = new ArrayList<>();
        DatabaseMetaData metadata = connection.getMetaData();
        try(ResultSet columnResSet = metadata.getColumns(null, null, "Company", null)) {

            while (columnResSet.next()) {
                String columnName = columnResSet.getString("COLUMN_NAME");
                columnsList.add(columnName);
            }

            if(columnsList.contains(column)) {
                return true;
            }

            throw new SQLException("Column " + column + " doesn't exist");
        }
    }

    public int deleteRecordByID(int id) throws SQLException {
        String deleteQuery =
                "DELETE FROM Company " +
                        "WHERE company_id = " + id;
        try (PreparedStatement pStatement = connection.prepareStatement(deleteQuery)) {
            return pStatement.executeUpdate();
        }
    }

    public int deleteRecordByColumn(String column, String columnValue) throws SQLException {
        if (!checkColumnValidity(column)) {
            return 0;
        }
        if(columnValue == null) {
            throw new NullPointerException("Null fields are not allowed");
        }

        String deleteQuery =
                "DELETE FROM Company " +
                        "WHERE " + column + " = ?";

        try (PreparedStatement pStatement = connection.prepareStatement(deleteQuery)) {
            pStatement.setString(1, columnValue);
            return pStatement.executeUpdate();
        }
    }

    public void selectByID(int id) throws SQLException {
        printSelectQuery("SELECT * FROM Company WHERE company_id = " + id);
    }

    public void selectAllCompanies() throws SQLException {
        printSelectQuery("SELECT * FROM Company");
    }

    public void selectByColumnValue(String column, String value) throws SQLException {
        if (!checkColumnValidity(column)) {
            return ;
        }
        if(value == null) {
            throw new NullPointerException("Null fields are not allowed");
        }
        String query =
                "SELECT * FROM Company WHERE " + column + " = '" + value + "'";
        printSelectQuery(query);
    }
    private void printSelectQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        while (resultSet.next()) {
            int id = resultSet.getInt("company_id");
            String name = resultSet.getString("company_name");
            String address = resultSet.getString("address");
            String email = resultSet.getString("contact_email");
            String phone = resultSet.getString("phone_number");

            StringBuilder sb = new StringBuilder("ID : " + id + "\t").
                    append("Company name : ").
                    append(name).
                    append("\t").
                    append(" Address : ").
                    append(address).
                    append("\t").
                    append(" Email : ").
                    append(email).
                    append("\t").
                    append(" Phone : ").
                    append(phone);
            System.out.println(sb);
        }

        resultSet.close();
        statement.close();
    }

}