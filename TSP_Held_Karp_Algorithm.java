import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Map.Entry;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.List.*;

// 集合はintで表せ、bit演算を用いることで高速に動かせる。

public class TSP_Held_Karp_Algorithm {
  int[][] cityData;

  int len;

  Map<CityAndViaCities, CostAndCity> map;

  public int tsp_shortest_cost;

  public int[] tsp_shortest_path;

  TSP_Held_Karp_Algorithm(List<List<Integer>> data) {
    len = data.size();
    cityData = new int[len][len];
    cityData = data.stream()
        .map(innerList -> innerList.stream().mapToInt(Integer::intValue).toArray())
        .toArray(int[][]::new);
    map = new HashMap<>();
  }

  public static TSP_Held_Karp_Algorithm from(BufferedReader reader) throws IOException {
    List<List<Integer>> tmpData = new ArrayList<List<Integer>>();

    String data;

    while ((data = reader.readLine()) != null) {
      List<Integer> tmpDataLine = new ArrayList<Integer>();

      Scanner scanner = new Scanner(data);
      scanner.useDelimiter(" ");
      while (scanner.hasNext())
        tmpDataLine.add(Integer.parseInt(scanner.next()));
      scanner.close();
      tmpData.add(tmpDataLine);
    }
    reader.close();

    return new TSP_Held_Karp_Algorithm(tmpData);
  }

  public static TSP_Held_Karp_Algorithm from(ZipInputStream zip) throws IOException {
    InputStreamReader isr = new InputStreamReader(zip);
    BufferedReader br = new BufferedReader(isr);

    ZipEntry entry = zip.getNextEntry();
    return TSP_Held_Karp_Algorithm.from(br);
  }

  private CostAndCity find_shortest_path_to_via(int last_city, BigInteger via_cities, int[] cities) {
    int prev_last_city = cities[0]; // 初期化がないと警告が出される
    int cost = -1;
    // System.out.println(
    // "find_shortest_path_to_via(last_city:" + last_city + ", " +
    // via_cities.toString(2) + ", "
    // + Arrays.toString(cities) + ")");

    for (int prev_last_city_i : cities) {
      if (prev_last_city_i == last_city)
        continue;
      BigInteger prev_via_cities = via_cities.clearBit(prev_last_city_i);

      CostAndCity costAndCity = map.get(new CityAndViaCities(prev_last_city_i, prev_via_cities));
      if (costAndCity == null) {
        CityAndViaCities citiestest = new CityAndViaCities(prev_last_city_i,
            prev_via_cities);
        System.out.println("citiestest: " + citiestest.toString());

        for (Entry<CityAndViaCities, CostAndCity> entry : map.entrySet()) {
          // if (citiestest != entry.getKey()) {
          // System.out.println("citiestest != entry.getKey()");
          // }
          CityAndViaCities key = entry.getKey();
          CostAndCity value = entry.getValue();
          if (citiestest.equals(key)) {
            System.out.println("equal key");
          }
          if (key.city == 128 && key.cities.equals(citiestest.cities)) {
            System.out.println("equal key alt");
            System.out.println(citiestest.city);
          }
          if (key.city.equals(citiestest.city) && key.cities.equals(citiestest.cities)) {
            System.out.println("equal key alt alt");
          }
          if (key.city == 128 && key.cities.equals(BigInteger.ZERO))
            System.out.println(key.toString() + " : " + value.toString());
        }

        if (citiestest.city == 128 && citiestest.cities.equals(BigInteger.ZERO)) {
          System.out.println("citiestest.toString()");
        }

      }
      int prev_cities_cost = costAndCity.cost;

      if (cost == -1 || prev_cities_cost + cityData[prev_last_city_i][last_city] < cost) {
        cost = prev_cities_cost + cityData[prev_last_city_i][last_city];
        prev_last_city = prev_last_city_i;
      }
    }

    return new CostAndCity(cost, prev_last_city);
  }

  // 都市0が開始。他は回せばよい
  public void find_tsp_shortest_path() {
    final long start_time = System.currentTimeMillis();
    long diff;

    for (int i = 1; i < len; ++i)
      map.put(new CityAndViaCities(i, 0), new CostAndCity(cityData[0][i], 0));

    for (int via_cities_size = 1; via_cities_size < len - 1; ++via_cities_size) {
      diff = System.currentTimeMillis();
      // (1), (2), ...; (1 2), (1 3), (2 3), ...; (1 2 3), (1 2 4) ...
      // 的な感じで増える
      // System.out.println(via_cities_size);

      for (int[] cities : CombinationGenerator.generateCombinations(len - 1, via_cities_size)) {
        // System.out.println(Arrays.toString(cities));

        BigInteger via_cities = BigInteger.ZERO; // 通った町はまだ一つもない
        for (int city : cities)
          via_cities = via_cities.setBit(city);

        for (int last_city = 1; last_city < len; ++last_city)
          if (!via_cities.testBit(last_city))
            map.put(new CityAndViaCities(last_city, via_cities),
                find_shortest_path_to_via(last_city, via_cities, cities));
      }

      System.out
          .println(via_cities_size + " / " + (len - 1) + ", all: " + (System.currentTimeMillis() - start_time) / 1000
              + "s, diff: " + (System.currentTimeMillis() - diff) / 1000 + "s");
    }

    BigInteger via_cities = BigInteger.ONE.shiftLeft(len).subtract(BigInteger.TWO);
    map.put(new CityAndViaCities(0, via_cities),
        find_shortest_path_to_via(0, via_cities, IntStream.range(1, len).toArray()));

    Integer min_cost = map.get(new CityAndViaCities(0, via_cities)).cost;
    List<Integer> reversed_path = new ArrayList<Integer>();
    reversed_path.add(0);

    int last_city = 0;
    for (int i = 0; i < len - 1; ++i) {
      // System.out.println(new CityAndViaCities(last_city, via_cities).toString());

      last_city = map.get(new CityAndViaCities(last_city, via_cities)).city;
      reversed_path.add(last_city);
      via_cities = via_cities.clearBit(last_city);
    }

    reversed_path.add(0);
    // This is java 21
    // tsp_shortest_path =
    // reversed_path.reversed().stream().mapToInt(Integer::intValue).toArray();
    Collections.reverse(reversed_path);
    reversed_path.stream().mapToInt(Integer::intValue).toArray();
    tsp_shortest_cost = min_cost;
  }

  public static void main(String[] args) {
    TSP_Held_Karp_Algorithm tsp;
    try {
      // File file = new File("test1.dat");
      // tsp = TSP_Held_Karp_Algorithm.from(new BufferedReader(new FileReader(file)));
      File file = new File("table.zip");
      tsp = TSP_Held_Karp_Algorithm.from(new ZipInputStream(new FileInputStream(file)));
      // System.out.println(Arrays.deepToString(tsp.cityData));

      tsp.find_tsp_shortest_path();
      System.out.println(tsp.tsp_shortest_cost);
      System.out.println(Arrays.toString(tsp.tsp_shortest_path));
    } catch (FileNotFoundException e) {
      System.out.println(e);
      return;
    } catch (IOException e) {
      System.out.println(e);
      return;
    }
    // System.out.println(Arrays.deepToString(tsp.cityData));
  }
}
