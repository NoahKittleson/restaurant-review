import java.util.List;
import org.sql2o.*;

public class Review {
  private int id;
  private String title;
  private String text;
  private int restaurantId;

  public Review(String title, String text, int restaurantId) {
    this.title = title;
    this.restaurantId = restaurantId;
  }

  public String getTitle() {
    return title;
  }

  public int getId() {
    return id;
  }

  public int getRestaurantId() {
    return restaurantId;
  }

  public static List<Review> all() {
    String sql = "SELECT id, title, text, restaurantId FROM reviews";
    try (Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Review.class);
    }
  }

  @Override
  public boolean equals(Object otherReview) {
    if (!(otherReview instanceof Review)) {
      return false;
    } else {
      Review newReview = (Review) otherReview;
      return this.getTitle().equals(newReview.getTitle())
      && this.getId() == newReview.getId()
      && this.getRestaurantId() == newReview.getRestaurantId();
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO reviews (title, text, restaurantId) VALUES (:title, :text, :restaurantId)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("title", this.title)
        .addParameter("text", this.text)
        .addParameter("restaurantId", this.restaurantId)
        .executeUpdate()
        .getKey();
    }
  }

  public static Review find (int id) {
    try (Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM reviews where id=:id";
      Review review = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Review.class);
      return review;
    }
  }
}
