import javax.print.DocFlavor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//4- DATABASE ile iletişim içinde olan class oluşturalım
//connection, statement, prepared statement
public class StudentRepository {
    private Connection con;
    private Statement st;
    private PreparedStatement prSt;


    //5.connection için method oluştur
    private void getConnection(){
        try {
            this.con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/jdbc_db","dev_user","password");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //6. statement içn method oluştur
    private void getStatement(){
        try {
            this.st= con.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //7. PreparedStatement için method oluştur
    private void getPreparedStatement(String sql){
        try {
            this.prSt= con.prepareStatement(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //8. tablo oluşturma
    public void createTable(){
        getConnection();
        getStatement();

        try {
            st.execute("CREATE TABLE IF NOT EXISTS t_student(id serial,name varchar(50), " +
                    "lastname VARCHAR(50),  city VARCHAR(50), age int)");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                st.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //12- tabloya veri ekleme
    public void save(Student newStd) {
        getConnection();
        String sql="INSERT INTO t_student(name,lastname, city,age) VALUES(?,?,?,?) ";
        getPreparedStatement(sql);
        try {
            prSt.setString(1, newStd.getName());
            prSt.setString(2, newStd.getLastName());
            prSt.setString(3, newStd.getCity());
            prSt.setInt(4,newStd.getAge());
            prSt.executeUpdate();
            System.out.println("Kayıt işlemi basarılı bir sekilde gerçekleşmiştir...");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                prSt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }
    //14- tablo tüm kayıtları listeleme
    public void findAll() {
        getConnection();
        getStatement();
        String sql="SELECT * FROM t_student";
        try {
            ResultSet resultSet= st.executeQuery(sql);
            System.out.println("+"+"-".repeat(80)+"+");
            System.out.printf("%-5s | %-20s | %-20s | %-20s | %-5s\n","id","ad","soyad","sehir","yas");
            while(resultSet.next()){
                System.out.printf("%-5s | %-20s | %-20s | %-20s | %-5s\n",
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("lastname"),
                        resultSet.getString("city"),
                        resultSet.getInt("age"));
            }
            System.out.println("+"+"-".repeat(80)+"+");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                st.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    //16- tablodan veri silme
    public void delete(int id) {
        getConnection();
        String sql= "DELETE FROM t_student WHERE id=?";
        getPreparedStatement(sql);

        try {
            prSt.setInt(1,id);
            int deleted= prSt.executeUpdate();
            if (deleted>0){
                System.out.println("id: "+id+" olan kayıt silinmiştir...");
            }else{
                System.out.println("id: "+ id+ " seklinde bir kayıt bulunamadı");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                st.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }
    //18 id ile tek kayıt dönme
    public Student findStudentById(int id) {
        Student student=null;
        getConnection();
         /* alternatif çözüm:
         String sql="SELECT * FROM t_student WHERE id= "+id;
         getStatement();
         st.executeQuery(sql);
          */
        String sql="SELECT * FROM t_student WHERE id= ? ";
        getPreparedStatement(sql);
        try {
            prSt.setInt(1,id);
            ResultSet resultSet= prSt.executeQuery();
            if (resultSet.next()){
                student=new Student();
                student.setId(resultSet.getInt("id"));
                student.setName(resultSet.getString("name"));
                student.setLastName(resultSet.getString("lastname"));
                student.setCity(resultSet.getString("city"));
                student.setAge(resultSet.getInt("age"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                prSt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return student;
    }
    //20- databasede veri güncelleme
    public void update(Student student) {
        getConnection();
        String sql= "UPDATE t_student SET name=?, lastname=?, city=?, age=? WHERE id= ?"; //playsholder deniyor
        getPreparedStatement(sql);

        try {
            prSt.setString(1, student.getName());
            prSt.setString(2,student.getLastName());
            prSt.setString(3,student.getCity());
            prSt.setInt(4,student.getAge());
            prSt.setInt(5,student.getId());
            int updated= prSt.executeUpdate();
            if(updated>0){
                System.out.println("Güncelleme işlemi basarı ile gerçekleşti...");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                prSt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

    }
    //22-name veya lastname sütununa girilen kelimeleri içeren kayıtların dönmesi
    public List<Student> findStudentByNameOrLastName(String nameOrLastname) {
        List<Student> list=new ArrayList<>();
        getConnection();
        String searched="%"+nameOrLastname+"%";   //aranan kelime ay ise searched=%ay%
        String sql="select * from t_student where name ilike ? or lastname ilike ?";
        getPreparedStatement(sql);
        try {
            prSt.setString(1,searched);
            prSt.setString(2,searched);
            ResultSet rs=prSt.executeQuery();
            while(rs.next()){
                Student student=new Student();
                student.setId(rs.getInt("id"));
                student.setName(rs.getString("name"));
                student.setLastName(rs.getString("lastname"));
                student.setCity(rs.getString("city"));
                student.setAge(rs.getInt("age"));
                list.add(student);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        finally{
            try {
                prSt.close();
                con.close();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return list;
    }


}
