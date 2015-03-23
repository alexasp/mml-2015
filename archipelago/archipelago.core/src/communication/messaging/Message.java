package communication.messaging;

import learning.Model;
import learning.models.LogisticModel;

/**
 * Created by alex on 3/9/15.
 */
public class Message {
    private final Model _model;

    public Message(Model model) {
        _model = model;
    }

    public Model getModel() {
        return _model;
    }
}
