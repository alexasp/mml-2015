package learning;

import com.google.inject.Inject;

import java.util.List;

/**
 * Created by alex on 4/23/15.
 */
public class ModelMerger {

    private final ModelFactory _modelFactory;

    @Inject
    public ModelMerger(ModelFactory modelFactory){
        _modelFactory = modelFactory;
    }

    public ParametricModel merge(List<ParametricModel> models) {
        double[] newParameters = new double[models.get(0).getParameters().length];

        for (ParametricModel model : models) {
            addToVector(newParameters, model.getParameters());
        }

        for (int i = 0; i < newParameters.length; i++) {
            newParameters[i] = newParameters[i] / (double) models.size();
        }

        return _modelFactory.getModel(newParameters);
    }

    private void addToVector(double[] target, double[] value) {
        for(int i = 0; i < target.length; i++){
            target[i] += value[i];
        }
    }
}
