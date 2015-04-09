package learning;

/**
 * Created by alex on 4/9/15.
 */
public interface ModelFactory {

    public Model getModel(double[] parameters);

    public Model getModel(int size);

    public Model getModel(String serializedModel);
}
