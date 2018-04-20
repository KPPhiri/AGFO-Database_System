package Connect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class SQLConnecter {
    public static Connection connect() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_team_14",
                    "cs4400_team_14",
                    "zmaGa96X");
            if(!con.isClosed()) System.out.println("Successfully connected to " +
                    "MySQL server using TCP/IP...");
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
       } //finally {
//            try {
//                if(con != null)
//                    con.close();
//            } catch(SQLException e) {}
//        }
        return con;
    }

}
