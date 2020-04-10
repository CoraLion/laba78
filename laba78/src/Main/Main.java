package Main;


import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    private List<Student> stud = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Main prog = new Main();
        prog.run();
    }

    private void run() {
        int sel;
        while((sel=menu())!= 0) {
            switch (sel) {
                case 1:
                    stud = readfromfile();
                    break;
                case 2:
                   writetofile(stud);
                   break;
                    case 3:
                    printlist(stud);
                    break;
                case 4:
                   writetoBinary("file.dat");
                    break;
                case 5:
                    stud = readtoBinary("file.dat");
                    break;
                case 6:
                    System.out.println("Введите студента: ");
                    int id = scanner.nextInt();
                    String secname = scanner.next();
                    String name = scanner.next();
                    String threename = scanner.next();
                    int day = scanner.nextInt();
                    int month = scanner.nextInt();
                    int year = scanner.nextInt();
                    String address = scanner.next();
                    int mob = scanner.nextInt();
                    String fac = scanner.next();
                    int curs = scanner.nextInt();
                    int group = scanner.nextInt();
                    Student addst = new Student(id, secname, name, threename, LocalDate.of(year, month, day), address, mob, fac, curs, group);
                    addelement(stud, addst);
                    break;
                case 7:
                   System.out.println("Введите айди удаляемого студента: ");
                    int id1 = scanner.nextInt();
                    Student st = findid(stud, id1);
                    if (st!=null) deleteelement(stud, st);
                    break;
                case 8:
                    System.out.println("Введите факультет: ");
                    String fac1 = scanner.next();
                    List<Student> findfac = fac(stud, fac1);
                    printlist(findfac);
                    break;
                case 9:
                    System.out.println("Введите год: ");
                    int year1 = scanner.nextInt();
                    List<Student> findgod =god(stud, year1);
                    printlist(findgod);
                    break;
                case 10:
                    System.out.println("Введите группу: ");
                    int group1 = scanner.nextInt();
                    List<Student> findgroup = group(stud, group1);
                    printlist(findgroup);
                    break;
                case 11:
                    List<String> facultet = findAllFac(stud);
                    printfac(facultet);
                    break;
                case 12:
                    Map<String, Integer> map = countStudent(stud);
                    printMap(map);
                    break;
                case 13:
                    stud.sort(Comparator.comparing(Student::getFac).thenComparing(Student::getAge));
                    printlist(stud);
                    break;
            }
        }
    }

    private List<Student> group(List<Student> stud, int group1) {
        List<Student> list = new ArrayList<>();
        for (Student student : stud) {
            if (student.getGroup()==group1) {
                list.add(student);
            }
        }
        list.sort(Comparator.comparing(Student::getSecname));
        return list;
    }

    private Map<String, List<Student>> facalfavit(List <Student> stud) {
        Map<String, List<Student>> map = new HashMap<>();
        for (Student student : stud) {
            String st = student.getFac();
            List<Student> list = map.get(st);
            if (list == null) {
                list = new ArrayList<>();
                map.put(st, list);
            }
            list.add(student);
        }
        return map;
    }

    private Map<String, Integer> countStudent(List<Student> stud) {
        Map<String, Integer> map = new HashMap<>();
        for (Student student : stud) {
            String fac = student.getFac();
            Integer count = map.get(fac);
            if(count==null) count = 0;
            map.put(fac, count+1);
        }
        return map;
    }

    private void printMap(Map<String, Integer> map) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private List<String> findAllFac(List<Student> stud) {
        Set<String> fac = new LinkedHashSet<>();
        for (Student student : stud) {
            fac.add(student.getFac());
        }
        return new ArrayList<>(fac);
    }

    private void printfac(List<String> facultet) {
        for (String fac : facultet) {
            System.out.println(fac);
        }
    }


    private List<Student> addelement(List<Student> stud, Student addst) {
        stud.add(addst);
        return stud;
    }

    private void deleteelement(List<Student> stud, Student st) {
        stud.remove(st);
    }

    private Student findid(List<Student> stud, int id1) {
        for (Student student : stud) {
            if(student.getId() == id1){
                return student;
            }
        }
        return null;
    }

    private List<Student> god(List<Student> stud, int year) {
        List<Student> list = new ArrayList<>();
        for (Student student : stud) {
            if(student.getAge().isAfter(LocalDate.of(year, 12, 31))) {
                list.add(student);
            }
        }
        return list;
    }

    private List<Student> fac(List<Student> stud, String fac) {
        List<Student> list = new ArrayList<>();
        for (Student student : stud) {
            if (student.getFac().equals(fac)) {
                list.add(student);
            }
        }
        return list;
    }

    private List<Student> readtoBinary(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                return (List<Student>) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
                return Collections.emptyList();
            }
    }

    private void writetoBinary(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(stud);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printlist(List<Student> stud) {
        System.out.println("Student: ");
        for (Student student : stud) {
            System.out.println(student);
        }
        System.out.println("________");
    }

    private List<Student> readfromfile() {
        List<Student> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("student.txt"))) {
            String line;
            while((line = reader.readLine()) != null) {
                String[] s = line.split(" ");
                int id = Integer.parseInt(s[0]);
                String secname = s[1];
                String name = s[2];
                String threename = s[3];
                int day = Integer.parseInt(s[4]);
                int month = Integer.parseInt(s[5]);
                int year = Integer.parseInt(s[6]);
                String address = s[7];
                int mob = Integer.parseInt(s[8]);
                String fac = s[9];
                int curs = Integer.parseInt(s[10]);
                int group = Integer.parseInt(s[11]);
                Student st = new Student(id, secname, name, threename, LocalDate.of(year, month, day), address, mob, fac, curs, group);
                list.add(st);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void writetofile(List<Student> stud) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d M yyyy");
        try (PrintWriter printw = new PrintWriter("student1.txt")) {
            for (Student student : stud) {
                printw.print(student.getId() + " " + student.getSecname() + " " + student.getName() + " "
                        + student.getThreename() + " ");
                printw.print(formatter.format(student.getAge()) + " ");
                printw.print(student.getAddress() + " " + student.getMob() + " " + student.getFac() + " "
                        + student.getCurs() + " " + student.getGroup() + "\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    private int menu() {
        System.out.println("---------------Меню-----------------");
        System.out.println("Нажмите 1, чтобы прочитать файл");
        System.out.println("Нажмите 2, чтобы записать в файл");
        System.out.println("Нажмите 3, чтобы вывести список на экран");
        System.out.println("Нажмите 4, чтобы записать в бинарный файл");
        System.out.println("Нажмите 5, чтобы прочитать бинарный файл");
        System.out.println("Нажмите 6, чтобы добавить элемент в список");
        System.out.println("Нажмите 7, чтобы удалить элемент из списка");
        System.out.println("Нажмите 8, чтобы вывести список студентов заданного факультета");
        System.out.println("Нажмите 9, чтобы вывести список студентов, родившихся после заданного года");
        System.out.println("Нажмите 10, чтобы вывести список студентов заданной группы");
        System.out.println("Нажмите 11, чтобы вывести список всех факультетов");
        System.out.println("Нажмите 12, чтобы вывести количестно студентов на факультетах");
        System.out.println("Нажмите 13, чтобы вывести список студентов за алфавитным порядком факультета");
        System.out.println("Нажмите 0, чтобы выйти");
        return new Scanner(System.in).nextInt();
    }
}
