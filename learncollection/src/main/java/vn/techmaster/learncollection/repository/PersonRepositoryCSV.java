package vn.techmaster.learncollection.repository;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import org.springframework.stereotype.Repository;
import org.springframework.util.ResourceUtils;

import vn.techmaster.learncollection.model.Person;

import static java.util.stream.Collectors.*;

@Repository
public class PersonRepositoryCSV implements PersonRepositoryInterface {
//  @Value("${csvFile}")
//  private String csvFile;

  private ArrayList<Person> people= new ArrayList<>();


  public PersonRepositoryCSV() {
    loadData("person.csv");
  }

  private void loadData(String csvFile) {
    try {
      File file = ResourceUtils.getFile("classpath:static/" + csvFile);
      CsvMapper mapper = new CsvMapper(); // Dùng để ánh xạ cột trong CSV với từng trường trong POJO
      CsvSchema schema = CsvSchema.emptySchema().withHeader(); // Dòng đầu tiên sử dụng làm Header
      ObjectReader oReader = mapper.readerFor(Person.class).with(schema); // Cấu hình bộ đọc CSV phù hợp với kiểu
      Reader reader = new FileReader(file);
      MappingIterator<Person> mi = oReader.readValues(reader); // Iterator đọc từng dòng trong file
      while (mi.hasNext()) {
        Person person = mi.next();
        people.add(person);
      }
    } catch (IOException e) {
      System.out.println(e);
    }
  }

  @Override
  public List<Person> getAll() {
    return people;
  }

  @Override
  public List<Person> sortPeopleByFullNameReversed() {
    return people.stream().sorted(Comparator.comparing(Person::getFullname).reversed()).collect(Collectors.toList());
  }

  @Override
  public List<String> getSortedCities() {
    /*return people.stream().
    sorted(Comparator.comparing(Person::getCity)).
    map(Person::getCity).collect(Collectors.toList());*/

    return people.stream().
    map(Person::getCity).
    sorted().collect(Collectors.toList());

  }

  @Override
  public List<String> getSortedJobs() {
    return people.stream().map(Person::getJob)
            .sorted().collect(Collectors.toList());
  }

  @Override
  public HashMap<String, Integer> findTop5Citis() {
    //Tạo một Hashmap<String,Integer> tên là map
    HashMap<String,Integer> map=new HashMap<>();

    //add vào map: key=city, value=số lượng city
    people.stream().collect(groupingBy(Person::getCity,counting()))
            .forEach((key,value)-> {
              map.put(key, Math.toIntExact(value));
            });

    //Tạo một HashMap tên result gán bằng map nhưng được sắp xếp theo thứ tự value giảm dần và chỉ lấy 5 căp giá trị đầu tiên
    HashMap<String,Integer> result= map.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    return result;
  }

  //Làm tương tự như findTop5Citis
  @Override
  public HashMap<String, Integer> findTop5Jobs() {
    Map<String,Integer> map=new HashMap<>();

    people.stream().collect(groupingBy(Person::getJob,counting()))
          .forEach((key,value)-> {
            map.put(key, Math.toIntExact(value));
          });

    HashMap<String,Integer> result= map.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    return result;
  }

  @Override
  public HashMap<String, String> findTopJobInCity() {
    //Tạo một Map tên 'map': key=city,value= một Map khác với key=các job trong city, value=số lượng job đó trong city.
    Map<String, Map<String,Long>> map=people.stream().collect(groupingBy(Person::getCity,groupingBy(Person::getJob,counting())));

    //Tạo hashmap result, cái này sẽ được return
    HashMap<String,String> result=new HashMap<>();

    //Tạo List các Map với tên là 'list' lấy ra các value Map của biến 'map' phía trên. Giờ cần lấy ra các top job là xong
    List<Map<String,Long>> list=map.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

    //Tạo list các top job có tên 'listJob'
    List<String> listJob=new ArrayList<>();

    //Duyệt 'list', lấy ra top job trong mỗi Map rồi add vào 'listJob'
    for(Map<String,Long> m:list){
      listJob.addAll(m.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
              .limit(1).map(Map.Entry::getKey).collect(Collectors.toList()));
    }

    //add vào 'result':key = giá trị key của 'map', value= phần tử trong 'listJob' (duyệt song hành với nhau để có giá trị tương ứng)
    for(int i=0;i<map.size();){
      for (Map.Entry<String,Map<String,Long>> m1:map.entrySet()) {
        result.put(m1.getKey(),listJob.get(i));
        i++;
      }
    }

    return result;
  }

