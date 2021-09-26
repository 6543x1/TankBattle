import entity.Coordinate;
import org.junit.Test;

public class MyTest {

    @Test
    public void testHashCode() {
        Coordinate a = new Coordinate(10, 20);
        Coordinate b = new Coordinate(10, 20);
        System.out.println(a.equals(b));
        System.out.println(a.hashCode());
        System.out.println(b.hashCode());
    }

}
