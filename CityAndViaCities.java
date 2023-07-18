import java.math.BigInteger;

// costAndCityを取得するためのキー
public class CityAndViaCities {
  public Integer city; // 最後の都市
  public BigInteger cities; // 経由した都市のビット集合

  public CityAndViaCities(Integer _city, BigInteger _cities) {
    city = _city;
    cities = _cities;
  }

  public CityAndViaCities(int _city, int _cities) {
    city = _city;
    cities = BigInteger.valueOf(_cities);
  }

  @Override
  public int hashCode() {
    return city.hashCode() * 31 + cities.hashCode();
    // return 1;
  }

  @Override
  public String toString() {
    return "(city:" + city.toString() + ", cities:" + cities.toString(2) + ")";
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof CityAndViaCities)) {
      return false;
    }
    CityAndViaCities item = (CityAndViaCities) obj;
    // System.out.println("equal");
    // System.out.println(this.toString());
    // System.out.println(item.toString());
    // if (item.city == this.city) {
    // System.out.println("item.city == this.city");
    // }
    // if (item.cities.equals(this.cities)) {
    // System.out.println("item.cities == this.cities");
    // }
    if (item.city.equals(this.city) && item.cities.equals(this.cities)) {
      // System.out.println("equal");
      return true;
    }
    // System.out.println("equal not");
    return false;
  }
}
