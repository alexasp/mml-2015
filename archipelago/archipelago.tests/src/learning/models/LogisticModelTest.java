package learning.models;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by alex on 3/9/15.
 */
public class LogisticModelTest {

    @Test(expected = IllegalArgumentException.class)
    public void noParameters_Throws(){
        LogisticModel model = new LogisticModel(new double[]{});
    }

}