  //Dùng stream để group thành Map chứa key=tên job,val=số lượng job và tạo Map khác để đổi giá trị long thành int.
  @Override
  public HashMap<String, Integer> groupJobByCount() {
    Map<String, Long> map=people.stream().collect(groupingBy(Person::getJob, counting()));
    HashMap<String,Integer> result=new HashMap<>();
    map.forEach((key,value)->result.put(key,Integer.valueOf(String.valueOf(value))));
    return result;
  }


  @Override
  public HashMap<String, List<Person>> groupPeopleByCity() {
//    HashMap<String,List<Person>> map=new HashMap<>();
//    Set<String> cities=people.
//            stream().map(Person::getCity).collect(Collectors.toSet());
//    for (String city:cities){
//      map.put(city,people.stream().filter(b->b.getCity().equals(city))
//              .collect(Collectors.toList()));
//    }
    return (HashMap<String,List<Person>>)people.stream().collect(groupingBy(Person::getCity,Collectors.toList()));
  }


  @Override
  public List<Person> sortPeopleByFullName() {
    return people.stream().sorted(Comparator.comparing(Person::getFullname)).collect(Collectors.toList());
  }

  //Dùng stream group thành Map: key=city,value=age trung bình, rồi tạo Map khác đổi double thành float
  @Override
  public HashMap<String, Float> averageCityAge() {
    Map<String, Double> map=people.stream().collect(groupingBy(Person::getCity,averagingDouble(Person::getBirthday)));

    HashMap<String,Float> map1=new HashMap<>();

    for (Map.Entry<String, Double> entry : map.entrySet()) {
      map1.put(entry.getKey(),Float.valueOf(String.valueOf(entry.getValue())));
    }

    return map1;
  }

  //Tương tự với averageCityAge
  @Override
  public HashMap<String, Float> averageJobAge() {
    Map<String, Double> map=people.stream().collect(groupingBy(Person::getJob,averagingDouble(Person::getBirthday)));

    HashMap<String,Float> map1=new HashMap<>();

    for (Map.Entry<String, Double> entry : map.entrySet()) {
      map1.put(entry.getKey(),Float.valueOf(String.valueOf(entry.getValue())));
    }

    return map1;
  }

  //Tương tự với averageCityAge
  @Override
  public HashMap<String, Float> averageJobSalary() {
    Map<String, Double> map=people.stream().collect(groupingBy(Person::getJob,averagingDouble(Person::getSalary)));
    HashMap<String,Float> map1=new HashMap<>();
    for (Map.Entry<String, Double> entry : map.entrySet()) {
      map1.put(entry.getKey(),Float.valueOf(String.valueOf(entry.getValue())));
    }
    return map1;
  }


  @Override
  public List<String> find5CitiesHaveMostSpecificJob(String job) {
    List<String> list=people.stream().filter(m->m.getJob().equals(job)).map(Person::getCity).collect(toList());

    Map<String,Integer> Map1=new HashMap<>();

    for(String city:list){
      if(!Map1.containsKey(city)) {
        Map1.put(city, (int) list.stream().filter(m -> m.equals(city)).count());
      }
    }

    List<String> result=Map1.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey).limit(5).collect(toList());

    return result;
  }

  @Override
  public HashMap<String, Float> top5HighestSalaryCities() {
    Map<String, Float> map=new HashMap<>();

    people.stream().collect(groupingBy(Person::getCity,averagingDouble(Person::getSalary)))
            .forEach((key,val)->map.put(key,Float.valueOf(String.valueOf(val))));

    return map.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .limit(5).collect(toMap(Map.Entry::getKey,Map.Entry::getValue,(e1,e2)->e1,LinkedHashMap::new));
  }
 
  
}
