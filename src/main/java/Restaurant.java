import java.util.List;
import org.sql2o.*;

public class Restaurant {
  private int id;
  public String name;
  private int cuisineId;

  public Restaurant(String name, int cuisineId) {
    this.name = name;
    this.cuisineId = cuisineId;
  }

  public String getName() {
    return name;
  }

  public String testName(String newName) {
    name = newName;
    return name;
  }

  public int getId() {
    return id;
  }

  public int getCuisineId() {
    return cuisineId;
  }

  public static List<Restaurant> all() {
    String sql = "SELECT id, name, cuisineId FROM restaurants";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Restaurant.class);
    }
  }

  @Override
  public boolean equals(Object otherRestaurant) {
    if (!(otherRestaurant instanceof Restaurant)) {
      return false;
    } else {
      Restaurant newRestaurant = (Restaurant) otherRestaurant;
      return this.getName().equals(newRestaurant.getName())
      && this.getId() == newRestaurant.getId()
      && this.getCuisineId() == newRestaurant.getCuisineId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO restaurants (name, cuisineId) VALUES (:name, :cuisineId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .addParameter("cuisineId", this.cuisineId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Restaurant find (int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM restaurants where id=:id";
      Restaurant restaurant = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Restaurant.class);
      return restaurant;
    }
  }

  public List<Review> getReviews() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews where restaurantId=:restaurantId";
      return con.createQuery(sql)
        .addParameter("restaurantId", this.id)
        .executeAndFetch(Review.class);
    }
  }


}
