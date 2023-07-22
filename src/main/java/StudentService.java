import java.util.List;
import java.util.Scanner;

//3- student ile ilgili methodlar için StudentService classi oluşturuldu
public class StudentService {

    //9. reponun methodlarını kullanmak için obje oluştur==> injection
    Scanner input=new Scanner(System.in);
    StudentRepository repository= new StudentRepository();

    //10. tablo oluşturmak için method
    public void createTable(){
        repository.createTable();
    }

    //11. öğrenci kaydetme

    public void saveStudent(){
        System.out.print("Öğrenci Adı: ");
        String name=input.nextLine().trim();
        System.out.print("Öğrenci Soyadı: ");
        String lastName=input.nextLine().trim();
        System.out.print("Öğrenci Sehri: ");
        String city=input.nextLine().trim();
        System.out.print("Öğrenci Yaşı: ");
        int age=input.nextInt();
        input.nextLine();
        Student newStd=new Student(name,lastName,city,age);
        repository.save(newStd);  // olması için create yapıldı repository de save methodu oluşturduk
    }

    //13-tüm öğrencileri listele
    public void getAllStudents(){
        repository.findAll();  // olması için create yapıldı repository de findALL methodu oluşturduk
    }



    //15- öğrenci silme
    public void delete(int id){
        repository.delete(id);
    }

    //17-id ile öğrenciyi getirme
    public Student getStudentById(int id){
        Student student= repository.findStudentById(id);
        return student;
    }

    //19- öğrenci güncelleme
    public void updateStudent(int id){
        Student student= getStudentById(id);
        if(student==null){
            System.out.println("Öğrenci bulunamadı!!!");
        }else{
            System.out.print("Öğrenci Adı: ");
            String name=input.nextLine().trim();
            System.out.print("Öğrenci Soyadı: ");
            String lastName=input.nextLine().trim();
            System.out.print("Öğrenci Sehri: ");
            String city=input.nextLine().trim();
            System.out.print("Öğrenci Yaşı: ");
            int age=input.nextInt();
            input.nextLine();

            //yeni değerlerle fieldları güncelle
            student.setName(name);
            student.setLastName(lastName);
            student.setCity(city);
            student.setAge(age);
            repository.update(student);

        }
    }
    //21-girilen kelime ad veya soyad da var olan studentları listele
//kelime ay--> ad veya soyad da ay içerenler gelecek
    public void listStudentsByNameOrLastName(){
        System.out.println("Ad veya soyad");
        String nameOrLastname=input.nextLine();
        //birden fazla kayıt dönebilir
        List<Student> studentList=repository.findStudentByNameOrLastName(nameOrLastname);
        //listedeki öğrencileri yazdıralım
        //list boş ise
        if(studentList.size()==0){
            System.out.println("Öğrenci bulunamadı...");
        }else{
            studentList.forEach(System.out::println);
        }
    }


}
