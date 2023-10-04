import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class ProductsCRUD {
    private final Connection connection;

    public ProductsCRUD(Connection connection) {
        this.connection = connection;
    }

    public int insertProductRecord(String product_name, double price, int company_id) throws SQLException {
        String insertQuery =
                "INSERT INTO Products (product_name, price, company_id)\n" +
                        " VALUES (?, ?, ?)";
        int newProductID = 0;

        try (PreparedStatement pStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pStatement.setString(1, product_name);
            pStatement.setDouble(2, price);
            pStatement.setInt(3, company_id);

            if (pStatement.executeUpdate() > 0) {
                ResultSet resSet = pStatement.getGeneratedKeys();

                if (resSet.next()) {
                    newProductID = resSet.getInt(1);
                }
            }
            return newProductID;
        }
    }

    public <T> int updateRecord(int product_id, T newValue) throws SQLException {
        PreparedStatement pStatement = null;

        if (newValue instanceof String) {
            pStatement = initStatement("product_name");
            pStatement.setString(1, (String) newValue);
        } else if (newValue instanceof Integer) {
            pStatement = initStatement("company_id");
            pStatement.setInt(1, (Integer) newValue);
        } else if (newValue instanceof Double) {
            pStatement = initStatement("price");
            pStatement.setDouble(1, (Double) newValue);
        } else {
            throw new SQLException("Received unknown column type");
        }

        pStatement.setInt(2, product_id);
        int rowAffected = pStatement.executeUpdate();
        pStatement.close();

        return rowAffected;
    }

    private PreparedStatement initStatement(String column) throws SQLException {
        String updateQueryTemplate =
                "UPDATE Products SET " +
                        "%s = ?" +
                        " WHERE product_id = ?";
        String updateNameQuery = String.format(updateQueryTemplate, column);
        return connection.prepareStatement(updateNameQuery);
    }

    public int deleteRecordByID(int id) throws SQLException {
        String deleteQuery =
                "DELETE FROM Products " +
                        "WHERE product_id = " + id;
        try (PreparedStatement pStatement = connection.prepareStatement(deleteQuery)) {
            return pStatement.executeUpdate();
        }
    }

    public <T> int deleteRecordByCondition(String column, T condition) throws SQLException {
        if (!checkColumnValidity(column)) {
            return 0;
        }

        String deleteQuery =
                "DELETE FROM Products " +
                        "WHERE " + column + " = ?";

        try (PreparedStatement pStatement = connection.prepareStatement(deleteQuery)) {
            if (condition instanceof String) {
                pStatement.setString(1, (String) condition);
            } else if (condition instanceof Integer) {
                pStatement.setInt(1, (Integer) condition);
            } else if (condition instanceof Double) {
                pStatement.setDouble(1, (Double) condition);
            } else {
                throw new SQLException("Received unknown condition type");
            }

            return pStatement.executeUpdate();
        }
    }

    private boolean checkColumnValidity(String column) throws SQLException {
        if (column == null) {
            throw new NullPointerException("Null column is not valid");
        }

        List<String> columnsList = new ArrayList<>();
        DatabaseMetaData metadata = connection.getMetaData();
        ResultSet columnResSet = metadata.getColumns(null, null, "Products", null);

        while (columnResSet.next()) {
            String columnName = columnResSet.getString("COLUMN_NAME");
            columnsList.add(columnName);
        }
        if(columnsList.contains(column)) {
            return true;
        }
        throw new SQLException("Column " + column + " doesn't exist");
    }

    public void selectAllProducts() throws SQLException {
        printSelectQuery("SELECT * FROM Products");
    }

    private void printSelectQuery(String sqlQuery) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlQuery);

        while (resultSet.next()) {
            int id = resultSet.getInt("product_id");
            String name = resultSet.getString("product_name");
            double price = resultSet.getDouble("price");
            int company_id = resultSet.getInt("company_id");

            System.out.print("ID: " + id + "\t");
            System.out.print(" Name: " + name + "\t");
            System.out.print(" Price: " + price + "\t");
            System.out.print(" Company_id: " + company_id + "\t");
            System.out.println();
        }
    }

    public void selectByID(int id) throws SQLException {
        printSelectQuery("SELECT * FROM Products WHERE product_id = " + id);
    }
}