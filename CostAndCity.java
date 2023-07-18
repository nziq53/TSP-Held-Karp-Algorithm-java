public class CostAndCity {
  public Integer cost; // コスト
  public Integer city; // 直前の街

  public CostAndCity(Integer _cost, Integer _city) {
    cost = _cost;
    city = _city;
  }

  @Override
  public String toString() {
    return "(cost:" + cost.toString() + ", city:" + city.toString() + ")";
  }
}
