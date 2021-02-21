package vn.techmaster.learncollection;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import vn.techmaster.learncollection.model.Person;
import vn.techmaster.learncollection.repository.PersonRepositoryInterface;

@SpringBootTest
class PersonRepositoryTest {

	@Autowired
	PersonRepositoryInterface personRepository;

	@Test
	public void getAll() {
		List<Person> people = personRepository.getAll();
		assertThat(people).hasSize(20);
	}

	@Test
	public void getSortedCities(){
		List<String> sortedCities = personRepository.getSortedCities();
		sortedCities.forEach(System.out::println);  //In theo tất các thành phố ra để kiểm tra xem có sắp xếp không
	/*
		Cách này viết dài
		assertThat(sortedCities).contains("Paris", "Dubai");
		assertThat(sortedCities).isSortedAccordingTo(Comparator.naturalOrder());*/

		//Cách này chain các điều kiện test với nhau ngắn gọn và đẹp hơn
		assertThat(sortedCities).isSortedAccordingTo(Comparator.naturalOrder())
				.contains("Berlin", "Budapest", "Buenos Aires", "Copenhagen", "Hanoi", "Jakarta","Mexico City","Zagreb");
	}

	@Test
	public void getSortedJobs(){
		List<String> sortedJobs = personRepository.getSortedJobs();
		sortedJobs.forEach(System.out::println);

		assertThat(sortedJobs).isSortedAccordingTo(Comparator.naturalOrder())
				.contains("Pole Dancer", "Bartender", "Developer", "Personal Trainer", "Soldier", "Teacher", "Taxi Driver", "Nurse", "Musician");

	}

	@Test
	public void sortPeopleByFullNameReversed() {
		List<Person> sortedPeople = personRepository.sortPeopleByFullNameReversed();
		sortedPeople.forEach(person -> System.out.println(person));
		assertThat(sortedPeople).isSortedAccordingTo(Comparator.comparing(Person::getFullname).reversed());
	}

	//Hiển thị 5 thành phố có tên nhiều nhất trong danh sách (key = city, value = số lượng city)
	@Test
	public void getTop5Cites(){
		Map<String, Integer> map=personRepository.findTop5Citis();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị 5 job có tên nhiều nhất trong danh sách (key = job, value = số lượng job)
	@Test
	public void getTop5Jobs(){
		Map<String, Integer> map=personRepository.findTop5Jobs();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị job có tên nhiều nhất ở mỗi thành phố trong danh sách (key = city,value = job)
	@Test
	public void TopJobInCity(){
		Map<String,String> map=personRepository.findTopJobInCity();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị danh sách người ứng với từng thành phố một (key = city, value = List person)
	@Test
	public void groupPeopleByCity(){
		Map<String,List<Person>> map=personRepository.groupPeopleByCity();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị danh sách các job với mức lương trung bình tương ứng (key = job, value = lương trung bình)
	@Test
	public void averageJobSalary(){
		Map<String,Float> map=personRepository.averageJobSalary();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị danh sách các job với độ tuổi trung bình tương ứng (key = job, value = tuổi trung bình)
	@Test
	public void averagejobAge(){
		Map<String,Float> map=personRepository.averageJobAge();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị danh sách các thành phố với độ tuổi trung bình tương ứng (key = city, value = tuổi trung bình)
	@Test
	public void averagecityage(){
		Map<String,Float> map=personRepository.averageCityAge();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));
	}

	//Hiển thị danh sách 5 thành phố chứa job được chọn nhiều nhất
	@Test
	public void find5CitiesHaveMostSpecificJb(){
		List<String> list=personRepository.find5CitiesHaveMostSpecificJob("Film Maker");
		list.forEach(System.out::println);
	}

	@Test
	public void top5HighestSalaryCities(){
		HashMap<String,Float> map=personRepository.top5HighestSalaryCities();
		map.forEach((key,value)->System.out.println("Key ="+key+" Value ="+value));

	}
}
