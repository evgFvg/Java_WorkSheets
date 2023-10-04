import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {

    private static final String jdbcURL = "jdbc:mysql://localhost:3306/companies";
    private static final String username = "evg";
    private static final String password = "123456";


    public static void main(String[] args) throws SQLException {
        Connection connection = DriverManager.getConnection(jdbcURL, username, password);
        ProductsCRUD pCRUD = new ProductsCRUD(connection);
        CompaniesCRUD cCrud = new CompaniesCRUD(connection);
//        int rowsAffected = pCRUD.deleteRecordByID(10);
//        System.out.println(rowsAffected);
//        int prodID = pCRUD.insertProductRecord("Xiaomi 17", 1524.45, 4);
//        int prodID = pCRUD.insertProductRecord("Samsung 5", 456.555, 8);
//        System.out.println(prodID);
//        int res = pCRUD.deleteRecordByCondition("produdct_name", "Samgsung 21");
//        System.out.println(res);
//        int res2 = pCRUD.deleteRecordByCondition("price", "19.99");
//        System.out.println(res2);

//        pCRUD.updateRecord(61, 64.66);
//        pCRUD.updateRecord(61, 7);
//        pCRUD.updateRecord(61, "Hello world");
//        pCRUD.selectAllProducts();
//        pCRUD.selectByID(24);

//        int re = cCrud.insertCompanyRecord("IO_team", "Hertzl 44", "infoIO@io.com", "3333-444-5");
//        System.out.println(re);
//        int gg = cCrud.updateCompanyRecord(9, "address", "Saharov 5");
//        System.out.println(gg);
//        gg = cCrud.updateCompanyRecord(9, "company_name", "IBM");
//        System.out.println(gg);
//        int gg = cCrud.deleteRecordByID(9);
//        System.out.println(gg);
//         int re = cCrud.insertCompanyRecord("DOK_team", "Weizman 44", "infoIO@io.com", "3333-4784-5");
//        System.out.println(re);
//
//        int kk = cCrud.deleteRecordByColumn("contact_email", "infoIO@io.com");
//        System.out.println(kk);
//        cCrud.selectAllCompanies();
//        cCrud.selectByID(6);
//        cCrud.selectByColumnValue("company_name", "NASA");
    }
}



