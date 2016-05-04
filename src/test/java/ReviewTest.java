import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.Arrays;

public class ReviewTest {

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
  public void Review_instantiatesCorrectly_true() {
    Review myReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    assertEquals(true, myReview instanceof Review);
  }

  @Test
  public void Review_reviewInstantiesWithTitle_true() {
    Review myReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    assertEquals("Killer Burger rules", myReview.getTitle());
  }

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Review.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfFieldsAretheSame() {
    Review firstReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    Review secondReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    assertTrue(firstReview.equals(secondReview));
  }

  @Test
  public void save_savesIntoDatabase_true() {
    Review myReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    myReview.save();
    assertTrue(Review.all().get(0).equals(myReview));
  }

  @Test
  public void save_assignsIdToObject() {
    Review myReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    myReview.save();
    Review savedReview = Review.all().get(0);
    assertEquals(myReview.getId(), savedReview.getId());
  }

  @Test
  public void find_findReviewInDatabase_true() {
    Review myReview = new Review("Killer Burger rules", "yo but it totally does", 0);
    myReview.save();
    Review savedReview = Review.find(myReview.getId());
    assertTrue(myReview.equals(savedReview));
  }
}
