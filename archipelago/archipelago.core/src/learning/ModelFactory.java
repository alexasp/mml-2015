package learning;

/**
 * Created by alex on 4/9/15.
 */
public interface ModelFactory {

    public ParametricModel getModel(double[] parameters);

    public ParametricModel getModel(int size);

    public ParametricModel getModel(String serializedModel);
}
