package learning.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 3/9/15.
 */
public class LogisticModelTest {

    @Test(expected = IllegalArgumentException.class)
    public void noParameters_Throws(){
        LogisticModel model = new LogisticModel(new double[]{});
    }

    @Test
    public void gradientUpdate() {
        double[] parameters = new double[]{2.0, 3.0};
        LogisticModel model = new LogisticModel(parameters);

        model.gradientUpdate(new double[]{-1.0, 0.5});

        double[] newParameters = model.getParameters();
        assertEquals(parameters[0] - 1.0, newParameters[0], 0.0001d);
        assertEquals(parameters[1] + 0.5, newParameters[1], 0.0001d);
    }

}
