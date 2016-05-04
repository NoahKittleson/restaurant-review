import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class RestaurantTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/restaurant_review_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteRestaurantsQuery = "DELETE FROM restaurants *;";
      String deleteReviewsQuery = "DELETE FROM reviews *;";
      con.createQuery(deleteRestaurantsQuery).executeUpdate();
      con.createQuery(deleteReviewsQuery).executeUpdate();
    }
  }

  @Test
  public void Restaurant_instantiatesCorrectly_true() {
    Restaurant myRestaurant = new Restaurant("Killer Burger", 1);
    assertEquals(true, myRestaurant instanceof Restaurant);
  }


  @Test
  public void Restaurant_reviewInstantiesWithTitle_true() {
    Restaurant myRestaurant = new Restaurant("Killer Burger", 1);
    assertEquals("Killer Burger", myRestaurant.getName());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Restaurant.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfFieldsAretheSame() {
    Restaurant firstRestaurant = new Restaurant("Killer Burger", 1);
    Restaurant secondRestaurant = new Restaurant("Killer Burger", 1);
    assertTrue(firstRestaurant.equals(secondRestaurant));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Restaurant myRestaurant = new Restaurant("Killer Burger", 1);
    myRestaurant.save();
    assertTrue(Restaurant.all().get(0).equals(myRestaurant));
  }

  @Test
  public void save_assignsIdToObject() {
    Restaurant myRestaurant = new Restaurant("Killer Burger", 1);
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.all().get(0);
    assertEquals(myRestaurant.getId(), savedRestaurant.getId());
  }

  @Test
  public void find_findRestaurantInDatabase_true() {
    Restaurant myRestaurant = new Restaurant("Killer Burger", 1);
    myRestaurant.save();
    Restaurant savedRestaurant = Restaurant.find(myRestaurant.getId());
    assertTrue(myRestaurant.equals(savedRestaurant));
  }

  @Test
  public void getReviews_retrievesAllReviewsFromDataBase_reviewsList() {
    Restaurant myRestaurant = new Restaurant("Killer Burger", 1);
    myRestaurant.save();
    Review firstReview = new Review("Killer Burger rules", "yo but it totally does", myRestaurant.getId());
    firstReview.save();
    Review secondReview = new Review("Killer Burger sucks", "naw jk", myRestaurant.getId());
    secondReview.save();
    Review[] tasks = new Review[] { firstReview, secondReview };
    assertTrue(myRestaurant.getReviews().containsAll(Arrays.asList(tasks)));
  }

}
