package vn.techmaster.topcar.repository;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.util.ResourceUtils;
import vn.techmaster.topcar.model.Car;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TopCar implements CRUD<Car>{
    List<Car> cars=new ArrayList<>();

    public TopCar(String CSVfile){
        readCSV(CSVfile);
        int id=1;
        for (Car car:cars){
            car.setId(id++);
        }
    }

    @Override
    public void readCSV(String file) {
        try {
            File file1= ResourceUtils.getFile("classpath:static/"+file);
            CsvMapper mapper = new CsvMapper();
            CsvSchema schema=CsvSchema.emptySchema().withHeader();
            ObjectReader oReader=mapper.readerFor(Car.class).with(schema);
            Reader reader=new FileReader(file1);
            MappingIterator<Car> mi=oReader.readValues(reader);
            while (mi.hasNext()){
                Car car=mi.next();
                cars.add(car);
            }
        }catch (IOException e){

        }
    }

    @Override
    public List<Car> getAll()
    {
        return cars;
    }

    public Optional<Car> get(int id) {
        return cars.stream().filter(u -> u.getId() == id).findFirst();
    }

    @Override
    public void add(Car car) {
        //Cơ chế tự tăng
        int id;
        if (cars.isEmpty()) {
            id = 1;
        } else {
            Car lastCar = cars.get(cars.size() - 1);
            id = lastCar.getId() + 1;
        }
        car.setId(id);
        cars.add(car);
    }

    @Override
    public void update(Car car) {
        get(car.getId()).ifPresent(existbook -> {
            existbook.setModel(car.getModel());
            existbook.setManufacture(car.getManufacture());
            existbook.setPrice(car.getPrice());
            existbook.setSale(car.getSale());
        });
    }

    @Override
    public void delete(Car car) {
        deleteById(car.getId());
    }

    @Override
    public void deleteById(int id) {
        get(id).ifPresent(existcar -> cars.remove(existcar));
    }

    @Override
    public List<Car> searchByKeyword(String keyword) {
        //Tham khảo chi tiết ở đây nhé. Đây là Lambda Expression có từ Java 8.
        //https://www.baeldung.com/java-stream-filter-lambda
        return cars
                .stream()
                .filter(car -> car.matchWithKeyword(keyword))
                .collect(Collectors.toList());
    }
}
