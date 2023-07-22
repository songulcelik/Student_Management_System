import javax.sound.midi.Soundbank;
import java.util.Scanner;

public class Runner {
    public static void main(String[] args) {
        //1.uygulama işlemleri için menü olmalı
        //2.Student Classı Oluşturma
        start();

    }

    private static void start() {
        Scanner input= new Scanner(System.in);
        System.out.println("Student Management System Uygulamamıza Hosgeldiniz");

        StudentService service= new StudentService();
        service.createTable();
        int select;
        do {
            System.out.println("==========================");
            System.out.println("öGRENCİ YÖNETİM PANELİ");
            System.out.println("1-Öğrenci Kayıt");
            System.out.println("2-Öğrencileri Listele");
            System.out.println("3-Öğrenci Güncelle");
            System.out.println("4-Öğrenci Sil");
            System.out.println("5-Öğrenci Bul");
            System.out.println("6-Ad veya Soyad ile Öğrenci Filtrele");
            System.out.println("0-ÇIKIŞ");
            System.out.print("İslem seciniz: ");
            select= input.nextInt();
            input.nextLine();   //Kullanıcının string girmesi durumunu tolere etmek için
            int id;
            switch (select){
                case 1:
                    //öğrenci kayıt
                    service.saveStudent();
                    break;
                case 2:
                    //öğrenci listele
                    service.getAllStudents();
                    break;
                case 3:
                    //öğrenci güncelle
                    id=getID(input);
                    service.updateStudent(id);
                    break;
                case 4:
                    //öğrenci sil
                    id=getID(input);
                    service.delete(id);
                    break;
                case 5:
                    //öğrenci bul
                    id=getID(input);
                    Student student=service.getStudentById(id);
                    if(student==null){
                        System.out.println(id +" nolu öğrenci bulunamadı");
                    }else {
                        System.out.println(student);
                    }
                    break;
                case 6:
                    //öğrenci filtrele
                    service.listStudentsByNameOrLastName();
                    break;
                case 0:
                    System.out.println("Uygulamamızı kullandığınız için tesekkur ederiz. İYİ GÜNLER...");
                    break;
                default:
                    System.out.println("Hatalı bir giriş yaptınız, lütfen seçiminizi kontrol ediniz ");

            }
        }while(select!=0);
    }
    private static int getID(Scanner inp){
        System.out.print("Öğrenci ID: ");
        int id=inp.nextInt();
        inp.nextLine();
        return id;
    }
}
